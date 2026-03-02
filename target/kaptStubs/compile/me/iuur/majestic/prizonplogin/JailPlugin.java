package me.iuur.majestic.prizonplogin;

@com.velocitypowered.api.plugin.Plugin(id = "prizonplogin", name = "prizonplogin", version = "1.0", authors = {"MrAngelz"})
@kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u00a8\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0010\u000e\n\u0002\u0010 \n\u0002\u0010\t\n\u0000\n\u0002\u0010#\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0012\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0011\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0017\b\u0007\u0018\u0000 S2\u00020\u0001:\fSTUVWXYZ[\\]^B#\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0004\b\b\u0010\tJ\u0010\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020\u001bH\u0007J\b\u0010\u001c\u001a\u00020\u0019H\u0002J\b\u0010\u001d\u001a\u00020\u0019H\u0002J\b\u0010\u001e\u001a\u00020\u0019H\u0002J\b\u0010\u001f\u001a\u00020\u0019H\u0002J\u0010\u0010 \u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020!H\u0007J\u0010\u0010\"\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020#H\u0007J\u0010\u0010$\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020%H\u0007J\u0010\u0010&\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u00020'H\u0007J\u0010\u0010(\u001a\u00020)2\u0006\u0010*\u001a\u00020+H\u0002J\u0010\u0010,\u001a\u00020\u000e2\u0006\u0010-\u001a\u00020\u000eH\u0002J\"\u0010.\u001a\u00020\u00102\u0006\u0010/\u001a\u00020\u00162\b\u00100\u001a\u0004\u0018\u00010\u000e2\u0006\u00101\u001a\u00020\u0010H\u0002J \u00102\u001a\u00020\u00192\u0006\u00103\u001a\u00020+2\u0006\u00104\u001a\u00020\u00152\u0006\u00105\u001a\u00020\u000eH\u0002J\u0010\u00106\u001a\u00020\u00192\u0006\u0010\u001a\u001a\u000207H\u0007J\u0018\u00108\u001a\u00020\u00192\u0006\u0010*\u001a\u00020+2\u0006\u00109\u001a\u00020:H\u0002J\u0018\u0010;\u001a\u00020\u00192\u0006\u00104\u001a\u00020\u00152\b\b\u0002\u0010<\u001a\u00020\u000eJ\u0018\u0010=\u001a\u00020\u00192\u0006\u0010*\u001a\u00020+2\u0006\u0010>\u001a\u00020?H\u0002J\b\u0010@\u001a\u00020\u0019H\u0002J\b\u0010A\u001a\u00020\u0019H\u0002J\b\u0010B\u001a\u00020\u0019H\u0002J!\u0010C\u001a\u00020\u000e2\u0012\u0010D\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u000e0E\"\u00020\u000eH\u0002\u00a2\u0006\u0002\u0010FJ\u0010\u0010G\u001a\u00020H2\u0006\u0010I\u001a\u00020\u000eH\u0002J\u0019\u0010J\u001a\u0004\u0018\u00010\u00102\b\u0010K\u001a\u0004\u0018\u00010\u000eH\u0002\u00a2\u0006\u0002\u0010LJ\u0010\u0010M\u001a\u00020\u000e2\u0006\u0010N\u001a\u00020\u0010H\u0002J\u0010\u0010O\u001a\u00020\u000e2\u0006\u0010P\u001a\u00020\u0010H\u0002J\u0010\u0010Q\u001a\u00020\u000e2\u0006\u0010R\u001a\u00020\u0010H\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082.\u00a2\u0006\u0002\n\u0000R \u0010\f\u001a\u0014\u0012\u0004\u0012\u00020\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00100\u000f0\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u000e0\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0013\u001a\u000e\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u00160\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0017\u001a\u000e\u0012\u0004\u0012\u00020\u000e\u0012\u0004\u0012\u00020\u00150\u0014X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006_"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin;", "", "server", "Lcom/velocitypowered/api/proxy/ProxyServer;", "logger", "Lorg/slf4j/Logger;", "dataDirectory", "Ljava/nio/file/Path;", "<init>", "(Lcom/velocitypowered/api/proxy/ProxyServer;Lorg/slf4j/Logger;Ljava/nio/file/Path;)V", "config", "Lorg/spongepowered/configurate/ConfigurationNode;", "offenseDurations", "", "", "", "", "blacklistReasons", "", "playerDataMap", "Ljava/util/concurrent/ConcurrentHashMap;", "Ljava/util/UUID;", "Lme/iuur/majestic/prizonplogin/JailPlugin$PlayerData;", "playerNameMap", "onProxyInitialization", "", "event", "Lcom/velocitypowered/api/event/proxy/ProxyInitializeEvent;", "loadConfig", "validateConfig", "loadOffenseSystem", "registerCommands", "onPlayerChooseInitialServer", "Lcom/velocitypowered/api/event/player/PlayerChooseInitialServerEvent;", "onServerConnected", "Lcom/velocitypowered/api/event/player/ServerConnectedEvent;", "onPlayerCommand", "Lcom/velocitypowered/api/event/command/CommandExecuteEvent;", "onPostLogin", "Lcom/velocitypowered/api/event/connection/PostLoginEvent;", "checkForIpBanEvasion", "", "player", "Lcom/velocitypowered/api/proxy/Player;", "normalizeIpAddress", "ip", "calculateDuration", "playerData", "reason", "defaultDuration", "openPunishmentGUI", "staff", "playerUuid", "playerName", "onPluginMessage", "Lcom/velocitypowered/api/event/connection/PluginMessageEvent;", "handlePluginMessage", "data", "", "unjailPlayer", "pardonedBy", "sendJailedMessages", "punishment", "Lme/iuur/majestic/prizonplogin/JailPlugin$PunishmentRecord;", "loadPlayerData", "savePlayerData", "scheduleUnjailTasks", "getMessage", "path", "", "([Ljava/lang/String;)Ljava/lang/String;", "formatMessage", "Lnet/kyori/adventure/text/Component;", "message", "parseDuration", "durationString", "(Ljava/lang/String;)Ljava/lang/Long;", "formatDurationAlwaysShowAllUnits", "totalSeconds", "formatTimeUntil", "endTimeMillis", "formatTimestamp", "timestampMillis", "Companion", "JailCommand", "UnjailCommand", "CheckjailCommand", "SetjailserverCommand", "DeleteCommand", "JailIPCommand", "UnjailIPCommand", "PrizonjailreloadCommand", "OffenseCommand", "PlayerData", "PunishmentRecord", "prizonplogin"})
public final class JailPlugin {
    @org.jetbrains.annotations.NotNull()
    private final com.velocitypowered.api.proxy.ProxyServer server = null;
    @org.jetbrains.annotations.NotNull()
    private final org.slf4j.Logger logger = null;
    @org.jetbrains.annotations.NotNull()
    private final java.nio.file.Path dataDirectory = null;
    private org.spongepowered.configurate.ConfigurationNode config;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<java.lang.String, java.util.List<java.lang.Long>> offenseDurations = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Set<java.lang.String> blacklistReasons = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.util.UUID, me.iuur.majestic.prizonplogin.JailPlugin.PlayerData> playerDataMap = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.concurrent.ConcurrentHashMap<java.lang.String, java.util.UUID> playerNameMap = null;
    private static final com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier GUI_CHANNEL = null;
    private static final long MAX_JAIL_DURATION_SECONDS = 1576800000L;
    private static final long IP_BAN_DURATION_SECONDS = 1576800000L;
    @org.jetbrains.annotations.NotNull()
    public static final me.iuur.majestic.prizonplogin.JailPlugin.Companion Companion = null;
    
    @com.google.inject.Inject()
    public JailPlugin(@org.jetbrains.annotations.NotNull()
    com.velocitypowered.api.proxy.ProxyServer server, @org.jetbrains.annotations.NotNull()
    org.slf4j.Logger logger, @com.velocitypowered.api.plugin.annotation.DataDirectory()
    @org.jetbrains.annotations.NotNull()
    java.nio.file.Path dataDirectory) {
        super();
    }
    
    @com.velocitypowered.api.event.Subscribe()
    public final void onProxyInitialization(@org.jetbrains.annotations.NotNull()
    com.velocitypowered.api.event.proxy.ProxyInitializeEvent event) {
    }
    
    private final void loadConfig() {
    }
    
    private final void validateConfig() {
    }
    
    private final void loadOffenseSystem() {
    }
    
    private final void registerCommands() {
    }
    
    @com.velocitypowered.api.event.Subscribe()
    public final void onPlayerChooseInitialServer(@org.jetbrains.annotations.NotNull()
    com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent event) {
    }
    
    @com.velocitypowered.api.event.Subscribe()
    public final void onServerConnected(@org.jetbrains.annotations.NotNull()
    com.velocitypowered.api.event.player.ServerConnectedEvent event) {
    }
    
    @com.velocitypowered.api.event.Subscribe()
    public final void onPlayerCommand(@org.jetbrains.annotations.NotNull()
    com.velocitypowered.api.event.command.CommandExecuteEvent event) {
    }
    
    @com.velocitypowered.api.event.Subscribe()
    public final void onPostLogin(@org.jetbrains.annotations.NotNull()
    com.velocitypowered.api.event.connection.PostLoginEvent event) {
    }
    
    /**
     * Checks if a player is trying to evade a jail sentence based on their IP address.
     * If a matching IP is found with a blacklisted reason, the player is auto-jailed.
     * @return true if the player was auto-jailed for IP evasion, false otherwise.
     */
    private final boolean checkForIpBanEvasion(com.velocitypowered.api.proxy.Player player) {
        return false;
    }
    
    private final java.lang.String normalizeIpAddress(java.lang.String ip) {
        return null;
    }
    
    /**
     * Calculate jail duration based on offense system
     */
    private final long calculateDuration(me.iuur.majestic.prizonplogin.JailPlugin.PlayerData playerData, java.lang.String reason, long defaultDuration) {
        return 0L;
    }
    
    /**
     * Open punishment GUI for a player
     */
    private final void openPunishmentGUI(com.velocitypowered.api.proxy.Player staff, java.util.UUID playerUuid, java.lang.String playerName) {
    }
    
    /**
     * Handle plugin messages from Bukkit servers
     */
    @com.velocitypowered.api.event.Subscribe()
    public final void onPluginMessage(@org.jetbrains.annotations.NotNull()
    com.velocitypowered.api.event.connection.PluginMessageEvent event) {
    }
    
    private final void handlePluginMessage(com.velocitypowered.api.proxy.Player player, byte[] data) {
    }
    
    /**
     * Unjails a player, allowing them to connect to any server again.
     */
    public final void unjailPlayer(@org.jetbrains.annotations.NotNull()
    java.util.UUID playerUuid, @org.jetbrains.annotations.NotNull()
    java.lang.String pardonedBy) {
    }
    
    /**
     * Sends the specific jailed messages to the player.
     */
    private final void sendJailedMessages(com.velocitypowered.api.proxy.Player player, me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord punishment) {
    }
    
    /**
     * Loads player data from 'playerdata.yml'.
     */
    private final void loadPlayerData() {
    }
    
    /**
     * Saves all current player data to 'playerdata.yml'.
     */
    private final void savePlayerData() {
    }
    
    /**
     * Schedule unjail tasks for players who are already jailed.
     */
    private final void scheduleUnjailTasks() {
    }
    
    /**
     * Get a message from the plugin's config.yml and translate color codes.
     */
    private final java.lang.String getMessage(java.lang.String... path) {
        return null;
    }
    
    /**
     * Formats a message with color codes.
     */
    private final net.kyori.adventure.text.Component formatMessage(java.lang.String message) {
        return null;
    }
    
    /**
     * Parses a duration string (e.g., "5s", "10m", "2h", "7d", "3w", "6M", "1y") into total seconds.
     */
    private final java.lang.Long parseDuration(java.lang.String durationString) {
        return null;
    }
    
    /**
     * Helper function to format seconds into a readable duration string.
     */
    private final java.lang.String formatDurationAlwaysShowAllUnits(long totalSeconds) {
        return null;
    }
    
    /**
     * Helper function to format a timestamp into a readable date/time string.
     */
    private final java.lang.String formatTimeUntil(long endTimeMillis) {
        return null;
    }
    
    /**
     * Helper function to format a timestamp (in milliseconds) into a readable date/time string.
     */
    private final java.lang.String formatTimestamp(long timestampMillis) {
        return null;
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\r"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$CheckjailCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "suggest", "", "", "hasPermission", "", "prizonplogin"})
    public final class CheckjailCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public CheckjailCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.util.List<java.lang.String> suggest(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return null;
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\t\b\u0002\u00a2\u0006\u0004\b\u0002\u0010\u0003R\u0016\u0010\u0004\u001a\n \u0006*\u0004\u0018\u00010\u00050\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$Companion;", "", "<init>", "()V", "GUI_CHANNEL", "Lcom/velocitypowered/api/proxy/messages/MinecraftChannelIdentifier;", "kotlin.jvm.PlatformType", "MAX_JAIL_DURATION_SECONDS", "", "IP_BAN_DURATION_SECONDS", "prizonplogin"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\n"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$DeleteCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "hasPermission", "", "prizonplogin"})
    public final class DeleteCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public DeleteCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\r"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$JailCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "suggest", "", "", "hasPermission", "", "prizonplogin"})
    public final class JailCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public JailCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.util.List<java.lang.String> suggest(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return null;
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\n"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$JailIPCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "hasPermission", "", "prizonplogin"})
    public final class JailIPCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public JailIPCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\r\u001a\u00020\n2\u0006\u0010\u000e\u001a\u00020\u000fH\u0002\u00a8\u0006\u0010"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$OffenseCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "suggest", "", "", "hasPermission", "", "formatDuration", "seconds", "", "prizonplogin"})
    public final class OffenseCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public OffenseCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.util.List<java.lang.String> suggest(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return null;
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
        
        private final java.lang.String formatDuration(long seconds) {
            return null;
        }
    }
    
    /**
     * Data class for player information
     */
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0000\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\b\u0013\n\u0002\u0010\u0002\n\u0002\b\r\n\u0002\u0010\u000b\n\u0002\b\u0004\b\u0086\b\u0018\u00002\u00020\u0001BY\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u0012\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\u0004\b\r\u0010\u000eJ\u000e\u0010\u001f\u001a\u00020 2\u0006\u0010!\u001a\u00020\u0007J\u000e\u0010\"\u001a\u00020\f2\u0006\u0010#\u001a\u00020\u0003J\u0016\u0010$\u001a\u00020 2\u0006\u0010#\u001a\u00020\u00032\u0006\u0010%\u001a\u00020\fJ\t\u0010&\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010'\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010(\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010)\u001a\u0004\u0018\u00010\u0007H\u00c6\u0003J\u000f\u0010*\u001a\b\u0012\u0004\u0012\u00020\u00070\tH\u00c6\u0003J\u0015\u0010+\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0003J]\u0010,\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0005\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u000e\b\u0002\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t2\u0014\b\u0002\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\f0\u000bH\u00c6\u0001J\u0014\u0010-\u001a\u00020.2\b\u0010/\u001a\u0004\u0018\u00010\u0001H\u00d6\u0083\u0004J\n\u00100\u001a\u00020\fH\u00d6\u0081\u0004J\n\u00101\u001a\u00020\u0003H\u00d6\u0081\u0004R\u001a\u0010\u0002\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012R\u001c\u0010\u0004\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0013\u0010\u0010\"\u0004\b\u0014\u0010\u0012R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u0010\"\u0004\b\u0016\u0010\u0012R\u001c\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0018\"\u0004\b\u0019\u0010\u001aR\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u001cR\u001d\u0010\n\u001a\u000e\u0012\u0004\u0012\u00020\u0003\u0012\u0004\u0012\u00020\f0\u000b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001e\u00a8\u00062"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$PlayerData;", "", "playerName", "", "lastIp", "originalServerName", "activePunishment", "Lme/iuur/majestic/prizonplogin/JailPlugin$PunishmentRecord;", "punishments", "", "offenseCounts", "", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lme/iuur/majestic/prizonplogin/JailPlugin$PunishmentRecord;Ljava/util/List;Ljava/util/Map;)V", "getPlayerName", "()Ljava/lang/String;", "setPlayerName", "(Ljava/lang/String;)V", "getLastIp", "setLastIp", "getOriginalServerName", "setOriginalServerName", "getActivePunishment", "()Lme/iuur/majestic/prizonplogin/JailPlugin$PunishmentRecord;", "setActivePunishment", "(Lme/iuur/majestic/prizonplogin/JailPlugin$PunishmentRecord;)V", "getPunishments", "()Ljava/util/List;", "getOffenseCounts", "()Ljava/util/Map;", "addPunishment", "", "punishment", "getOffenseCount", "reason", "setOffenseCount", "count", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "toString", "prizonplogin"})
    public static final class PlayerData {
        @org.jetbrains.annotations.NotNull()
        private java.lang.String playerName;
        @org.jetbrains.annotations.Nullable()
        private java.lang.String lastIp;
        @org.jetbrains.annotations.Nullable()
        private java.lang.String originalServerName;
        @org.jetbrains.annotations.Nullable()
        private me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord activePunishment;
        @org.jetbrains.annotations.NotNull()
        private final java.util.List<me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord> punishments = null;
        @org.jetbrains.annotations.NotNull()
        private final java.util.Map<java.lang.String, java.lang.Integer> offenseCounts = null;
        
        public PlayerData(@org.jetbrains.annotations.NotNull()
        java.lang.String playerName, @org.jetbrains.annotations.Nullable()
        java.lang.String lastIp, @org.jetbrains.annotations.Nullable()
        java.lang.String originalServerName, @org.jetbrains.annotations.Nullable()
        me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord activePunishment, @org.jetbrains.annotations.NotNull()
        java.util.List<me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord> punishments, @org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.Integer> offenseCounts) {
            super();
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getPlayerName() {
            return null;
        }
        
        public final void setPlayerName(@org.jetbrains.annotations.NotNull()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getLastIp() {
            return null;
        }
        
        public final void setLastIp(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getOriginalServerName() {
            return null;
        }
        
        public final void setOriginalServerName(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord getActivePunishment() {
            return null;
        }
        
        public final void setActivePunishment(@org.jetbrains.annotations.Nullable()
        me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord p0) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord> getPunishments() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<java.lang.String, java.lang.Integer> getOffenseCounts() {
            return null;
        }
        
        public final void addPunishment(@org.jetbrains.annotations.NotNull()
        me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord punishment) {
        }
        
        public final int getOffenseCount(@org.jetbrains.annotations.NotNull()
        java.lang.String reason) {
            return 0;
        }
        
        public final void setOffenseCount(@org.jetbrains.annotations.NotNull()
        java.lang.String reason, int count) {
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component2() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component3() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord component4() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.List<me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord> component5() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.util.Map<java.lang.String, java.lang.Integer> component6() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final me.iuur.majestic.prizonplogin.JailPlugin.PlayerData copy(@org.jetbrains.annotations.NotNull()
        java.lang.String playerName, @org.jetbrains.annotations.Nullable()
        java.lang.String lastIp, @org.jetbrains.annotations.Nullable()
        java.lang.String originalServerName, @org.jetbrains.annotations.Nullable()
        me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord activePunishment, @org.jetbrains.annotations.NotNull()
        java.util.List<me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord> punishments, @org.jetbrains.annotations.NotNull()
        java.util.Map<java.lang.String, java.lang.Integer> offenseCounts) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\n"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$PrizonjailreloadCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "hasPermission", "", "prizonplogin"})
    public final class PrizonjailreloadCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public PrizonjailreloadCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
    }
    
    /**
     * Data class for punishment records
     */
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\t\n\u0002\b\u001a\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\b\u0086\b\u0018\u00002\u00020\u0001B?\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u0012\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u0003\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005\u00a2\u0006\u0004\b\n\u0010\u000bJ\u000b\u0010\u0018\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u0019\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001a\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u001b\u001a\u00020\u0003H\u00c6\u0003J\u000b\u0010\u001c\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00c6\u0003JI\u0010\u001e\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\b\b\u0002\u0010\u0007\u001a\u00020\u00032\n\b\u0002\u0010\b\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\t\u001a\u00020\u0005H\u00c6\u0001J\u0014\u0010\u001f\u001a\u00020 2\b\u0010!\u001a\u0004\u0018\u00010\u0001H\u00d6\u0083\u0004J\n\u0010\"\u001a\u00020#H\u00d6\u0081\u0004J\n\u0010$\u001a\u00020\u0003H\u00d6\u0081\u0004R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u001c\u0010\b\u001a\u0004\u0018\u00010\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\r\"\u0004\b\u0013\u0010\u0014R\u001a\u0010\t\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0015\u0010\u000f\"\u0004\b\u0016\u0010\u0017\u00a8\u0006%"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$PunishmentRecord;", "", "reason", "", "startTimeMillis", "", "endTimeMillis", "issuedBy", "pardonedBy", "pardonTimeMillis", "<init>", "(Ljava/lang/String;JJLjava/lang/String;Ljava/lang/String;J)V", "getReason", "()Ljava/lang/String;", "getStartTimeMillis", "()J", "getEndTimeMillis", "getIssuedBy", "getPardonedBy", "setPardonedBy", "(Ljava/lang/String;)V", "getPardonTimeMillis", "setPardonTimeMillis", "(J)V", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "", "other", "hashCode", "", "toString", "prizonplogin"})
    public static final class PunishmentRecord {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.String reason = null;
        private final long startTimeMillis = 0L;
        private final long endTimeMillis = 0L;
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String issuedBy = null;
        @org.jetbrains.annotations.Nullable()
        private java.lang.String pardonedBy;
        private long pardonTimeMillis;
        
        public PunishmentRecord(@org.jetbrains.annotations.Nullable()
        java.lang.String reason, long startTimeMillis, long endTimeMillis, @org.jetbrains.annotations.NotNull()
        java.lang.String issuedBy, @org.jetbrains.annotations.Nullable()
        java.lang.String pardonedBy, long pardonTimeMillis) {
            super();
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getReason() {
            return null;
        }
        
        public final long getStartTimeMillis() {
            return 0L;
        }
        
        public final long getEndTimeMillis() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getIssuedBy() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String getPardonedBy() {
            return null;
        }
        
        public final void setPardonedBy(@org.jetbrains.annotations.Nullable()
        java.lang.String p0) {
        }
        
        public final long getPardonTimeMillis() {
            return 0L;
        }
        
        public final void setPardonTimeMillis(long p0) {
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component1() {
            return null;
        }
        
        public final long component2() {
            return 0L;
        }
        
        public final long component3() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component4() {
            return null;
        }
        
        @org.jetbrains.annotations.Nullable()
        public final java.lang.String component5() {
            return null;
        }
        
        public final long component6() {
            return 0L;
        }
        
        @org.jetbrains.annotations.NotNull()
        public final me.iuur.majestic.prizonplogin.JailPlugin.PunishmentRecord copy(@org.jetbrains.annotations.Nullable()
        java.lang.String reason, long startTimeMillis, long endTimeMillis, @org.jetbrains.annotations.NotNull()
        java.lang.String issuedBy, @org.jetbrains.annotations.Nullable()
        java.lang.String pardonedBy, long pardonTimeMillis) {
            return null;
        }
        
        @java.lang.Override()
        public boolean equals(@org.jetbrains.annotations.Nullable()
        java.lang.Object other) {
            return false;
        }
        
        @java.lang.Override()
        public int hashCode() {
            return 0;
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\r"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$SetjailserverCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "suggest", "", "", "hasPermission", "", "prizonplogin"})
    public final class SetjailserverCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public SetjailserverCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.util.List<java.lang.String> suggest(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return null;
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0016\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\r"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$UnjailCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "suggest", "", "", "hasPermission", "", "prizonplogin"})
    public final class UnjailCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public UnjailCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        @org.jetbrains.annotations.NotNull()
        public java.util.List<java.lang.String> suggest(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return null;
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
    }
    
    @kotlin.Metadata(mv = {2, 3, 0}, k = 1, xi = 48, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\u0007\u00a2\u0006\u0004\b\u0002\u0010\u0003J\u0010\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016J\u0010\u0010\b\u001a\u00020\t2\u0006\u0010\u0006\u001a\u00020\u0007H\u0016\u00a8\u0006\n"}, d2 = {"Lme/iuur/majestic/prizonplogin/JailPlugin$UnjailIPCommand;", "Lcom/velocitypowered/api/command/SimpleCommand;", "<init>", "(Lme/iuur/majestic/prizonplogin/JailPlugin;)V", "execute", "", "invocation", "Lcom/velocitypowered/api/command/SimpleCommand$Invocation;", "hasPermission", "", "prizonplogin"})
    public final class UnjailIPCommand implements com.velocitypowered.api.command.SimpleCommand {
        
        public UnjailIPCommand() {
            super();
        }
        
        @java.lang.Override()
        public void execute(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
        }
        
        @java.lang.Override()
        public boolean hasPermission(@org.jetbrains.annotations.NotNull()
        com.velocitypowered.api.command.SimpleCommand.Invocation invocation) {
            return false;
        }
    }
}