package com.example.loggingmetricsbug

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LoggingMetricsBugApplication

fun main(args: Array<String>) {
	runApplication<LoggingMetricsBugApplication>(*args)
}
