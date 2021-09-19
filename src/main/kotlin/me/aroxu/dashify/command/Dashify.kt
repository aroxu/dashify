package me.aroxu.dashify.command

import io.github.monun.kommand.getValue
import io.github.monun.kommand.node.LiteralNode
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
                then("key" to string()) {
                    requires { playerOrNull != null && isOp }
                    executes {
                        val key: String by it
                        if (key.length < 8) {
                            player.sendMessage(text("[ERR] Please enter a password of 8 digits or longer!", NamedTextColor.RED))
                            return@executes
                        }
                        val numberIncluded = key.filter { keyValue -> keyValue.isDigit() }
                        val stringIncluded = key.filter { keyValue -> keyValue.isLetter() }
                        if (numberIncluded.isEmpty()) {
                            player.sendMessage("${ChatColor.RED}[ERR] Passwords must contain at least one number!")
                            return@executes
                        } else if (stringIncluded.isEmpty()) {
                            player.sendMessage("${ChatColor.RED}[ERR] Passwords must contain at least one string!")
                            return@executes
                        }
                        player.sendMessage(text("[INFO] Updating Access Key...", NamedTextColor.GRAY))
                        player.sendMessage(text("Your Access key is: \"${DashifyConfigurator.updateAuthKey(key)}\".").decorate(TextDecoration.BOLD))
                        player.sendMessage(text("DO NOT SHARE THIS KEY.").decorate(TextDecoration.BOLD).decorate(TextDecoration.UNDERLINED))
                        player.sendMessage(text("[SUCCESS] Access Key Updated!", NamedTextColor.AQUA))
                    }
                }
            }
            then("version") {
                executes {
                    player.sendMessage("Dashify v${version} by aroxu")
                }
            }
        }
    }
}