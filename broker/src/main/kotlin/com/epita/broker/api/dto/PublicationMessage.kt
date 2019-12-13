package com.epita.broker.api.dto

import com.epita.broker.broker.ClientId
import com.epita.spooderman.types.TopicId

data class PublicationMessage(val clientId: ClientId, val topicId: TopicId, val message: String)
