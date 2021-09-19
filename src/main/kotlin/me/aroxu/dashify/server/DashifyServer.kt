package me.aroxu.dashify.server

import com.typesafe.config.ConfigFactory
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.aroxu.dashify.server.route.routeConfig

private var isServerRunning: Boolean = false
private val server = embeddedServer(Netty, environment = applicationEngineEnvironment {
    config = HoconApplicationConfig(ConfigFactory.load())

    module {
        routeConfig()
    }

    classLoader = (me.aroxu.dashify.DashifyPlugin)::class.java.classLoader

    connector {
        port = 1972
        host = "0.0.0.0"
    }
})

fun start() {
    if (isServerRunning) return
    Thread {
        server.start(wait = true)
    }.start()
    isServerRunning = true
}

fun stop() {
    if (!isServerRunning) return
    server.stop(20, 20)
    isServerRunning = false
}

fun restart() {
    stop()
    start()
}

fun checkIsServerRunning(): Boolean {
    return isServerRunning
}
