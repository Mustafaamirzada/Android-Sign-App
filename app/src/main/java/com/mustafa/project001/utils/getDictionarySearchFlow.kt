package com.mustafa.project001.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

// 1. THE SEARCH FLOW
fun getDictionarySearchFlow(
    query: String,
    dictionary: List<String>
): Flow<List<String>> = flow {
    if (query.isEmpty()) {
        emit(emptyList())
        return@flow
    }

    val matches = dictionary
        .map { word ->
            val distance = minEditDistance(query, word)
            val maxLen = maxOf(query.length, word.length)
            val similarity = if (maxLen > 0) (1 - distance.toFloat() / maxLen) else 0f
            word to similarity
        }
        .filter { it.second >= 0.4f }
        .sortedByDescending { it.second }
        .map { it.first }

    emit(matches)
}.flowOn(Dispatchers.Default)

// 2. THE HELPER FUNCTION (Move here if not resolving from other files)
fun minEditDistance(word1: String, word2: String): Int {
    val m = word1.length
    val n = word2.length
    val dp = Array(m + 1) { IntArray(n + 1) }

    for (i in 0..m) dp[i][0] = i
    for (j in 0..n) dp[0][j] = j

    for (i in 1..m) {
        for (j in 1..n) {
            val cost = if (word1[i - 1] == word2[j - 1]) 0 else 1
            dp[i][j] = minOf(
                dp[i - 1][j] + 1,
                dp[i][j - 1] + 1,
                dp[i - 1][j - 1] + cost
            )
        }
    }
    return dp[m][n]
}
