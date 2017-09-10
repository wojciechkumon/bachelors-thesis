#!/usr/bin/env bash

function runTester { # testType, requestSize, responseSize, numberOfTests, warmUpTests, csvDir
    java -jar transfer-tester.jar testType=${1} requestSize=${2} responseSize=${3} numberOfTests=${4} warmUpTests=${5} csvFilePath=${6}/${1}_${2}_${3}.csv
}

function runTesterWithDefaultRequestResponseSizes { # testType, numberOfTests, warmUpTests, csvDir
    runTester ${1} 16 16 ${2} ${3} ${4} # 16B
    runTester ${1} 1024 1024 ${2} ${3} ${4} # 1KB
    runTester ${1} 8192 8192 ${2} ${3} ${4} # 8KB
    runTester ${1} 262144 262144 ${2} ${3} ${4} # 256KB
    runTester ${1} 1048576 1048576 ${2} ${3} ${4} # 1MB
}

runTesterWithDefaultRequestResponseSizes JNI 1000 100 csv
