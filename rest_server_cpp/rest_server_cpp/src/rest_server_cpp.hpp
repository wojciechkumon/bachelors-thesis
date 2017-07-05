#ifndef REST_SERVER_CPP_H
#define REST_SERVER_CPP_H

#include <ngrest/common/Service.h>

// '*location' comment sets resource path for this service
// *location: rest_server_cpp
class rest_server_cpp: public ngrest::Service
{
public:
    //! Dummy description for the operation
    /*! Some detailed description of the operation */
    // To invoke this operation from browser open: http://localhost:9098/rest_server_cpp/World!
    //
    // '*location' metacomment sets path to operation relative to service operation.
    // Default value is operation name.
    // This will bind "echo" method to resource path: http://host:port/rest_server_cpp/{text}
    // *location: /{text}
    //
    // '*method' metacomment sets HTTP method for the operation. GET is default method.
    // *method: GET
    //
    std::string echo(const std::string& text)
    {
        return "Hi, hey, hello, " + text;
    }
};


#endif // REST_SERVER_CPP_H