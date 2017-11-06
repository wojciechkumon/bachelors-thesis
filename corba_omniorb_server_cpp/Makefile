LIBS = -lomnithread -lomniORB4
STUBS = echoSK.cc
STUB_HEADERS = echo.hh

all: main

$(STUBS): ./echo.idl
	omniidl -bcxx ./echo.idl

main: main.cpp $(STUBS)
	g++ -o main $(LIBS) main.cpp $(STUBS)

clean:
	rm -f client server $(STUBS) $(STUB_HEADERS)