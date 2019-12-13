package com.epita.spooderman

import com.epita.spooderman.types.TopicId

enum class Topics(val topicId: TopicId) {
    TO_DOCUMENTIZE_COMMAND("documentize-command"),
    TO_INDEX_COMMAND("index-command"),
    DOCUMENTIZED_EVENT("documentized-event")

}