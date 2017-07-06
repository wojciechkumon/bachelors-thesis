cmake_minimum_required(VERSION 3.7)
project(tcp_server_cpp)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES main.cpp)
add_executable(tcp_server_cpp ${SOURCE_FILES})