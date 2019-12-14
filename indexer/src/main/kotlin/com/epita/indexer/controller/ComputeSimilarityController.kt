package com.epita.indexer.controller

import com.epita.indexer.controller.dto.QueryResponse
import com.epita.indexer.core.Querying
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.http.BadRequestResponse


class ComputeSimilarityController(private val querying: Querying,
                                  private val server: Javalin) {
    init {
        setup()
    }

    private fun query(query: String): QueryResponse {
        return querying.getDocuments(query)
    }


    private fun setup() {
        server.get("/query") { ctx ->
            val search = ctx.queryParam("q", null) ?: throw BadRequestResponse()
            val response = query(search)
            ctx.result(jacksonObjectMapper().writeValueAsString(response))
        }

    }

    fun start(port: Int) {
        server.start(port)
    }
}