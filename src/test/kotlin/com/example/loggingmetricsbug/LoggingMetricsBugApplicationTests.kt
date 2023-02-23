package com.example.loggingmetricsbug

import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.simple.SimpleMeterRegistry
import mu.KotlinLogging
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.system.CapturedOutput
import org.springframework.boot.test.system.OutputCaptureExtension
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import

private val log = KotlinLogging.logger {}

@SpringBootTest
@Import(MeterRegistryTestConfiguration::class)
@ExtendWith(OutputCaptureExtension::class)
class LoggingMetricsBugApplicationTests(
	@Autowired private val meterRegistry: MeterRegistry
) {
	private val errorCounter = meterRegistry.counter("logback.events", "level", "error")

	@Test
	fun `should not count filtered logs`(output: CapturedOutput) {
		assertThat(errorCounter.count().toInt())
			.isEqualTo(0)

		val errorLog = "should count this log message"
		val errorLogFiltered =
			"should not count this log message, because is filtered: ${CustomEventFilter.FILTER_STRING}"

		log.error { errorLog }
		log.error { errorLogFiltered }

		assertAll(
			{ assertThat(errorCounter.count().toInt()).isEqualTo(1) },
			{ assertThat(output).contains(errorLog).doesNotContain(errorLogFiltered) }
		)
	}
}

@TestConfiguration
class MeterRegistryTestConfiguration {
	@Bean
	fun meterRegistry(): MeterRegistry = SimpleMeterRegistry()
}
