package me.iuur.majestic.prizonplogin

import com.google.common.collect.ImmutableList
import com.google.common.io.ByteArrayDataOutput
import com.google.common.io.ByteStreams
import com.google.inject.Inject
import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.command.SimpleCommand
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.PostLoginEvent
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import com.velocitypowered.api.event.player.ServerConnectedEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.slf4j.Logger
import org.spongepowered.configurate.ConfigurationNode
import org.spongepowered.configurate.yaml.YamlConfigurationLoader
import java.io.File
import java.io.IOException
import java.net.InetAddress
import java.nio.file.Path
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@Plugin(id = "prizonplogin", name = "prizonplogin", version = "1.0", authors = ["MrAngelz"])
class JailPlugin @Inject constructor(
    private val server: ProxyServer,
    private val logger: Logger,
    @DataDirectory private val dataDirectory: Path
) {

    // Configuration
    private lateinit var config: ConfigurationNode
    private val offenseDurations = mutableMapOf<String, List<Long>>()
    private val blacklistReasons = mutableSetOf<String>()

    // Player data
    private val playerDataMap = ConcurrentHashMap<UUID, PlayerData>()
    private val playerNameMap = ConcurrentHashMap<String, UUID>()

    // Plugin messaging channel
    companion object {
        private val GUI_CHANNEL = MinecraftChannelIdentifier.create("prizonplogin", "gui")
        private const val MAX_JAIL_DURATION_SECONDS = 1576800000L // ~50 years
        private const val IP_BAN_DURATION_SECONDS = 1576800000L // ~50 years
    }

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        // Load configuration
        loadConfig()

        // Register commands
        registerCommands()

        // Register plugin messaging channel
        server.channelRegistrar.register(GUI_CHANNEL)

        // Load player data
        loadPlayerData()

        // Schedule unjail tasks
        scheduleUnjailTasks()

        logger.info("PrizonLogin has been enabled!")
    }

    private fun loadConfig() {
        // Ensure data directory exists
        if (!dataDirectory.toFile().exists()) {
            dataDirectory.toFile().mkdirs()
        }

        val configFile = dataDirectory.resolve("config.yml").toFile()
        val loader = YamlConfigurationLoader.builder()
            .file(configFile)
            .build()

        try {
            if (!configFile.exists()) {
                // Create default config
                config = loader.createNode()

                // Set default values
                config.node("options", "jail-on-login").set(true)
                config.node("options", "timeformat").set("dd.MM.yyyy HH:mm")
                config.node("options", "uuid").set(true)

                config.node("jail-server").set("jail")
                config.node("lobby-server").set("lobby")

                config.node("commands", "filter").set("whitelist")
                config.node("commands", "filtered-list").set(ImmutableList.of("help", "discord"))

                // Default messages
                config.node("messages", "permission-message").set("&cYou do not have permission to use this command.")
                config.node("messages", "not-enough-arguments").set("&cNot enough arguments.")
                config.node("messages", "too-many-arguments").set("&cToo many arguments.")
                config.node("messages", "wrong-time-format").set("&cWrong time format. Use: <number><s|m|h|d|w|M|y>")
                config.node("messages", "jail-server-not-set").set("&cJail server has not been set! Use /prizonset.")
                config.node("messages", "jail-success").set("&aPlayer %player% has been jailed until %until%. Reason: %reason%.")
                config.node("messages", "jail-fail").set("&cFailed to jail %player%. Player already jailed or not found.")
                config.node("messages", "unjail-success").set("&aPlayer %player% has been unjailed.")
                config.node("messages", "unjail-fail").set("&cPlayer %player% is not jailed or could not be found.")
                config.node("messages", "checkjail-is-jailed").set("&e%player% is jailed until %until%. Reason: %reason%.")
                config.node("messages", "checkjail-not-jailed").set("&a%player% is not jailed.")
                config.node("messages", "setjailserver-success").set("&aJail server has been set to %server%!")
                config.node("messages", "delete-success").set("&aJail server has been removed. No jail server is currently set.")
                config.node("messages", "delete-fail").set("&cNo jail server is currently set.")
                config.node("messages", "jailip-success").set("&aIP %ip% has been jailed. All players with this IP will be jailed.")
                config.node("messages", "unjailip-success").set("&aIP %ip% has been unjailed.")
                config.node("messages", "unjailip-fail").set("&cIP %ip% is not jailed.")
                config.node("messages", "offense-add-success").set("&aAdded offense to %player%: %reason% for %duration%.")
                config.node("messages", "offense-remove-success").set("&aRemoved offense from %player%.")
                config.node("messages", "offense-list-header").set("&eOffenses for %player%:")
                config.node("messages", "offense-list-empty").set("&a%player% has no offenses.")
                config.node("messages", "console-error").set("&cYou can't use this command from console.")
                config.node("messages", "you-have-been-jailed").set("&4You are currently jailed and must only play on the hacker jail")
                config.node("messages", "you-have-been-jailed-details").set("&fYou have been &4jailed for &e%reason% &fwhich will expire in &e%until%.")
                config.node("messages", "you-have-been-unjailed").set("&aYou have been unjailed! Welcome back.")
                config.node("messages", "still-jailed").set("&4You are currently jailed and must only play on the hacker jail")
                config.node("messages", "still-jailed-details").set("&fYou have been &4jailed for &e%reason% &fwhich will expire in &e%until%.")
                config.node("messages", "cant-use-this-command").set("&cYou can't use this command while jailed!")
                config.node("messages", "cant-chat-while-jailed").set("&cYou are currently jailed and cannot chat!")
                config.node("messages", "reload").set("&aPrizonLogin config reloaded.")
                config.node("messages", "indefinitely").set("indefinitely")
                config.node("messages", "ip-ban-evade-message").set("&cYou are jailed due to an active IP ban on this network.")

                // Offense system configuration
                val offenseSystem = config.node("offense-system")
                offenseSystem.node("blacklist-reasons").set(listOf(
                    "10+ Alts", "IP Ban", "Perm Ban", "Manual Raid Ban",
                    "Webstore Fraud / Scamming / Chargeback"
                ))

                val offenses = offenseSystem.node("offenses")
                offenses.node("5 Minute Ban").set(listOf("5m", "5m", "5m", "5m"))
                offenses.node("Exploits / Glitches").set(listOf("7d", "30d", "30d", "90d"))
                offenses.node("AFK Grinding / AFK Autoclicker").set(listOf("12h", "1d", "3d", "7d"))
                offenses.node("AFK McMMO Grinding / Non-PVP Autoclicker / Botting").set(listOf("3d", "7d", "14d", "30d"))
                offenses.node("Auto Ban - Hate/Disc").set(listOf("Kick", "12h", "1d", "7d"))
                offenses.node("Being a Troll").set(listOf("1m", "1m", "1m", "1m"))
                offenses.node("Block Glitching / Avoiding Claims").set(listOf("Warn", "Kick", "1h", "6h"))
                offenses.node("Causing Drama").set(listOf("7d", "14d", "30d", "60d"))
                offenses.node("Chat Infraction").set(listOf("Warn", "Warn", "Kick", "1h"))
                offenses.node("Chat/Command Spam").set(listOf("3h", "6h", "1d", "1d"))
                offenses.node("Community Harassment / Disrespect").set(listOf("Warn", "Warn", "Kick", "1d"))
                offenses.node("Criticals / Anti-Knockback").set(listOf("14d", "30d", "45d", "60d"))
                offenses.node("DDoS Threat / Crash Attempt (Deliberate)").set(listOf("14d", "30d", "60d", "180d"))
                offenses.node("Derp / Crouch / Skin Blinker").set(listOf("3d", "7d", "14d", "30d"))
                offenses.node("Excessive Advertising / Inappropriate Links").set(listOf("180d", "1y", "50y", "50y"))
                offenses.node("Excessive Swearing / Inap. Behaviour").set(listOf("6h", "1d", "3d", "3d"))
                offenses.node("Exploiting / X-Ray Bases").set(listOf("7d", "14d", "30d", "45d"))
                offenses.node("Fancy Chat / Mute Evasion").set(listOf("6h", "6h", "1d", "1d"))
                offenses.node("Fast Break/Place / Nuker / Auto-Tool / Macros").set(listOf("3d", "7d", "14d", "30d"))
                offenses.node("Fly / Speed / No-Fall / Jesus").set(listOf("3d", "7d", "14d", "30d"))
                offenses.node("Griefing / Spawn Pushing / Scamming").set(listOf("1d", "3d", "7d", "14d"))
                offenses.node("Hate / Discrimination").set(listOf("1d", "7d", "14d", "30d"))
                offenses.node("Inappropriate Name").set(listOf("1d", "7d", "7d", "14d"))
                offenses.node("Inappropriate Skin").set(listOf("1d", "7d", "7d", "14d"))
                offenses.node("KillAura / Combat Fly / PVP Autoclicker").set(listOf("7d", "14d", "30d", "60d"))
                offenses.node("Shared Account Services / Clients").set(listOf("14d", "30d", "60d", "90d"))
                offenses.node("Lag Machine (Accidental)").set(listOf("1d", "3d", "7d", "14d"))
                offenses.node("Manual Ban (30 Days)").set(listOf("30d", "30d", "30d", "30d"))
                offenses.node("Manual Ban (14 Days)").set(listOf("14d", "14d", "14d", "14d"))
                offenses.node("Manual Ban (2 Months)").set(listOf("60d", "60d", "60d", "60d"))
                offenses.node("Manual Raid Ban").set(listOf("50y", "50y", "50y", "50y"))
                offenses.node("Minor Offences").set(listOf("1h", "1h", "1h", "1h"))
                offenses.node("Perm Ban").set(listOf("50y", "50y", "50y", "50y"))
                offenses.node("Phase / Freecam / Glitching / Minor Duping").set(listOf("30d", "45d", "60d", "120d"))
                offenses.node("Releasing Personal Info").set(listOf("1y", "50y", "50y", "50y"))
                offenses.node("Repeat Minor Offenses (2 days)").set(listOf("2d", "2d", "2d", "2d"))
                offenses.node("Scaffold / Printer / Schematica").set(listOf("3d", "7d", "14d", "30d"))
                offenses.node("Sexual Harrassment").set(listOf("1d", "7d", "14d", "30d"))
                offenses.node("Staff Disrespect / Impersonation").set(listOf("1d", "7d", "14d", "30d"))
                offenses.node("Teaming / Breaking Minigame Rules").set(listOf("1d", "3d", "7d", "7d"))
                offenses.node("Tracers / Chest ESP / Player ESP").set(listOf("14d", "30d", "45d", "60d"))
                offenses.node("Advertising / Sending links").set(listOf("1d", "3d", "7d", "14d"))
                offenses.node("Webstore Fraud / Scamming / Chargeback").set(listOf("180d", "1y", "50y", "50y"))
                offenses.node("X-Ray Ores/Dungeons").set(listOf("1d", "3d", "7d", "14d"))

                loader.save(config)
            } else {
                config = loader.load()

                // Validate critical configuration values
                validateConfig()
            }

            // Load offense durations and blacklist reasons
            loadOffenseSystem()

        } catch (e: IOException) {
            logger.error("Failed to load config file", e)
        }
    }

    private fun validateConfig() {
        // Validate jail server
        val jailServerName = config.node("jail-server").getString("")
        if (jailServerName.isEmpty()) {
            logger.warn("Jail server is not configured! Players cannot be jailed.")
        } else {
            val jailServer = server.getServer(jailServerName)
            if (!jailServer.isPresent) {
                logger.warn("Jail server '$jailServerName' does not exist! Players cannot be jailed.")
            }
        }

        // Validate max jail duration
        val maxDurationStr = config.node("options", "max-jail-duration").getString("18250d")
        val maxDuration = parseDuration(maxDurationStr)
        if (maxDuration == null) {
            logger.warn("Invalid max jail duration format: '$maxDurationStr'. Using default: 18250d")
            config.node("options", "max-jail-duration").set("18250d")
        }

        // Validate IP ban duration
        val ipBanDurationStr = config.node("options", "ip-ban-duration").getString("18250d")
        val ipBanDuration = parseDuration(ipBanDurationStr)
        if (ipBanDuration == null) {
            logger.warn("Invalid IP ban duration format: '$ipBanDurationStr'. Using default: 18250d")
            config.node("options", "ip-ban-duration").set("18250d")
        }

        // Validate command filter type
        val filterType = config.node("commands", "filter").getString("whitelist")
        if (filterType != "whitelist" && filterType != "blacklist") {
            logger.warn("Invalid command filter type: '$filterType'. Using default: whitelist")
            config.node("commands", "filter").set("whitelist")
        }
    }

    private fun loadOffenseSystem() {
        offenseDurations.clear()
        blacklistReasons.clear()

        // Load blacklist reasons
        val blacklist = config.node("offense-system", "blacklist-reasons")
            .getList(String::class.java) ?: emptyList()
        blacklistReasons.addAll(blacklist)

        // Load offense durations
        val offensesNode = config.node("offense-system", "offenses")
        for ((key, value) in offensesNode.childrenMap()) {
            val offense = key.toString()
            val durations = value.getList(String::class.java)
                ?.mapNotNull { parseDuration(it) }
                ?: emptyList()

            if (durations.isNotEmpty()) {
                offenseDurations[offense] = durations
            }
        }

        logger.info("Loaded ${offenseDurations.size} offenses and ${blacklistReasons.size} blacklist reasons.")
    }

    private fun registerCommands() {
        val commandManager = server.commandManager

        // Register prizonjail command
        commandManager.register(
            commandManager.metaBuilder("prizonjail")
                .aliases("j")
                .build(),
            JailCommand()
        )

        // Register prizonunjail command
        commandManager.register(
            commandManager.metaBuilder("prizonunjail")
                .aliases("u")
                .build(),
            UnjailCommand()
        )

        // Register prizoncheckjail command
        commandManager.register(
            commandManager.metaBuilder("prizoncheckjail")
                .aliases("c")
                .build(),
            CheckjailCommand()
        )

        // Register prizonset command
        commandManager.register(
            commandManager.metaBuilder("prizonset")
                .build(),
            SetjailserverCommand()
        )

        // Register prizondel command
        commandManager.register(
            commandManager.metaBuilder("prizondel")
                .build(),
            DeleteCommand()
        )

        // Register prizonjailip command
        commandManager.register(
            commandManager.metaBuilder("prizonjailip")
                .build(),
            JailIPCommand()
        )

        // Register prizonunjailip command
        commandManager.register(
            commandManager.metaBuilder("prizonunjailip")
                .build(),
            UnjailIPCommand()
        )

        // Register prizonjailreload command
        commandManager.register(
            commandManager.metaBuilder("prizonjailreload")
                .build(),
            PrizonjailreloadCommand()
        )

        // Register prizonoffense command to manage offenses
        commandManager.register(
            commandManager.metaBuilder("prizonoffense")
                .build(),
            OffenseCommand()
        )
    }

    // Event handlers for jailed players
    @Subscribe
    fun onPlayerChooseInitialServer(event: PlayerChooseInitialServerEvent) {
        val player = event.player
        val playerData = playerDataMap[player.uniqueId]

        if (playerData?.activePunishment != null) {
            val jailServerName = config.node("jail-server").getString("")
            if (jailServerName.isNotEmpty()) {
                val jailServer = server.getServer(jailServerName)
                if (jailServer.isPresent) {
                    event.setInitialServer(jailServer.get())
                }
            }
        }
    }

    @Subscribe
    fun onServerConnected(event: ServerConnectedEvent) {
        val player = event.player
        val playerData = playerDataMap[player.uniqueId]

        if (playerData?.activePunishment != null) {
            val jailServerName = config.node("jail-server").getString("")
            val currentServer = event.server.serverInfo.name

            if (currentServer != jailServerName) {
                player.sendMessage(formatMessage(getMessage("messages", "must-stay-in-jail")))

                val jailServer = server.getServer(jailServerName)
                if (jailServer.isPresent) {
                    player.createConnectionRequest(jailServer.get()).connect()
                }
            }
        }
    }

    @Subscribe
    fun onPlayerCommand(event: com.velocitypowered.api.event.command.CommandExecuteEvent) {
        val source = event.commandSource
        if (source !is Player) return

        val player = source
        val playerData = playerDataMap[player.uniqueId]

        if (playerData?.activePunishment != null) {
            val command = event.command.split(" ")[0].lowercase()

            // Special handling for /hub command - redirect to jail
            if (command == "hub" || command == "lobby") {
                val jailServerName = config.node("jail-server").getString("")
                if (jailServerName.isNotEmpty()) {
                    val jailServer = server.getServer(jailServerName)
                    if (jailServer.isPresent) {
                        player.createConnectionRequest(jailServer.get()).connect()
                        player.sendMessage(formatMessage(getMessage("messages", "still-jailed")))
                    }
                }
                event.result = com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult.denied()
                return
            }

            val filterType = config.node("commands", "filter").getString("whitelist")
            val filteredList = config.node("commands", "filtered-list")
                .getList(String::class.java)
                ?.map { it.lowercase() }
                ?: emptyList()

            val isAllowed = if (filterType == "whitelist") {
                filteredList.contains(command)
            } else {
                !filteredList.contains(command)
            }

            if (!isAllowed) {
                player.sendMessage(formatMessage(getMessage("messages", "cant-use-this-command")))
                event.result = com.velocitypowered.api.event.command.CommandExecuteEvent.CommandResult.denied()
            }
        }
    }

    // Chat blocking removed - PlayerChatEvent is deprecated and causes issues with signed chat (1.19.1+)
    // To block chat for jailed players, use your backend server's chat plugin or block chat commands
    // like /tell, /msg, /w, /whisper, /reply, /r in the command filter above

    @Subscribe
    fun onPostLogin(event: PostLoginEvent) {
        val player = event.player
        
        // Check if we have data for this player under a different UUID (offline jail case)
        val existingUuid = playerNameMap[player.username.lowercase()]
        if (existingUuid != null && existingUuid != player.uniqueId && playerDataMap.containsKey(existingUuid)) {
            // Player was jailed while offline with a temporary UUID - migrate data to real UUID
            val tempData = playerDataMap[existingUuid]!!
            playerDataMap.remove(existingUuid)
            playerDataMap[player.uniqueId] = tempData
            playerNameMap[player.username.lowercase()] = player.uniqueId
            logger.info("Migrated offline jail data for ${player.username} from temporary UUID $existingUuid to real UUID ${player.uniqueId}")
            savePlayerData()
        }
        
        val playerData = playerDataMap.computeIfAbsent(player.uniqueId) {
            PlayerData(player.username)
        }

        // Check if player has changed their name
        if (playerData.playerName != player.username) {
            // Remove old name mapping
            playerNameMap.remove(playerData.playerName.lowercase())

            // Update player name
            playerData.playerName = player.username

            // Add new name mapping
            playerNameMap[player.username.lowercase()] = player.uniqueId

            // Save updated data
            savePlayerData()
        }

        // Record player IP
        val playerIp = player.remoteAddress.address.hostAddress
        if (playerIp != null) {
            playerData.lastIp = playerIp
        }

        // Check for IP ban evasion
        if (checkForIpBanEvasion(player)) {
            return // Player was auto-jailed for IP ban evasion
        }

        // Check if player is jailed
        if (playerData.activePunishment != null) {
            val jailServerName = config.node("jail-server").getString("")
            if (jailServerName.isNotEmpty()) {
                val jailServer = server.getServer(jailServerName)
                if (jailServer.isPresent) {
                    // Send jailed messages
                    sendJailedMessages(player, playerData.activePunishment!!)

                    // Move to jail server if not already there
                    if (!player.currentServer.map { it.server == jailServer.get() }.orElse(false)) {
                        player.createConnectionRequest(jailServer.get()).connect()
                    }
                }
            }
        }
    }

    /**
     * Checks if a player is trying to evade a jail sentence based on their IP address.
     * If a matching IP is found with a blacklisted reason, the player is auto-jailed.
     * @return true if the player was auto-jailed for IP evasion, false otherwise.
     */
    private fun checkForIpBanEvasion(player: Player): Boolean {
        val playerIp = player.remoteAddress.address.hostAddress ?: return false
        val playerData = playerDataMap.computeIfAbsent(player.uniqueId) {
            PlayerData(player.username)
        }

        // Normalize IP address for comparison
        val normalizedPlayerIp = normalizeIpAddress(playerIp)

        // Check if this IP is associated with any banned accounts
        for ((uuid, data) in playerDataMap) {
            if (data.lastIp != null && normalizeIpAddress(data.lastIp!!) == normalizedPlayerIp && data.activePunishment != null) {
                val punishment = data.activePunishment!!
                val reason = punishment.reason

                // Check if this reason is in the blacklist
                if (reason != null && blacklistReasons.contains(reason)) {
                    // Auto-jail this player for IP ban evasion
                    val jailDuration = parseDuration(config.node("options", "ip-ban-duration").getString("18250d"))
                        ?: IP_BAN_DURATION_SECONDS

                    val jailEndTimeMillis = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(jailDuration)
                    val newPunishment = PunishmentRecord(
                        "IP Ban Evasion: $reason",
                        System.currentTimeMillis(),
                        jailEndTimeMillis,
                        "System"
                    )

                    playerData.addPunishment(newPunishment)
                    playerData.activePunishment = newPunishment
                    playerData.lastIp = playerIp

                    savePlayerData()

                    // Send message to player
                    player.sendMessage(formatMessage(getMessage("messages", "ip-ban-evade-message")))

                    // Move to jail server
                    val jailServerName = config.node("jail-server").getString("")
                    if (jailServerName.isNotEmpty()) {
                        val jailServer = server.getServer(jailServerName)
                        if (jailServer.isPresent) {
                            player.createConnectionRequest(jailServer.get()).connect()
                        }
                    }

                    return true
                }
            }
        }

        return false
    }

    // Helper method to normalize IP addresses
    private fun normalizeIpAddress(ip: String): String {
        return try {
            val address = InetAddress.getByName(ip)
            address.hostAddress
        } catch (e: Exception) {
            ip
        }
    }

    // Command classes
    inner class JailCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()
            val args = invocation.arguments()

            if (!source.hasPermission("majestic.jail")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            if (args.size < 2) {
                source.sendMessage(formatMessage(getMessage("messages", "not-enough-arguments")))
                source.sendMessage(formatMessage(getMessage("messages", "correct-usage-jail")))
                return
            }

            val targetPlayerName = args[0]
            val targetPlayer = server.getPlayer(targetPlayerName)

            val jailServerName = config.node("jail-server").getString("")
            if (jailServerName.isEmpty()) {
                source.sendMessage(formatMessage(getMessage("messages", "jail-server-not-set")))
                return
            }

            var durationSeconds: Long
            var reason: String? = null

            val durationStringInput = args[1].lowercase()
            val parsedDuration = parseDuration(durationStringInput)

            if (parsedDuration == null) {
                source.sendMessage(formatMessage(getMessage("messages", "wrong-time-format")))
                source.sendMessage(formatMessage(getMessage("messages", "correct-usage-jail")))
                return
            } else {
                durationSeconds = parsedDuration
                if (args.size > 2) {
                    reason = args.sliceArray(2 until args.size).joinToString(" ")
                }
            }

            // Get max jail duration from config
            val maxDurationStr = config.node("options", "max-jail-duration").getString("18250d")
            val maxDuration = parseDuration(maxDurationStr) ?: MAX_JAIL_DURATION_SECONDS

            if (durationSeconds > maxDuration) {
                source.sendMessage(Component.text("Max jail duration is ${formatDurationAlwaysShowAllUnits(maxDuration)}.")
                    .color(NamedTextColor.RED))
                source.sendMessage(Component.text("Your entered duration would be: ${formatDurationAlwaysShowAllUnits(durationSeconds)}.")
                    .color(NamedTextColor.RED))
                return
            }

            // Get or create player UUID and data
            val targetUuid: UUID
            val playerData: PlayerData
            
            if (targetPlayer.isPresent) {
                // Player is online - use their current UUID
                targetUuid = targetPlayer.get().uniqueId
                playerData = playerDataMap.computeIfAbsent(targetUuid) {
                    PlayerData(targetPlayerName)
                }
                // Update player name mapping
                playerNameMap[targetPlayerName.lowercase()] = targetUuid
            } else {
                // Player is offline - check if we have their data
                val existingUuid = playerNameMap[targetPlayerName.lowercase()]
                if (existingUuid != null && playerDataMap.containsKey(existingUuid)) {
                    // We have data for this player
                    targetUuid = existingUuid
                    playerData = playerDataMap[existingUuid]!!
                } else {
                    // New player we haven't seen before - create new data
                    targetUuid = UUID.randomUUID() // Temporary UUID until they log in
                    playerData = PlayerData(targetPlayerName)
                    playerDataMap[targetUuid] = playerData
                    playerNameMap[targetPlayerName.lowercase()] = targetUuid
                    logger.info("Creating new player data for offline player: $targetPlayerName with temporary UUID: $targetUuid")
                }
            }

            // Calculate duration based on offense system
            val calculatedDuration = calculateDuration(playerData, reason, durationSeconds)
            val jailEndTimeMillis = if (calculatedDuration > 0)
                System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(calculatedDuration)
            else 0L // 0 for indefinite

            // Create punishment record
            val punishment = PunishmentRecord(
                reason,
                System.currentTimeMillis(),
                jailEndTimeMillis,
                if (source is Player) source.username else "Console"
            )

            // Add to player's punishment history
            playerData.addPunishment(punishment)

            // Set current active punishment
            playerData.activePunishment = punishment

            // Save player data
            savePlayerData()

            // If player is online, handle them immediately
            if (targetPlayer.isPresent) {
                // Record player IP for IP ban detection
                val playerIp = targetPlayer.get().remoteAddress.address.hostAddress
                if (playerIp != null) {
                    playerData.lastIp = playerIp
                }

                // Send jailed message to player
                sendJailedMessages(targetPlayer.get(), punishment)

                // Move player to jail server if not already there
                val jailServer = server.getServer(jailServerName)
                if (jailServer.isPresent && !targetPlayer.get().currentServer
                        .map { it.server == jailServer.get() }.orElse(false)) {
                    targetPlayer.get().createConnectionRequest(jailServer.get()).connect()
                }
            }

            source.sendMessage(
                formatMessage(getMessage("messages", "jail-success")
                    .replace("%player%", targetPlayerName)
                    .replace("%reason%", reason ?: getMessage("messages", "no-reason-specified"))
                    .replace("%until%", formatTimeUntil(jailEndTimeMillis)))
            )

            server.allPlayers.forEach { p ->
                p.sendMessage(
                    Component.text("$targetPlayerName has been jailed by ${if (source is Player) source.username else "Console"}${if (!targetPlayer.isPresent) " (offline)" else ""}.")
                        .color(NamedTextColor.YELLOW)
                )
            }

            // Schedule unjail task if not indefinite
            if (calculatedDuration > 0) {
                server.scheduler
                    .buildTask(this@JailPlugin, Runnable { unjailPlayer(targetUuid) })
                    .delay(calculatedDuration, TimeUnit.SECONDS)
                    .schedule()
            }
        }

        override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
            val args = invocation.arguments()
            when {
                args.size == 1 -> {
                    // Suggest player names
                    return server.allPlayers
                        .filter { it.username.lowercase().startsWith(args[0].lowercase()) }
                        .map { it.username }
                        .toList()
                }
                args.size == 2 -> {
                    // Suggest time units
                    val input = args[1].lowercase()
                    if (input.isEmpty()) {
                        return listOf("1s", "1m", "1h", "1d", "1w", "1M", "1y")
                    }

                    // Try to parse the number part
                    val pattern = Pattern.compile("(\\d+)")
                    val matcher = pattern.matcher(input)
                    if (matcher.find()) {
                        val number = matcher.group(1)
                        return listOf(
                            "${number}s", "${number}m", "${number}h",
                            "${number}d", "${number}w", "${number}M", "${number}y"
                        )
                    }
                    return emptyList()
                }
                args.size == 3 -> {
                    // Suggest offense reasons
                    val input = args[2].lowercase()
                    return offenseDurations.keys
                        .filter { it.lowercase().startsWith(input) }
                        .toList()
                }
            }
            return emptyList()
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.jail")
        }
    }

    inner class UnjailCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()
            val args = invocation.arguments()

            if (!source.hasPermission("majestic.unjail")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            if (args.isEmpty()) {
                source.sendMessage(formatMessage(getMessage("messages", "not-enough-arguments")))
                source.sendMessage(Component.text("Usage: /prizonunjail <player>").color(NamedTextColor.RED))
                return
            }

            val targetPlayerName = args[0]
            val playerUuid = playerNameMap[targetPlayerName.lowercase()]

            if (playerUuid == null || !playerDataMap.containsKey(playerUuid)) {
                source.sendMessage(formatMessage(getMessage("messages", "unjail-fail").replace("%player%", targetPlayerName)))
                return
            }

            val playerData = playerDataMap[playerUuid]!!

            // Check if player has an active punishment
            if (playerData.activePunishment == null) {
                source.sendMessage(formatMessage(getMessage("messages", "unjail-fail").replace("%player%", targetPlayerName)))
                return
            }

            // Unjail the player directly
            val staffName = if (source is Player) source.username else "Console"
            unjailPlayer(playerUuid, staffName)
            source.sendMessage(formatMessage(getMessage("messages", "unjail-success").replace("%player%", targetPlayerName)))
        }

        override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
            val args = invocation.arguments()
            if (args.size == 1) {
                // Suggest jailed player names
                val input = args[0].lowercase()
                return playerNameMap.entries
                    .filter { it.key.startsWith(input) && playerDataMap.containsKey(it.value) }
                    .mapNotNull {
                        val data = playerDataMap[it.value]
                        if (data?.activePunishment != null) it.key else null
                    }
                    .toList()
            }
            return emptyList()
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.unjail")
        }
    }

    inner class CheckjailCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()
            val args = invocation.arguments()

            if (!source.hasPermission("majestic.checkjail")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            if (args.isEmpty()) {
                source.sendMessage(formatMessage(getMessage("messages", "not-enough-arguments")))
                source.sendMessage(Component.text("Usage: /prizoncheckjail <player>").color(NamedTextColor.RED))
                return
            }

            val targetPlayerName = args[0]
            val playerUuid = playerNameMap[targetPlayerName.lowercase()]

            if (playerUuid == null || !playerDataMap.containsKey(playerUuid)) {
                source.sendMessage(formatMessage(getMessage("messages", "checkjail-not-jailed").replace("%player%", targetPlayerName)))
                return
            }

            val playerData = playerDataMap[playerUuid]!!
            val activePunishment = playerData.activePunishment

            if (activePunishment == null) {
                source.sendMessage(formatMessage(getMessage("messages", "checkjail-not-jailed").replace("%player%", targetPlayerName)))
                return
            }

            val remainingSeconds = (activePunishment.endTimeMillis - System.currentTimeMillis()) / 1000

            source.sendMessage(
                formatMessage(getMessage("messages", "checkjail-is-jailed")
                    .replace("%player%", targetPlayerName)
                    .replace("%until%", formatDurationAlwaysShowAllUnits(remainingSeconds))
                    .replace("%reason%", activePunishment.reason ?: getMessage("messages", "no-reason-specified")))
            )
        }

        override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
            val args = invocation.arguments()
            if (args.size == 1) {
                // Suggest player names
                val input = args[0].lowercase()
                return playerNameMap.keys
                    .filter { it.startsWith(input) }
                    .toList()
            }
            return emptyList()
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.checkjail")
        }
    }

    inner class SetjailserverCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()
            val args = invocation.arguments()

            if (!source.hasPermission("majestic.set")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            if (source !is Player) {
                source.sendMessage(formatMessage(getMessage("messages", "console-error")))
                return
            }

            if (args.isEmpty()) {
                source.sendMessage(formatMessage(getMessage("messages", "not-enough-arguments")))
                source.sendMessage(Component.text("Usage: /prizonset <server>").color(NamedTextColor.RED))
                return
            }

            val serverName = args[0]
            val targetServer = this@JailPlugin.server.getServer(serverName)

            if (!targetServer.isPresent) {
                source.sendMessage(Component.text("Server '$serverName' not found.").color(NamedTextColor.RED))
                return
            }

            config.node("jail-server").set(serverName)
            try {
                val loader = YamlConfigurationLoader.builder()
                    .file(dataDirectory.resolve("config.yml").toFile())
                    .build()
                loader.save(config)
                source.sendMessage(formatMessage(getMessage("messages", "setjailserver-success").replace("%server%", serverName)))
            } catch (e: IOException) {
                logger.error("Failed to save config", e)
                source.sendMessage(Component.text("Failed to save configuration.").color(NamedTextColor.RED))
            }
        }

        override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
            val args = invocation.arguments()
            if (args.size == 1) {
                // Suggest server names
                val input = args[0].lowercase()
                return server.allServers
                    .filter { it.serverInfo.name.lowercase().startsWith(input) }
                    .map { it.serverInfo.name }
                    .toList()
            }
            return emptyList()
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.set")
        }
    }

    inner class DeleteCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()

            if (!source.hasPermission("majestic.delete")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            if (source !is Player) {
                source.sendMessage(formatMessage(getMessage("messages", "console-error")))
                return
            }

            // Check if jail server is currently set
            val currentJailServer = config.node("jail-server").getString("")
            if (currentJailServer.isEmpty()) {
                source.sendMessage(formatMessage(getMessage("messages", "delete-fail")))
                return
            }

            // Remove the jail server setting
            config.node("jail-server").set("")
            
            try {
                val loader = YamlConfigurationLoader.builder()
                    .file(dataDirectory.resolve("config.yml").toFile())
                    .build()
                loader.save(config)
                source.sendMessage(formatMessage(getMessage("messages", "delete-success")))
            } catch (e: IOException) {
                logger.error("Failed to save config", e)
                source.sendMessage(Component.text("Failed to save configuration.").color(NamedTextColor.RED))
            }
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.delete")
        }
    }

    inner class JailIPCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()
            val args = invocation.arguments()

            if (!source.hasPermission("majestic.jailip")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            if (args.size < 3) {
                source.sendMessage(Component.text("Usage: /prizonjailip <ip> <duration> <reason>").color(NamedTextColor.RED))
                return
            }

            val ipAddress = normalizeIpAddress(args[0])
            val durationStr = args[1]
            val reason = args.sliceArray(2 until args.size).joinToString(" ")

            val duration = parseDuration(durationStr)
            if (duration == null) {
                source.sendMessage(formatMessage(getMessage("messages", "wrong-time-format")))
                return
            }

            val jailServerName = config.node("jail-server").getString("")
            if (jailServerName.isEmpty()) {
                source.sendMessage(formatMessage(getMessage("messages", "jail-server-not-set")))
                return
            }

            val endTimeMillis = if (duration > 0) System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(duration) else 0L

            // Jail all players with this IP
            var jailedCount = 0
            for ((uuid, playerData) in playerDataMap) {
                if (playerData.lastIp == ipAddress) {
                    val punishment = PunishmentRecord(
                        reason,
                        System.currentTimeMillis(),
                        endTimeMillis,
                        if (source is Player) source.username else "Console"
                    )

                    playerData.punishments.add(punishment)
                    playerData.activePunishment = punishment
                    playerData.offenseCounts[reason] = (playerData.offenseCounts[reason] ?: 0) + 1

                    // Move player to jail if online
                    val player = server.getPlayer(uuid)
                    if (player.isPresent) {
                        val jailServer = server.getServer(jailServerName)
                        if (jailServer.isPresent) {
                            player.get().createConnectionRequest(jailServer.get()).connect()
                            sendJailedMessages(player.get(), punishment)
                        }
                    }

                    jailedCount++
                }
            }

            savePlayerData()

            source.sendMessage(formatMessage(
                getMessage("messages", "jailip-success")
                    .replace("%ip%", ipAddress)
            ))
            source.sendMessage(Component.text("Jailed $jailedCount player(s) with IP $ipAddress").color(NamedTextColor.GREEN))
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.jailip")
        }
    }

    inner class UnjailIPCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()
            val args = invocation.arguments()

            if (!source.hasPermission("majestic.unjailip")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            if (args.isEmpty()) {
                source.sendMessage(Component.text("Usage: /prizonunjailip <ip>").color(NamedTextColor.RED))
                return
            }

            val ipAddress = normalizeIpAddress(args[0])

            // Unjail all players with this IP
            var unjailedCount = 0
            for ((uuid, playerData) in playerDataMap) {
                if (playerData.lastIp == ipAddress && playerData.activePunishment != null) {
                    unjailPlayer(uuid)
                    unjailedCount++
                }
            }

            if (unjailedCount > 0) {
                source.sendMessage(formatMessage(
                    getMessage("messages", "unjailip-success")
                        .replace("%ip%", ipAddress)
                ))
                source.sendMessage(Component.text("Unjailed $unjailedCount player(s) with IP $ipAddress").color(NamedTextColor.GREEN))
            } else {
                source.sendMessage(formatMessage(
                    getMessage("messages", "unjailip-fail")
                        .replace("%ip%", ipAddress)
                ))
            }
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.unjailip")
        }
    }

    inner class PrizonjailreloadCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()

            if (!source.hasPermission("majestic.reload")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            loadConfig()
            source.sendMessage(formatMessage(getMessage("messages", "reload")))
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.reload")
        }
    }

    inner class OffenseCommand : SimpleCommand {
        override fun execute(invocation: SimpleCommand.Invocation) {
            val source = invocation.source()
            val args = invocation.arguments()

            if (!source.hasPermission("majestic.offense")) {
                source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                return
            }

            if (args.isEmpty()) {
                source.sendMessage(Component.text("Usage: /prizonoffense <add|remove|list> <player> [args]").color(NamedTextColor.RED))
                return
            }

            val subCommand = args[0].lowercase()

            when (subCommand) {
                "add" -> {
                    if (!source.hasPermission("majestic.offense.add")) {
                        source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                        return
                    }

                    if (args.size < 4) {
                        source.sendMessage(Component.text("Usage: /prizonoffense add <player> <reason> <duration>").color(NamedTextColor.RED))
                        return
                    }

                    val targetPlayerName = args[1]
                    val playerUuid = playerNameMap[targetPlayerName.lowercase()]

                    if (playerUuid == null) {
                        source.sendMessage(Component.text("Player $targetPlayerName not found.").color(NamedTextColor.RED))
                        return
                    }

                    val playerData = playerDataMap[playerUuid]
                    if (playerData == null) {
                        source.sendMessage(Component.text("Player $targetPlayerName not found.").color(NamedTextColor.RED))
                        return
                    }

                    val durationStr = args[args.size - 1]
                    val duration = parseDuration(durationStr)
                    if (duration == null) {
                        source.sendMessage(Component.text("Invalid duration format: $durationStr").color(NamedTextColor.RED))
                        return
                    }

                    val reason = args.sliceArray(2 until args.size - 1).joinToString(" ")
                    val endTimeMillis = if (duration > 0) System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(duration) else 0L

                    val punishment = PunishmentRecord(
                        reason,
                        System.currentTimeMillis(),
                        endTimeMillis,
                        if (source is Player) source.username else "Console"
                    )

                    playerData.punishments.add(punishment)
                    playerData.offenseCounts[reason] = (playerData.offenseCounts[reason] ?: 0) + 1

                    savePlayerData()

                    val durationText = if (duration > 0) formatDurationAlwaysShowAllUnits(duration) else "permanent"
                    source.sendMessage(formatMessage(
                        getMessage("messages", "offense-add-success")
                            .replace("%player%", targetPlayerName)
                            .replace("%reason%", reason)
                            .replace("%duration%", durationText)
                    ))
                }

                "remove" -> {
                    if (!source.hasPermission("majestic.offense.remove")) {
                        source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                        return
                    }

                    if (args.size < 2) {
                        source.sendMessage(Component.text("Usage: /prizonoffense remove <player>").color(NamedTextColor.RED))
                        return
                    }

                    val targetPlayerName = args[1]
                    val playerUuid = playerNameMap[targetPlayerName.lowercase()]

                    if (playerUuid == null) {
                        source.sendMessage(Component.text("Player $targetPlayerName not found.").color(NamedTextColor.RED))
                        return
                    }

                    val playerData = playerDataMap[playerUuid]
                    if (playerData == null) {
                        source.sendMessage(Component.text("Player $targetPlayerName not found.").color(NamedTextColor.RED))
                        return
                    }

                    // Clear all punishments and offense counts
                    playerData.punishments.clear()
                    playerData.offenseCounts.clear()
                    playerData.activePunishment = null

                    savePlayerData()

                    source.sendMessage(formatMessage(
                        getMessage("messages", "offense-remove-success")
                            .replace("%player%", targetPlayerName)
                    ))
                }

                "list" -> {
                    if (!source.hasPermission("majestic.offense.list")) {
                        source.sendMessage(formatMessage(getMessage("messages", "permission-message")))
                        return
                    }

                    if (args.size < 2) {
                        source.sendMessage(Component.text("Usage: /prizonoffense list <player>").color(NamedTextColor.RED))
                        return
                    }

                    val targetPlayerName = args[1]
                    val playerUuid = playerNameMap[targetPlayerName.lowercase()]

                    if (playerUuid == null) {
                        source.sendMessage(Component.text("Player $targetPlayerName not found.").color(NamedTextColor.RED))
                        return
                    }

                    val playerData = playerDataMap[playerUuid]
                    if (playerData == null || playerData.punishments.isEmpty()) {
                        source.sendMessage(formatMessage(
                            getMessage("messages", "offense-list-empty")
                                .replace("%player%", targetPlayerName)
                        ))
                        return
                    }

                    source.sendMessage(formatMessage(
                        getMessage("messages", "offense-list-header")
                            .replace("%player%", targetPlayerName)
                    ))

                    for ((index, punishment) in playerData.punishments.withIndex()) {
                        val reason = punishment.reason ?: "Unknown"
                        val issuedBy = punishment.issuedBy
                        val startTime = formatTimestamp(punishment.startTimeMillis)
                        val endTime = if (punishment.endTimeMillis > 0) formatTimestamp(punishment.endTimeMillis) else "Never"
                        val status = if (punishment.pardonedBy != null) "Pardoned" else if (punishment.endTimeMillis > 0 && System.currentTimeMillis() >= punishment.endTimeMillis) "Expired" else "Active"

                        source.sendMessage(Component.text("  ${index + 1}. $reason").color(NamedTextColor.WHITE))
                        source.sendMessage(Component.text("     Issued by: $issuedBy | Status: $status").color(NamedTextColor.GRAY))
                        source.sendMessage(Component.text("     Start: $startTime | End: $endTime").color(NamedTextColor.GRAY))
                    }

                    // Show offense counts
                    if (playerData.offenseCounts.isNotEmpty()) {
                        source.sendMessage(Component.text("  Offense Counts:").color(NamedTextColor.YELLOW))
                        for ((reason, count) in playerData.offenseCounts) {
                            source.sendMessage(Component.text("    $reason: $count").color(NamedTextColor.WHITE))
                        }
                    }
                }

                else -> {
                    source.sendMessage(Component.text("Usage: /prizonoffense <add|remove|list> <player> [args]").color(NamedTextColor.RED))
                }
            }
        }

        override fun suggest(invocation: SimpleCommand.Invocation): List<String> {
            val args = invocation.arguments()
            return when {
                args.size == 1 -> listOf("add", "remove", "list")
                args.size == 2 -> playerNameMap.keys.toList()
                else -> emptyList()
            }
        }

        override fun hasPermission(invocation: SimpleCommand.Invocation): Boolean {
            return invocation.source().hasPermission("majestic.offense")
        }

        private fun formatDuration(seconds: Long): String {
            if (seconds == 0L) return "0s"

            val days = seconds / 86400L
            val hours = (seconds % 86400L) / 3600L
            val minutes = (seconds % 3600L) / 60L
            val secs = seconds % 60L

            val sb = StringBuilder()

            if (days > 0) sb.append("${days}d")
            if (hours > 0) sb.append("${hours}h")
            if (minutes > 0) sb.append("${minutes}m")
            if (secs > 0) sb.append("${secs}s")

            return sb.toString()
        }
    }

    /**
     * Calculate jail duration based on offense system
     */
    private fun calculateDuration(playerData: PlayerData, reason: String?, defaultDuration: Long): Long {
        // Check if reason is blacklisted
        if (reason != null && blacklistReasons.contains(reason)) {
            return defaultDuration
        }

        // Get offense durations
        val durations = if (reason != null) offenseDurations[reason] else null

        if (durations == null || durations.isEmpty()) {
            // Reason not in offense system, use default
            return defaultDuration
        }

        // Get the number of previous offenses for this reason
        val offenseCount = if (reason != null) playerData.getOffenseCount(reason) else 0

        // Determine which duration to use (1st, 2nd, 3rd, or 4th+)
        val index = minOf(offenseCount, durations.size - 1)

        return durations[index]
    }

    /**
     * Open punishment GUI for a player
     */
    private fun openPunishmentGUI(staff: Player, playerUuid: UUID, playerName: String) {
        val playerData = playerDataMap[playerUuid]
        if (playerData == null) {
            staff.sendMessage(Component.text("Player data not found.").color(NamedTextColor.RED))
            return
        }

        // Create plugin message to open GUI
        val out = ByteStreams.newDataOutput()
        out.writeUTF("OpenPunishmentGUI")
        out.writeUTF(playerName)

        // Add punishment history
        val punishments = playerData.punishments
        out.writeInt(punishments.size)

        for (punishment in punishments) {
            out.writeUTF(punishment.reason ?: "")
            out.writeLong(punishment.startTimeMillis)
            out.writeLong(punishment.endTimeMillis)
            out.writeUTF(punishment.issuedBy)
            out.writeUTF(punishment.pardonedBy ?: "")
            out.writeLong(punishment.pardonTimeMillis)
        }

        // Send plugin message to player
        staff.sendPluginMessage(GUI_CHANNEL, out.toByteArray())
    }

    /**
     * Handle plugin messages from Bukkit servers
     */
    @Subscribe
    fun onPluginMessage(event: com.velocitypowered.api.event.connection.PluginMessageEvent) {
        if (event.identifier != GUI_CHANNEL) {
            return
        }

        if (event.source !is Player) {
            return
        }

        val player = event.source as Player

        // Ensure we're on the main thread
        server.scheduler.buildTask(this, Runnable {
            handlePluginMessage(player, event.data)
        }).schedule()
    }

    private fun handlePluginMessage(player: Player, data: ByteArray) {
        val inStream = ByteStreams.newDataInput(data)

        val subChannel = inStream.readUTF()
        if (subChannel == "UnjailPlayer") {
            val playerName = inStream.readUTF()
            val playerUuid = playerNameMap[playerName.lowercase()]

            if (playerUuid != null) {
                unjailPlayer(playerUuid, player.username)
                player.sendMessage(formatMessage(getMessage("messages", "unjail-success").replace("%player%", playerName)))
            }
        }
    }

    /**
     * Unjails a player, allowing them to connect to any server again.
     */
    fun unjailPlayer(playerUuid: UUID, pardonedBy: String = "System") {
        val playerData = playerDataMap[playerUuid] ?: return
        val activePunishment = playerData.activePunishment ?: return

        // Mark punishment as pardoned
        activePunishment.pardonedBy = pardonedBy
        activePunishment.pardonTimeMillis = System.currentTimeMillis()

        // Clear active punishment
        playerData.activePunishment = null

        // Save player data
        savePlayerData()

        val player = server.getPlayer(playerUuid)
        if (player.isPresent) {
            player.get().sendMessage(formatMessage(getMessage("messages", "you-have-been-unjailed")))

            // Teleport player to lobby server
            val lobbyServerName = config.node("lobby-server").getString("lobby")
            if (lobbyServerName.isNotEmpty()) {
                val lobbyServer = server.getServer(lobbyServerName)
                if (lobbyServer.isPresent) {
                    player.get().createConnectionRequest(lobbyServer.get()).connect()
                    logger.info("Teleported ${player.get().username} to lobby server: $lobbyServerName")
                } else {
                    logger.warn("Lobby server '$lobbyServerName' not found. Player not teleported.")
                    // Try original server as fallback
                    val originalServerName = playerData.originalServerName
                    if (originalServerName != null && originalServerName != "unknown") {
                        val originalServer = server.getServer(originalServerName)
                        if (originalServer.isPresent) {
                            player.get().createConnectionRequest(originalServer.get()).connect()
                        }
                    }
                }
            } else {
                logger.warn("Lobby server not configured. Player not teleported.")
            }

            server.allPlayers.forEach { p ->
                p.sendMessage(
                    Component.text("${player.get().username} has been unjailed by $pardonedBy.")
                        .color(NamedTextColor.YELLOW)
                )
            }
        } else {
            // Player is offline - broadcast to online players
            logger.info("Player $playerUuid was unjailed by $pardonedBy but was offline. Their data has been updated.")
            server.allPlayers.forEach { p ->
                p.sendMessage(
                    Component.text("${playerData.playerName} has been unjailed by $pardonedBy (offline).")
                        .color(NamedTextColor.YELLOW)
                )
            }
        }
    }

    /**
     * Sends the specific jailed messages to the player.
     */
    private fun sendJailedMessages(player: Player, punishment: PunishmentRecord) {
        player.sendMessage(formatMessage(getMessage("messages", "you-have-been-jailed")))
        val reason = punishment.reason ?: getMessage("messages", "no-reason-specified")

        // Calculate remaining seconds and format
        val remainingSeconds = (punishment.endTimeMillis - System.currentTimeMillis()) / 1000
        val durationDisplay = formatDurationAlwaysShowAllUnits(remainingSeconds)

        player.sendMessage(
            formatMessage(getMessage("messages", "you-have-been-jailed-details")
                .replace("%reason%", reason)
                .replace("%until%", durationDisplay))
        )
    }

    /**
     * Loads player data from 'playerdata.yml'.
     */
    private fun loadPlayerData() {
        val playerDataFile = dataDirectory.resolve("playerdata.yml").toFile()
        if (!playerDataFile.exists()) {
            logger.info("No playerdata.yml found, starting with empty player data.")
            return
        }

        try {
            val loader = YamlConfigurationLoader.builder().file(playerDataFile).build()
            val data = loader.load()

            // Load player data
            val playersNode = data.node("players")
            if (!playersNode.virtual()) {
                for ((key, value) in playersNode.childrenMap()) {
                    try {
                        val uuid = try {
                            UUID.fromString(key.toString())
                        } catch (e: IllegalArgumentException) {
                            logger.warn("Invalid UUID found in playerdata.yml: $key")
                            continue
                        }

                        val playerNode = value

                        val playerName = playerNode.node("playerName").getString("unknown")
                        val lastIp = playerNode.node("lastIp").string
                        val originalServerName = playerNode.node("originalServerName").getString("unknown")

                        val playerData = PlayerData(playerName)
                        playerData.lastIp = lastIp
                        playerData.originalServerName = originalServerName

                        // Load punishments
                        val punishmentsNode = playerNode.node("punishments")
                        if (!punishmentsNode.virtual()) {
                            for ((_, punishmentEntry) in punishmentsNode.childrenMap()) {
                                try {
                                    val punishmentNode = punishmentEntry

                                    val reason = punishmentNode.node("reason").getString("Unknown")
                                    val startTimeMillis = punishmentNode.node("startTimeMillis").long
                                    val endTimeMillis = punishmentNode.node("endTimeMillis").long
                                    val issuedBy = punishmentNode.node("issuedBy").getString("Unknown")
                                    val pardonedBy = punishmentNode.node("pardonedBy").string
                                    val pardonTimeMillis = punishmentNode.node("pardonTimeMillis").long

                                    val punishment = PunishmentRecord(
                                        reason, startTimeMillis, endTimeMillis, issuedBy
                                    )

                                    if (pardonedBy != null) {
                                        punishment.pardonedBy = pardonedBy
                                        punishment.pardonTimeMillis = pardonTimeMillis
                                    }

                                    playerData.addPunishment(punishment)
                                } catch (e: Exception) {
                                    logger.warn("Invalid punishment data found for player $uuid: ${e.message}")
                                }
                            }
                        }

                        // Set active punishment if exists
                        val activePunishmentId = playerNode.node("activePunishment").string
                        if (activePunishmentId != null) {
                            for (punishment in playerData.punishments) {
                                if (punishment.reason == activePunishmentId &&
                                    (punishment.endTimeMillis == 0L ||
                                            punishment.endTimeMillis > System.currentTimeMillis()) &&
                                    punishment.pardonedBy == null) {
                                    playerData.activePunishment = punishment
                                    break
                                }
                            }
                        }

                        // Load offense counts
                        val offenseCountsNode = playerNode.node("offenseCounts")
                        if (!offenseCountsNode.virtual()) {
                            for ((reasonKey, countEntry) in offenseCountsNode.childrenMap()) {
                                try {
                                    val reason = reasonKey.toString()
                                    val count = countEntry.int
                                    playerData.setOffenseCount(reason, count)
                                } catch (e: Exception) {
                                    logger.warn("Invalid offense count data for player $uuid: ${e.message}")
                                }
                            }
                        }

                        playerDataMap[uuid] = playerData
                        playerNameMap[playerName.lowercase()] = uuid

                        logger.info("Loaded player data for: $playerName ($uuid)")
                    } catch (e: Exception) {
                        logger.warn("Invalid data found in playerdata.yml for player $key: ${e.message}")
                    }
                }
            }
        } catch (e: IOException) {
            logger.error("Failed to load player data", e)

            // Create backup of corrupted file
            try {
                val backupFile = File(playerDataFile.parent, "playerdata.yml.backup.${System.currentTimeMillis()}")
                playerDataFile.copyTo(backupFile)
                logger.info("Created backup of corrupted playerdata.yml at ${backupFile.absolutePath}")
            } catch (ex: Exception) {
                logger.error("Failed to create backup of corrupted playerdata.yml", ex)
            }
        }
    }

    /**
     * Saves all current player data to 'playerdata.yml'.
     */
    private fun savePlayerData() {
        val playerDataFile = dataDirectory.resolve("playerdata.yml").toFile()

        try {
            val loader = YamlConfigurationLoader.builder().file(playerDataFile).build()
            val data = loader.createNode()

            // Save player data
            val playersNode = data.node("players")
            for ((uuid, playerData) in playerDataMap) {
                val playerNode = playersNode.node(uuid.toString())

                playerNode.node("playerName").set(playerData.playerName)
                playerNode.node("lastIp").set(playerData.lastIp)
                playerNode.node("originalServerName").set(playerData.originalServerName)

                // Save punishments
                val punishmentsNode = playerNode.node("punishments")
                for ((index, punishment) in playerData.punishments.withIndex()) {
                    val punishmentNode = punishmentsNode.node(index.toString())

                    punishmentNode.node("reason").set(punishment.reason)
                    punishmentNode.node("startTimeMillis").set(punishment.startTimeMillis)
                    punishmentNode.node("endTimeMillis").set(punishment.endTimeMillis)
                    punishmentNode.node("issuedBy").set(punishment.issuedBy)

                    if (punishment.pardonedBy != null) {
                        punishmentNode.node("pardonedBy").set(punishment.pardonedBy)
                        punishmentNode.node("pardonTimeMillis").set(punishment.pardonTimeMillis)
                    }
                }

                // Save active punishment
                if (playerData.activePunishment != null) {
                    playerNode.node("activePunishment").set(playerData.activePunishment?.reason)
                }

                // Save offense counts
                val offenseCountsNode = playerNode.node("offenseCounts")
                for ((reason, count) in playerData.offenseCounts) {
                    offenseCountsNode.node(reason).set(count)
                }
            }

            loader.save(data)
        } catch (e: IOException) {
            logger.error("Failed to save player data", e)
        }
    }

    /**
     * Schedule unjail tasks for players who are already jailed.
     */
    private fun scheduleUnjailTasks() {
        for ((uuid, playerData) in playerDataMap) {
            val player = server.getPlayer(uuid)
            if (player.isPresent && playerData.activePunishment != null) {
                val activePunishment = playerData.activePunishment!!

                // If jail is indefinite or time has passed, unjail immediately.
                if (activePunishment.endTimeMillis == 0L) {
                    // Indefinite jail, don't schedule unjail
                    sendJailedMessages(player.get(), activePunishment)
                } else if (System.currentTimeMillis() >= activePunishment.endTimeMillis) {
                    // Time has passed, unjail immediately
                    unjailPlayer(uuid)
                } else {
                    val delayMillis = activePunishment.endTimeMillis - System.currentTimeMillis()
                    if (delayMillis > 0) {
                        server.scheduler
                            .buildTask(this, Runnable { unjailPlayer(uuid) })
                            .delay(delayMillis, TimeUnit.MILLISECONDS)
                            .schedule()
                        // Send jailed message on startup if player is already online
                        sendJailedMessages(player.get(), activePunishment)
                    } else {
                        // Time has already passed, unjail
                        unjailPlayer(uuid)
                    }
                }
            }
        }
    }

    /**
     * Get a message from the plugin's config.yml and translate color codes.
     */
    private fun getMessage(vararg path: String): String {
        return config.node(*path).getString("&cMessage not found: ${path.joinToString(".")}")
    }

    /**
     * Formats a message with color codes.
     */
    private fun formatMessage(message: String): Component {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message)
    }

    /**
     * Parses a duration string (e.g., "5s", "10m", "2h", "7d", "3w", "6M", "1y") into total seconds.
     */
    private fun parseDuration(durationString: String?): Long? {
        if (durationString == null) return null

        if (durationString.equals("warn", ignoreCase = true) ||
            durationString.equals("kick", ignoreCase = true)) {
            return 0L // Special case for warnings and kicks
        }

        val pattern = Pattern.compile("(\\d+)([smhdwyM])")
        val matcher = pattern.matcher(durationString)

        if (matcher.matches()) {
            return try {
                val value = matcher.group(1).toLong()
                val unit = matcher.group(2)

                when (unit) {
                    "s" -> value
                    "m" -> value * 60L
                    "h" -> value * 3600L
                    "d" -> value * 86400L
                    "w" -> value * 604800L
                    "M" -> value * 2592000L // Approximately 30 days per month
                    "y" -> value * 31536000L // Approximately 365 days per year
                    else -> null
                }
            } catch (e: NumberFormatException) {
                null
            }
        }
        return null
    }

    /**
     * Helper function to format seconds into a readable duration string.
     */
    private fun formatDurationAlwaysShowAllUnits(totalSeconds: Long): String {
        if (totalSeconds <= 0L) return getMessage("messages", "indefinitely")

        var remainingSeconds = totalSeconds
        val years = remainingSeconds / 31536000L
        remainingSeconds %= 31536000L
        val days = remainingSeconds / 86400L
        remainingSeconds %= 86400L
        val hours = remainingSeconds / 3600L
        remainingSeconds %= 3600L
        val minutes = remainingSeconds / 60L
        remainingSeconds %= 60L
        val seconds = remainingSeconds

        val parts = mutableListOf<String>()
        // Always add Year, Day, Hour, and Minute components
        parts.add("$years Year${if (years != 1L) "s" else ""}")
        parts.add("$days Day${if (days != 1L) "s" else ""}")
        parts.add("$hours Hour${if (hours != 1L) "s" else ""}")
        parts.add("$minutes Minute${if (minutes != 1L) "s" else ""}")

        // Add seconds only if all higher units are zero
        val allHigherUnitsZero = (years == 0L && days == 0L && hours == 0L && minutes == 0L)
        if (seconds > 0 && allHigherUnitsZero) {
            parts.add("$seconds Second${if (seconds != 1L) "s" else ""}")
        }

        return parts.joinToString(" ")
    }

    /**
     * Helper function to format a timestamp into a readable date/time string.
     */
    private fun formatTimeUntil(endTimeMillis: Long): String {
        if (endTimeMillis == 0L) return getMessage("messages", "indefinitely")

        val date = Instant.ofEpochMilli(endTimeMillis)
        val timeFormat = config.node("options", "timeformat").getString("dd.MM.yyyy HH:mm")

        return try {
            val formatter = DateTimeFormatter.ofPattern(timeFormat)
                .withZone(TimeZone.getDefault().toZoneId())
            formatter.format(date)
        } catch (e: Exception) {
            logger.warn("Invalid time format in config: '$timeFormat'. Using default.", e)
            DateTimeFormatter.ISO_INSTANT.format(date)
        }
    }

    /**
     * Helper function to format a timestamp (in milliseconds) into a readable date/time string.
     */
    private fun formatTimestamp(timestampMillis: Long): String {
        if (timestampMillis == 0L) return "N/A"

        val date = Instant.ofEpochMilli(timestampMillis)
        val timeFormat = config.node("options", "timeformat").getString("dd.MM.yyyy HH:mm")

        return try {
            val formatter = DateTimeFormatter.ofPattern(timeFormat)
                .withZone(TimeZone.getDefault().toZoneId())
            formatter.format(date)
        } catch (e: Exception) {
            logger.warn("Invalid time format in config: '$timeFormat'. Using default.", e)
            DateTimeFormatter.ISO_INSTANT.format(date)
        }
    }

    /**
     * Data class for player information
     */
    data class PlayerData(
        var playerName: String,
        var lastIp: String? = null,
        var originalServerName: String? = null,
        var activePunishment: PunishmentRecord? = null,
        val punishments: MutableList<PunishmentRecord> = mutableListOf(),
        val offenseCounts: MutableMap<String, Int> = mutableMapOf()
    ) {
        fun addPunishment(punishment: PunishmentRecord) {
            punishments.add(punishment)

            // Update offense count
            val reason = punishment.reason
            if (reason != null) {
                offenseCounts[reason] = offenseCounts.getOrDefault(reason, 0) + 1
            }
        }

        fun getOffenseCount(reason: String): Int {
            return offenseCounts.getOrDefault(reason, 0)
        }

        fun setOffenseCount(reason: String, count: Int) {
            offenseCounts[reason] = count
        }
    }

    /**
     * Data class for punishment records
     */
    data class PunishmentRecord(
        val reason: String?,
        val startTimeMillis: Long,
        val endTimeMillis: Long,
        val issuedBy: String,
        var pardonedBy: String? = null,
        var pardonTimeMillis: Long = 0
    )
}