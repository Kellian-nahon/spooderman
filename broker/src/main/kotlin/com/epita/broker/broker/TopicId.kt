package com.epita.broker.broker

typealias TopicId = String

fun TopicId.containsTopic(other: TopicId): Boolean {
    val split = this.split("/")
    val otherSplit = other.split("/")

    for ((i, value) in split.withIndex()) {
        if (otherSplit[i] != value) {
            return false
        }
    }
    return true
}

