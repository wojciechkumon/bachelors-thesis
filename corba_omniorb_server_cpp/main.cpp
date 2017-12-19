#include <iostream>
#include <omniORB4/CORBA.h>
#include "CorbaInterface.hh"

using namespace std;

class CorbaConnectorImpl : public POA_pl::kumon::transfertester::tester::corba::CorbaConnector,
                           public PortableServer::RefCountServantBase {
public:
    inline CorbaConnectorImpl() {}

    virtual ~CorbaConnectorImpl() {}

    virtual char *get(CORBA::Long responseSize, const char *request);
};

char *CorbaConnectorImpl::get(CORBA::Long responseSize, const char *request) {
    char *response = new char[responseSize + 1];
    memset(response, 'A', responseSize * sizeof(*response));
    response[responseSize] = '\0';
    return CORBA::string_dup(response);
}

int main(int argc, char **argv) {
    CORBA::ORB_ptr orb = CORBA::ORB_init(argc, argv, "omniORB4");

    CORBA::Object_var obj = orb->resolve_initial_references("RootPOA");
    PortableServer::POA_var poa = PortableServer::POA::_narrow(obj);

    CorbaConnectorImpl *connector = new CorbaConnectorImpl();
    poa->activate_object(connector);

    CORBA::String_var ior(orb->object_to_string(connector->_this()));
    cout << ior << endl;

    PortableServer::POAManager_var pman = poa->the_POAManager();
    pman->activate();

    orb->run();
    orb->destroy();
    return 0;
}
