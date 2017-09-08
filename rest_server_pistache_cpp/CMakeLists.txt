cmake_minimum_required(VERSION 3.8)
project(rest_server_pistache_cpp)


set(CMAKE_CXX_STANDARD 11)

find_package (Threads)

set(SOURCE_FILES main.cpp)
add_executable(rest_server_pistache_cpp ${SOURCE_FILES})
target_link_libraries(rest_server_pistache_cpp pistache ${CMAKE_THREAD_LIBS_INIT})
