package com.biteSized.bitesizedv4

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class Bitesizedv4Application

fun main(args: Array<String>) {
	runApplication<Bitesizedv4Application>(*args)
}
