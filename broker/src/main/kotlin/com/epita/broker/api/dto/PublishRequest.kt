package com.epita.broker.api.dto

import com.epita.broker.broker.ClientId
import com.epita.spooderman.types.TopicId
import com.epita.broker.core.PublicationType

data class PublishRequest(val clientId: ClientId, val topicId: TopicId, val message: String, val publishType: PublicationType)
