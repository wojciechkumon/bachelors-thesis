#!/usr/bin/env bash

function runChartGenerator { # csvSourceDirectory, chartOutputDirectory
    java -jar transfer-tester.jar chart csvSourceDirectory=${1} chartOutputDirectory=${2}
}

runChartGenerator csv charts
