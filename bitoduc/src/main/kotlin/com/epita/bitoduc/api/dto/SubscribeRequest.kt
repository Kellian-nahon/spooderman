package com.epita.bitoduc.api.dto

import com.epita.bitoduc.broker.ClientId
import com.epita.bitoduc.broker.TopicId

class SubscribeRequest(val clientId: ClientId, val topicId: TopicId)
