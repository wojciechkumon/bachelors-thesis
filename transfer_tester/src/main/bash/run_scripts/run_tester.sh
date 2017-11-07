#!/usr/bin/env bash

function runTester { # testType, requestSize, responseSize, numberOfTests, warmUpTests, delayBetweenTestsMillis, csvDir, corbaIor
    java --add-modules java.corba -jar transfer-tester.jar test testType=${1} requestSize=${2} responseSize=${3} numberOfTests=${4} warmUpTests=${5} delayBetweenTestsMillis=${6} csvFilePath=${7}/${1}_${2}_${3}.csv corbaIor=${8}
}

function runTesterWithDefaultRequestResponseSizes { # testType, numberOfTests, warmUpTests, delayBetweenTestsMillis, csvDir, corbaIor
    runTester ${1} 16 16 ${2} ${3} ${4} ${5} ${6} # 16B
    runTester ${1} 1024 1024 ${2} ${3} ${4} ${5} ${6} # 1KB
    runTester ${1} 8192 8192 ${2} ${3} ${4} ${5} ${6} # 8KB
    runTester ${1} 262144 262144 ${2} ${3} ${4} ${5} ${6} # 256KB
    runTester ${1} 1048576 1048576 ${2} ${3} ${4} ${5} ${6} # 1MB
}

runTesterWithDefaultRequestResponseSizes JNI 1000 100 0 csv
runTesterWithDefaultRequestResponseSizes TCP 1000 100 40 csv
runTesterWithDefaultRequestResponseSizes CORBA 1000 100 0 csv IOR:123etc
