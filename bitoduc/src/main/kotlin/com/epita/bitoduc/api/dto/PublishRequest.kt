package com.epita.bitoduc.api.dto

import com.epita.bitoduc.broker.ClientId
import com.epita.bitoduc.broker.Message
import com.epita.bitoduc.broker.TopicId
import com.epita.bitoduc.core.PublicationType

data class PublishRequest(val clientId: ClientId, val topicId: TopicId, val message: Message, val publishType: PublicationType)
