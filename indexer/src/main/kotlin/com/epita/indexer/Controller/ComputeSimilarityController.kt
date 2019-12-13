package com.epita.indexer.controller

import com.epita.indexer.Controller.dto.QueryResponse
import com.epita.indexer.core.Querying
import com.epita.indexer.core.RetroIndex
import com.epita.indexer.core.SimilarityComputer
import com.epita.indexer.tokenisation.Tokenizer
import com.epita.indexer.vectorisation.Vectorizer
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.http.BadRequestResponse
import java.lang.Exception


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