package com.example.loggingmetricsbug

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.filter.Filter
import ch.qos.logback.core.spi.FilterReply

class CustomEventFilter : Filter<ILoggingEvent>() {

    override fun decide(event: ILoggingEvent): FilterReply =
        when {
            event.formattedMessage.contains(FILTER_STRING) -> FilterReply.DENY
            else -> FilterReply.NEUTRAL
        }

    companion object {
        const val FILTER_STRING = "CUSTOM_EVENT_FILTER"
    }
}