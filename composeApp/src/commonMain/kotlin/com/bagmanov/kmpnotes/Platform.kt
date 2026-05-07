package com.bagmanov.kmpnotes

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform