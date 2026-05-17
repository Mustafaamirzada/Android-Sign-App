package com.mustafa.project001.utils

object PersianTextProcessor {
    // List of common Persian stop words
    private val stopWords = setOf(
        "از", "به", "در", "را", "با", "که", "این", "آن", "برای", "و", "اما",
        "ولی", "اگر", "چون", "تا", "هر", "بود", "است", "شد", "نیز", "هم"
    )

    // Simple Stemming: Removes common suffixes
    private val suffixes = listOf("تر", "ترین", "‌ها", "ها", "می‌", "انه", "یت" ,"ان")

    fun processText(input: String): List<String> {
        return input.trim()
            .split("\\s+".toRegex()) // Tokenization
            .map { it.lowercase() }
            .filter { it !in stopWords } // Stop word removal
            .map { stem(it) } // Stemming
    }

    private fun stem(word: String): String {
        var currentWord = word
        // Simple suffix stripping logic
        suffixes.forEach { suffix ->
            if (currentWord.endsWith(suffix) && currentWord.length > suffix.length) {
                currentWord = currentWord.substring(0, currentWord.length - suffix.length)
            }
            if (currentWord.startsWith("می‌")) {
                currentWord = currentWord.replace("می‌", "")
            }
        }
        return currentWord
    }
}