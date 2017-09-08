cmake_minimum_required(VERSION 3.8)
project(file_watcher_cpp)

set(CMAKE_CXX_STANDARD 11)

set(SOURCE_FILES main.cpp)
add_executable(file_watcher_cpp ${SOURCE_FILES})