package com.epita.spooderman.types

import com.epita.spooderman.annotation.Pure

typealias TopicId = String

@Pure
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

