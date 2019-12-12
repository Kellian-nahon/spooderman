package com.epita.bitoduc.broker

import java.net.URL

data class BrokerClient(val id: ClientID, val url: URL, val topics: MutableSet<TopicID>)