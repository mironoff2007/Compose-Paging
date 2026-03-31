package ru.mironov.paging

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform