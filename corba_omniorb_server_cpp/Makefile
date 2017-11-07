LIBS = -lomnithread -lomniORB4
STUBS = CorbaInterfaceSK.cc
STUB_HEADERS = CorbaInterface.hh

all: main

$(STUBS): CorbaInterface.idl
	omniidl -bcxx CorbaInterface.idl

main: main.cpp $(STUBS)
	g++ -o main main.cpp $(STUBS) $(LIBS)

clean:
	rm -f main $(STUBS) $(STUB_HEADERS)