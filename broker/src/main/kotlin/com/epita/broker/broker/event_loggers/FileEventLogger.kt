package com.epita.broker.broker.event_loggers

import com.epita.broker.broker.Message
import com.epita.spooderman.types.TopicId
import java.io.FileOutputStream

class FileEventLogger(val outputStream: FileOutputStream) : EventLogger {
    override fun logEvent(topicId: TopicId, message: Message) {
        val logMessage = "$topicId|$message\n"
        outputStream.write(logMessage.toByteArray())
    }
}
