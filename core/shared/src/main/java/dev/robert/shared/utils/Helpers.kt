package dev.robert.shared.utils
import androidx.compose.ui.graphics.Color
import dev.robert.shared.R
import org.threeten.bp.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import org.threeten.bp.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


fun getPlatformIcon(platform: String): Int {
    return when {
        platform.ignoreCaseEquals("PC") -> R.drawable.pc
        platform.ignoreCaseEquals("PlayStation") -> R.drawable.ps
        platform.ignoreCaseEquals("Xbox") -> R.drawable.xbox
        platform.ignoreCaseEquals("iOS") -> R.drawable.ios
        platform.ignoreCaseEquals("macOS") -> R.drawable.ios
        platform.ignoreCaseEquals("Linux") -> R.drawable.linux
        platform.ignoreCaseEquals("Nintendo") -> R.drawable.nintendo
        platform.ignoreCaseEquals("Android") -> R.drawable.android
        platform.ignoreCaseEquals("Web") -> R.drawable.web
        platform.ignoreCaseEquals("Apple Macintosh") -> R.drawable.ios
        platform.ignoreCaseEquals("Commodore / Amiga") -> R.drawable.commodore
        platform.ignoreCaseEquals("Atari") -> R.drawable.atari
        platform.ignoreCaseEquals("SEGA") -> R.drawable.sonic
        platform.ignoreCaseEquals("MSX") -> R.drawable.console
        platform.ignoreCaseEquals("TurboGrafx-16") -> R.drawable.console
        platform.ignoreCaseEquals("Virtual") -> R.drawable.virtual
        else -> {
            R.drawable.sonic
        }
    }
}

fun String.ignoreCaseEquals(other: String): Boolean {
    return this.equals(other, ignoreCase = true)
}

fun String.convertDateTo(format: String): String {
    return try {
        val parser = SimpleDateFormat(ConverterDate.FULL_DATE, Locale.getDefault())
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        val date = parser.parse(this)
        formatter.format(date!!)
    } catch (e: ParseException) {
        e.printStackTrace()
        ""
    }
}

object ConverterDate {
    const val FULL_DATE = "yyyy-MM-dd"
}

fun getDateRange(range: Range, isPast: Boolean): String {
    val format = DateTimeFormatter.ofPattern(ConverterDate.FULL_DATE)
    val today = LocalDate.now()
    return if (isPast) {
        val prev = when (range) {
            Range.YEAR -> today.minusYears(1)
            Range.MONTH -> today.minusMonths(6)
            Range.DATE -> today.minusDays(1)
        }
        "${prev.format(format)},${today.format(format)}"
    } else {
        val future = when (range) {
            Range.YEAR -> today.plusYears(1)
            Range.MONTH -> today.plusMonths(6)
            Range.DATE -> today.plusDays(1)
        }
        "${today.format(format)},${future.format(format)}"
    }
}

fun getLastMonthDate(): Date {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.MONTH, -1)
    return calendar.time
}

enum class Range {
    YEAR, MONTH, DATE
}

fun stringToColor(colorString: String?): Color {
    if (colorString == null) return Color.Transparent
    val color = colorString.uppercase().replace("[^0-9A-F]", "")
    if (color.length != 6) {
        error("Invalid color string. Must be 6 hex characters long.")
    }
    val red = color.substring(0, 2).toInt(16)
    val green = color.substring(2, 4).toInt(16)
    val blue = color.substring(4, 6).toInt(16)
    return Color(red, green, blue)
}