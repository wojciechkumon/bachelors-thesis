#include <cstdio>
#include <cstring>
#include <unistd.h>
#include <sys/socket.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <iostream>

#define BUFFER_SIZE 1024
#define INT_SIZE 4
#define ZERO_OFFSET 0


void sendResponse(int fd, int size);

void fillBuffer(char buffer[1024], int fill);

int fromBytesToInt(const char *bytes);

void handleClient(int new_conn_fd) {
    char intBuffer[4];

    recv(new_conn_fd, intBuffer, INT_SIZE, ZERO_OFFSET);
    int requestSize = fromBytesToInt(intBuffer);

    recv(new_conn_fd, intBuffer, INT_SIZE, ZERO_OFFSET);
    int responseSize = fromBytesToInt(intBuffer);

    int numberOfChunks = (requestSize / BUFFER_SIZE);
    char messageFromClient[BUFFER_SIZE];
    for (int i = 0; i < numberOfChunks; i++) {
        recv(new_conn_fd, messageFromClient, BUFFER_SIZE, ZERO_OFFSET);
    }
    if (requestSize % BUFFER_SIZE > 0) {
        recv(new_conn_fd, messageFromClient, requestSize % BUFFER_SIZE, ZERO_OFFSET);
    }

//    std::cout << "requestSize: " << requestSize << ", responseSize: " << responseSize
//              << ", numberOfChunks: " << numberOfChunks << std::endl;
    sendResponse(new_conn_fd, responseSize);
//    std::cout << "message sent" << std::endl;
    close(new_conn_fd);
}

int fromBytesToInt(const char *bytes) {
    return (bytes[0] << 24) | ((bytes[1] << 16) & 16777215) | ((bytes[2] << 8) & 65535) | (bytes[3] & 255);
}

void sendResponse(int fd, int sizeToSend) {
    char buffer[BUFFER_SIZE];
    int sizeLeft = sizeToSend;
    while (sizeLeft > 0) {
        int bufferSizeToFill = sizeLeft < BUFFER_SIZE ? sizeLeft : BUFFER_SIZE;
        fillBuffer(buffer, bufferSizeToFill);
        send(fd, buffer, (size_t) bufferSizeToFill, 0);
        sizeLeft -= BUFFER_SIZE;
    }
}

void fillBuffer(char *buffer, int sizeToFill) {
    for (int i = 0; i < sizeToFill; i++) {
        buffer[i] = (i % 26) + 65;
    }
}

void *get_in_addr(struct sockaddr *sa) {
    if (sa->sa_family == AF_INET) {
        return &(((struct sockaddr_in *) sa)->sin_addr);
    }

    return &(((struct sockaddr_in6 *) sa)->sin6_addr);
}

int main(int argc, char *argv[]) {
    int status;
    struct addrinfo hints, *res;
    int listner;
    const char *port = "5000";

    // Before using hint you have to make sure that the data structure is empty
    memset(&hints, 0, sizeof hints);
    // Set the attribute for hint
    hints.ai_family = AF_UNSPEC; // We don't care V4 AF_INET or 6 AF_INET6
    hints.ai_socktype = SOCK_STREAM; // TCP Socket SOCK_DGRAM
    hints.ai_flags = AI_PASSIVE;

    // Fill the res data structure and make sure that the results make sense.
    status = getaddrinfo(NULL, port, &hints, &res);
    if (status != 0) {
        fprintf(stderr, "getaddrinfo error: %s\n", gai_strerror(status));
        return -1;
    }

    // Create Socket and check if error occured afterwards
    listner = socket(res->ai_family, res->ai_socktype, res->ai_protocol);
    if (listner < 0) {
        fprintf(stderr, "socket error: %s\n", gai_strerror(status));
        return -1;
    }

    // Bind the socket to the address of my local machine and port number
    status = bind(listner, res->ai_addr, res->ai_addrlen);
    if (status < 0) {
        fprintf(stderr, "listen: %s\n", gai_strerror(status));
        return -1;
    }

    status = listen(listner, 10);
    if (status < 0) {
        fprintf(stderr, "listen: %s\n", gai_strerror(status));
        return -1;
    }

    // Free the res linked list after we are done with it
    freeaddrinfo(res);


    // We should wait now for a connection to accept
    int new_conn_fd;
    struct sockaddr_storage client_addr;
    socklen_t addr_size;
    char s[INET6_ADDRSTRLEN]; // an empty string

    // Calculate the size of the data structure
    addr_size = sizeof client_addr;

    printf("I am now accepting connections...\n");

    while (true) {
        new_conn_fd = accept(listner, (struct sockaddr *) &client_addr, &addr_size);
        if (new_conn_fd < 0) {
            fprintf(stderr, "accept error: %s\n", gai_strerror(new_conn_fd));
            continue;
        }
        inet_ntop(client_addr.ss_family, get_in_addr((struct sockaddr *) &client_addr), s, sizeof s);
//        printf("I am now connected to %s \n", s);
        handleClient(new_conn_fd);
    }
}