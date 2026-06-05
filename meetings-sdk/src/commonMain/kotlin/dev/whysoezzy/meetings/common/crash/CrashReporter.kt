package dev.whysoezzy.meetings.common.crash

interface CrashReporter {
    fun log(message: String)

    fun recordException(throwable: Throwable)

    fun setCustomKey(key: String, value: String)

    fun setUserId(id: String?)
}

class NoOpCrashReporter : CrashReporter {
    override fun log(message: String) = Unit

    override fun recordException(throwable: Throwable) = Unit

    override fun setCustomKey(key: String, value: String) = Unit

    override fun setUserId(id: String?) = Unit
}
