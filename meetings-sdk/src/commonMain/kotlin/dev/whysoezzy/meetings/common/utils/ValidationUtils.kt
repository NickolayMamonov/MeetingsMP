package dev.whysoezzy.meetings.common.utils

object ValidationUtils {
    private val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$")

    private const val PHONE_DIGITS_WITH_COUNTRY = 11
    private const val PHONE_DIGITS_WITHOUT_COUNTRY = 10
    private const val OTP_CODE_LENGTH = 4

    fun isValidPhoneNumber(phone: String): Boolean {
        val digits = phone.filter { it.isDigit() }
        return (digits.length == PHONE_DIGITS_WITH_COUNTRY && (digits.startsWith("7") || digits.startsWith("8"))) ||
            (digits.length == PHONE_DIGITS_WITHOUT_COUNTRY)
    }

    fun isValidOtpCode(code: String): Boolean =
        code.length == OTP_CODE_LENGTH && code.all { it.isDigit() }

    @Deprecated("Use isValidOtpCode instead", ReplaceWith("isValidOtpCode(code)"))
    fun isValidSmsCode(code: String): Boolean = isValidOtpCode(code)

    fun isValidName(name: String): Boolean =
        name.isNotBlank() && name.length >= 2 && name.all { it.isLetter() || it == '-' || it == ' ' }

    fun isValidSurname(surname: String): Boolean = isValidName(surname)

    fun isValidEmail(email: String): Boolean = EMAIL_REGEX.matches(email)
}
