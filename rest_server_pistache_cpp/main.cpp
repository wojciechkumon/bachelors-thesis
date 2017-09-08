#include <iostream>
#include <cstring>
#include <pistache/endpoint.h>
#include "rapidjson/document.h"
#include "rapidjson/writer.h"

using namespace Pistache;
using namespace rapidjson;

class TransferTesterHandler : public Http::Handler {
public:

HTTP_PROTOTYPE(TransferTesterHandler)

    void onRequest(const Http::Request &request, Http::ResponseWriter response) {
        Document document;
        document.Parse(request.body().c_str());
        int responseSize = document["responseSize"].GetInt();
        std::cout << "data length: " << strlen(document["data"].GetString());
        std::cout << ", responseSize: " << responseSize << std::endl;

        std::string responseBody = generateResponse(responseSize);
        response.send(Http::Code::Ok, responseBody);
    }

private:
    std::string generateResponse(int responseSize) {
        char* response = new char[responseSize + 1];
        for (int i = 0; i < responseSize; i++) {
            response[i] = (rand() % 26) + 65;
        }
        response[responseSize] = '\0';

        Document document;
        document.SetObject();
        Document::AllocatorType& allocator = document.GetAllocator();

        Value resultValue;
        resultValue.SetString(response, allocator);
        document.AddMember("result", resultValue, allocator);

        StringBuffer buffer;
        Writer<StringBuffer> writer(buffer);
        document.Accept(writer);

        return std::string(buffer.GetString());
    }
};


int main() {
    std::string port = "9098";
    std::cout << "Starting http server, port: " << port << std::endl;
    Http::listenAndServe<TransferTesterHandler>("*:" + port);

    return 0;
}

