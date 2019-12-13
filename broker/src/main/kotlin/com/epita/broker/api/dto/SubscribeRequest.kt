package com.epita.broker.api.dto

import com.epita.broker.broker.ClientId
import com.epita.broker.broker.TopicId
import java.net.URL

data class SubscribeRequest(val clientId: ClientId, val topicId: TopicId, val callbackURL: URL)
