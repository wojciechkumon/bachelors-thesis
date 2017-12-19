#include <iostream>
#include <sys/stat.h>
#include <fstream>
#include <sys/inotify.h>
#include <unistd.h>
#include <ctime>
#include <vector>

#define EVENT_SIZE  ( sizeof (struct inotify_event) )
#define BUF_LEN     ( 1024 * ( EVENT_SIZE + 16 ) )
#define TMP_REQUEST_ENDING "_request_tmp"
#define REQUEST_ENDING "_request"
#define TMP_RESPONSE_ENDING "_response_tmp"
#define RESPONSE_ENDING "_response"

using namespace std;

bool isFileCreated(struct inotify_event *event);

void handleFile(char newFileNameChars[], const char *dirPath);

bool isTmpRequestFile(string &fileName);

void handleTmpRequestFile(string &tmpRequestFileName, const char *dirPath);

string extractBaseFileName(string &fileName);

bool fileExists(const std::string &name);

void waitForRequestFile(string &path);

void sleepMillisecond();

int readRequestAndResponseSize(string &path);

vector<char> readAllBytes(string &path);

int fromBytesToInt(vector<char> &bytes);

void putBytesToResponseFile(const char *dirPath, string &baseFileName, int responseSize);


int main() {
    const char *dirPath = "/home/wojtas626/IdeaProjects/praca_inz/transfer_tester/target/dist/integration_files";

    char buffer[BUF_LEN];
    int fd = inotify_init();

    if (fd < 0) {
        perror("inotify_init");
        return -1;
    }

    int wd = inotify_add_watch(fd, dirPath, IN_CREATE);

    while (true) {
        int i = 0;
        int length = read(fd, buffer, BUF_LEN);

        if (length < 0) {
            perror("read");
        }

        while (i < length) {
            struct inotify_event *event = (struct inotify_event *) &buffer[i];
//            cout << "event: " << event->name << endl;
            if (isFileCreated(event)) {
                handleFile(event->name, dirPath);
            }
            i += EVENT_SIZE + event->len;
        }
    }

    inotify_rm_watch(fd, wd);
    close(fd);

    return 0;
}

bool isFileCreated(struct inotify_event *event) {
    return event->len
           && (event->mask & (IN_ALL_EVENTS))
           && !(event->mask & IN_ISDIR);
}

void handleFile(char newFileNameChars[], const char *dirPath) {
    string newFileName(newFileNameChars);
    if (!isTmpRequestFile(newFileName)) {
        return;
    }

    handleTmpRequestFile(newFileName, dirPath);
}

bool isTmpRequestFile(string &fileName) {
    return fileName.find(TMP_REQUEST_ENDING) != string::npos;
}

void handleTmpRequestFile(string &tmpRequestFileName, const char *dirPath) {
    string baseFileName = extractBaseFileName(tmpRequestFileName);
    string requestName = baseFileName + REQUEST_ENDING;
    string path(dirPath);
    path += "/" + requestName;
//    cout << "path: " << path << endl;

    waitForRequestFile(path);

    int responseSize = readRequestAndResponseSize(path);

    putBytesToResponseFile(dirPath, baseFileName, responseSize);
}

string extractBaseFileName(string &fileName) {
    size_t index = fileName.find(TMP_REQUEST_ENDING);
    return fileName.substr(0, index);
}

bool fileExists(const std::string &name) {
    struct stat buffer;
    return (stat(name.c_str(), &buffer) == 0);
}

void waitForRequestFile(string &path) {
    bool exists = false;
    for (int i = 0; i < 1000; ++i) {
        if (fileExists(path)) {
            exists = true;
            break;
        }
        sleepMillisecond();
    }
    if (!exists) {
        cout << "File still not exists! " << path << endl;
    }
}

void sleepMillisecond() {
    struct timespec req = {0};
    req.tv_sec = 0;
    req.tv_nsec = 1000000L;
    nanosleep(&req, (struct timespec *) nullptr);
}

int readRequestAndResponseSize(string &path) {
    // read 4 bytes response size
    // read whole request (to EOF)
    ifstream infile(path, ios::in | ios::binary);
    vector<char> allBytes = readAllBytes(path);
    int responseSize = fromBytesToInt(allBytes);
//    cout << "responseSize: " << responseSize << ", requestSize: " << (allBytes.size() - 4) << endl;
    return responseSize;
}

vector<char> readAllBytes(string &path) {
    ifstream ifs(path, ios::binary | ios::ate);
    ifstream::pos_type pos = ifs.tellg();

    std::vector<char> result(pos);

    ifs.seekg(0, ios::beg);
    ifs.read(&result[0], pos);

    return result;
}

int fromBytesToInt(vector<char> &bytes) {
    return (bytes[0] << 24) | ((bytes[1] << 16) & 16777215) | ((bytes[2] << 8) & 65535) | (bytes[3] & 255);
}

void putBytesToResponseFile(const char *dirPath, string &baseFileName, int responseSize) {
    string tmpResponsePath(dirPath);
    tmpResponsePath += "/" + baseFileName + TMP_RESPONSE_ENDING;

    ofstream tmpResponseStream(tmpResponsePath);
    if (tmpResponseStream.is_open()) {
        char* buffer = new char[responseSize];
        tmpResponseStream.write(buffer, responseSize);
        tmpResponseStream.flush();
        delete[] buffer;
        tmpResponseStream.close();
    }

    string responsePath(dirPath);
    responsePath += "/" + baseFileName + RESPONSE_ENDING;
    rename(tmpResponsePath.c_str(), responsePath.c_str());
    // build response with correct size
    // save response to tmp file
    // atomically rename tmp file to end with _response and start same as _request
}