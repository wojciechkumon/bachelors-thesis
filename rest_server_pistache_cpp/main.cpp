#include <iostream>
#include <pistache/endpoint.h>

using namespace Pistache;

class HelloHandler : public Http::Handler {
public:

HTTP_PROTOTYPE(HelloHandler)

    void onRequest(const Http::Request &request, Http::ResponseWriter response) {
        response.send(Http::Code::Ok, "Hello, World");
    }
};

int main() {
    std::string port = "5000";
    std::cout << "Starting http server, port: " << port << std::endl;
    Http::listenAndServe<HelloHandler>("*:" + port);

    return 0;
}

