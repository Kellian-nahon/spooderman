package com.epita.bitoduc.api.dto

import com.epita.bitoduc.broker.ClientId
import com.epita.bitoduc.broker.TopicId

data class PublicationMessage(val clientId: ClientId, val topicId: TopicId, val message: String)
