LIBS = -lomnithread -lomniORB4
STUBS = echoSK.cc
STUB_HEADERS = echo.hh

all: main

$(STUBS): ./echo.idl
	omniidl -bcxx ./echo.idl

main: main.cpp $(STUBS)
	g++ -o main main.cpp $(STUBS) $(LIBS)

clean:
	rm -f main $(STUBS) $(STUB_HEADERS)