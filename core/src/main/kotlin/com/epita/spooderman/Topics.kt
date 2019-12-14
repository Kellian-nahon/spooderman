package com.epita.spooderman

import com.epita.spooderman.types.TopicId

enum class Topics(val topicId: TopicId) {
    CrawlURLCommand("crawl-url"),
    DocumentizeContentCommand("documentize-content"),
    IndexDocumentCommand("index-document"),
    ValidateURLCommand("validate-url"),
    CrawledURLEvent("crawled-url"),
    DocumentizedContentEvent("documentized-content"),
    ValidatedURLEvent("validated-url"),
    FoundURLEvent("found-url")
}