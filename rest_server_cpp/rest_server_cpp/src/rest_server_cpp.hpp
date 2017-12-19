#ifndef REST_SERVER_CPP_H
#define REST_SERVER_CPP_H

#include <ngrest/common/Service.h>
#include <iostream>

// '*location' comment sets resource path for this service
// *location: rest_server_cpp
class rest_server_cpp : public ngrest::Service {
public:

    // To invoke this operation send POST with "data" in json to: http://localhost:9098/rest_server_cpp/World!
    // '*method' metacomment sets HTTP method for the operation. GET is default method.
    // *method: POST
    //
    std::string testResult(const std::string data, const int responseSize) {
//        std::cout << "Data size received: " << data.length() << ", responseSize: " << responseSize << std::endl;

        std::string responseLetters(responseSize, 'A');
        return responseLetters;
    }
};


#endif // REST_SERVER_CPP_H