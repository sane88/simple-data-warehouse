package com.github.sane88.sdw

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@EnableAutoConfiguration
@ComponentScan
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}