package com.epita.broker.broker.event_loggers

import com.epita.broker.broker.Message
import com.epita.spooderman.types.TopicId

interface EventLogger {
    fun logEvent(topicId: TopicId, message: Message)
}