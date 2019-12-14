package com.epita.indexer.core

import com.epita.indexer.Controller.dto.QueryResponse

interface Querying {
    fun getDocuments(query: String): QueryResponse
}