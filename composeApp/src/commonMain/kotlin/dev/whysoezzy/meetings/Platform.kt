package dev.whysoezzy.meetings

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform