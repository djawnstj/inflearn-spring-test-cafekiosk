package sample.cafekiosk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CafekioskApplication

fun main(args: Array<String>) {
    runApplication<CafekioskApplication>(*args)
}
