package com.epita.indexer.core

import com.epita.indexer.controller.dto.QueryResponse

interface Querying {
    fun getDocuments(query: String): QueryResponse
}