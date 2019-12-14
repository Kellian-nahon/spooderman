package com.epita.urlvalidator

import com.epita.broker.api.client.BrokerConsumer
import com.epita.broker.api.client.BrokerProducer
import com.epita.broker.core.PublicationType
import com.epita.reussaure.bean.LogBean
import com.epita.spooderman.Topics
import com.epita.spooderman.annotation.Mutate
import com.epita.spooderman.commands.ValidateURLCommand
import com.epita.spooderman.events.ValidatedURLEvent
import com.epita.urlvalidator.core.UrlValidator
import java.net.URL

class DefaultUrlValidator(private val consumer: BrokerConsumer,
                          private val producer: BrokerProducer) : UrlValidator, LogBean {
    override val validatedURLs: MutableSet<URL> = mutableSetOf()

    init {
        setup()
    }

    @Synchronized
    @Mutate
    override fun validateUrl(url: URL): Boolean {
        return super.validateUrl(url)
    }

    private fun onValidateURLCommand(command: ValidateURLCommand) {
        val isValid = validateUrl(command.url)

        if (isValid) {
            producer.sendMessage(
                Topics.ValidatedURLEvent.topicId,
                ValidatedURLEvent(command.url),
                PublicationType.ONCE
            ) { response, error ->
                error?.let {
                    logger().warn(it.toString())
                }
            }
        }
    }

    private fun setup() {
        consumer.setHandler(
            Topics.ValidateURLCommand.topicId,
            ValidateURLCommand::class.java
        ) { onValidateURLCommand(it) }
    }

    fun start(serverPort: Int) {
        consumer.start(serverPort)
    }
}
