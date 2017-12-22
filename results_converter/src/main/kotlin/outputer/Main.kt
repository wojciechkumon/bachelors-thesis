package outputer

import java.math.RoundingMode
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.regex.Matcher
import java.util.regex.Pattern


private val recordPattern = Pattern.compile("(\\w+) requestSize=(\\d+) responseSize=(\\d+) medianNanos=(\\d+) meanNanos=(\\d+) standardDeviationNanos=(\\d+)")

fun main(args: Array<String>) {
    val uri: URI = Record::class.java.classLoader.getResource("data.txt").toURI()
    Sequence { Files.lines(Paths.get(uri)).iterator() }
            .map(recordPattern::matcher)
            .filter(Matcher::matches)
            .map { matcher ->
                Record(method = matcher.group(1),
                        requestSize = matcher.group(2).toLong(),
                        responseSize = matcher.group(3).toLong(),
                        medianNanos = matcher.group(4).toLong(),
                        meanNanos = matcher.group(5).toLong(),
                        standardDeviationNanos = matcher.group(6).toLong())
            }
            .filter { record -> record.method != "MOCK" }
            .groupBy { RequestResponse(it.requestSize, it.responseSize) }
            .toSortedMap(Comparator { left, right ->
                val diff: Int = left.requestSize.compareTo(right.requestSize)
                if (diff != 0) diff
                else left.responseSize.compareTo(right.responseSize)
            })
            .entries
            .map { it.value }
            .map { "    " + toUnit(it[0].requestSize) + " & " + toUnit(it[0].responseSize) + " & " + getNumbers(it) + " \\\\" }
            .forEach(::println)

//        16B & 16B & 1224 (150\%) & 2579 (150\%) & 6.5 (100\%)  & 1496 (650\%) & 339 (111150\%) \\
}

private fun getNumbers(records: List<Record>): String {
    val jniMean: Long = records.first { it.method == "JNI" }.meanNanos
    return get(records, "CORBA", jniMean) +
            " & " + get(records, "FILE", jniMean) +
            " & " + get(records, "JNI", jniMean) +
            " & " + get(records, "REST", jniMean) +
            " & " + get(records, "TCP", jniMean)
}

private fun get(records: List<Record>, method: String, meanToCompare: Long): String =
        records.firstOrNull { it.method == method }
                ?.let { round(it.meanNanos) + " (" + round(it.meanNanos.toDouble() / meanToCompare) + "x)" }
                ?: "-"

private fun round(value: Long): String {
    val format = if (value < 1_000_000) DecimalFormat("#.#") else DecimalFormat("#")
    format.roundingMode = RoundingMode.HALF_UP
    return format.format(value.toDouble() / 1000.0)
}

private fun round(value: Double): String {
    val format = DecimalFormat("#.#")
    format.roundingMode = RoundingMode.HALF_UP
    return format.format(value)
}

private fun toUnit(value: Long): String =
        when (value) {
            16L -> "16B"
            1024L -> "1KiB"
            8192L -> "8KiB"
            262144L -> "256KiB"
            1048576L -> "1MiB"
            134217728L -> "128MiB"
            else -> value.toString()
        }


private data class Record(val method: String,
                          val requestSize: Long,
                          val responseSize: Long,
                          val medianNanos: Long,
                          val meanNanos: Long,
                          val standardDeviationNanos: Long)

private data class RequestResponse(val requestSize: Long,
                                   val responseSize: Long)
