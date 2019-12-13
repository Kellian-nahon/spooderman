package com.epita.spooderman.events

import com.epita.spooderman.types.DocumentContent
import java.net.URL

data class CrawledURLEvent(val url: URL, val content: DocumentContent)
