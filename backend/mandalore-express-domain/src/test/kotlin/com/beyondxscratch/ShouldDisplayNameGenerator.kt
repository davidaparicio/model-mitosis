package com.beyondxscratch

import org.junit.jupiter.api.DisplayNameGenerator

class ShouldDisplayNameGenerator : DisplayNameGenerator.Standard() {
    override fun generateDisplayNameForClass(testClass: Class<*>): String {
        return testClass.simpleName.replace("Should", " should")
    }
}