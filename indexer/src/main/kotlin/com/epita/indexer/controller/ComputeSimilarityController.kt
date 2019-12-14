package com.epita.indexer.controller

import com.epita.indexer.controller.dto.QueryResponse
import com.epita.indexer.core.Querying
import com.epita.reussaure.bean.LogBean
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.javalin.Javalin
import io.javalin.http.BadRequestResponse
import kotlin.math.log


class ComputeSimilarityController(private val querying: Querying,
                                  private val server: Javalin): LogBean {
    init {
        setup()
    }

    private fun query(query: String): QueryResponse {
        logger().info("handle query: %s", query)
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