package me.aroxu.dashify.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
import me.aroxu.dashify.DashifyPlugin.Companion.plugin
import me.aroxu.dashify.DashifyPlugin.Companion.version
import me.aroxu.dashify.config.DashifyConfigurator
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.ChatColor

object Dashify {
    fun register(builder: LiteralNode) {
        builder.apply {
            then("key") {
                requires { isConsole }
                then("key" to string()) {
                    executes {
                        val key: String by it
                        if (key.length < 8) {
                            plugin.server.consoleSender.sendMessage(text("Please enter a password of 8 digits or longer!", NamedTextColor.RED))
                            return@executes
                        }
                        val numberIncluded = key.filter { keyValue -> keyValue.isDigit() }
                        val stringIncluded = key.filter { keyValue -> keyValue.isLetter() }
                        if (numberIncluded.isEmpty()) {
                            plugin.server.consoleSender.sendMessage("${ChatColor.RED}Passwords must contain at least one number!")
                            return@executes
                        } else if (stringIncluded.isEmpty()) {
                            plugin.server.consoleSender.sendMessage("${ChatColor.RED}Passwords must contain at least one string!")
                            return@executes
                        }
                        plugin.server.consoleSender.sendMessage(text("Updating Access Key...", NamedTextColor.GRAY))
                        plugin.server.consoleSender.sendMessage(text("Your Access key is: \"${DashifyConfigurator.updateAuthKey(key)}\".").decorate(TextDecoration.BOLD))
                        plugin.server.consoleSender.sendMessage(text("DO NOT SHARE THIS KEY.").decorate(TextDecoration.BOLD).decorate(TextDecoration.UNDERLINED))
                        plugin.server.consoleSender.sendMessage(text("Access Key Updated!", NamedTextColor.AQUA))
                    }
                }
            }
            then("version") {
                executes {
                    if (isPlayer) {
                        player.sendMessage("Dashify v${version} by aroxu")
                    } else {
                        plugin.server.consoleSender.sendMessage("Dashify v${version} by aroxu")
                    }
                }
            }
        }
    }
}