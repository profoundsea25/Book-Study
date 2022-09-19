package study.kotlinindepth

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import study.kotlinindepth.chapter4.highLevelProperty

@SpringBootApplication
class KotlinInDepthApplication

fun main(args: Array<String>) {
    val highLevelPropertyTest = highLevelProperty
    runApplication<KotlinInDepthApplication>(*args)
}
