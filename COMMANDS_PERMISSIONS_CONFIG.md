# PrizonLogin - Commands, Permissions & Configuration Guide

## Table of Contents
- [Overview](#overview)
- [Commands](#commands)
- [Permissions](#permissions)
- [Configuration](#configuration)
- [Offense System](#offense-system)
- [Time Format](#time-format)

---

## Overview

PrizonLogin is a Velocity proxy plugin that provides a comprehensive jail/punishment system with offense tracking, IP banning, and automatic punishment escalation based on player history.

---

## Commands

### `/prizonjail <player> <duration> [reason]`
**Aliases:** `/j`  
**Description:** Jails a player for a specified duration with an optional reason.  
**Permission:** `majestic.jail`  
**Usage Examples:**
```
/prizonjail Steve 1h Griefing
/prizonjail Alex 30d X-Ray
/j Bob 7d Excessive Swearing
```

**Duration Format:**
- `s` - seconds
- `m` - minutes
- `h` - hours
- `d` - days
- `w` - weeks
- `M` - months (30 days)
- `y` - years (365 days)

**Notes:**
- The duration is automatically calculated based on the offense system if the reason matches a configured offense
- Maximum jail duration is configurable (default: 18250d / ~50 years)
- Player will be automatically transferred to the jail server upon login
- **Works with offline players** - you can jail players who are not currently online, and they will be jailed when they next log in

---

### `/prizonunjail <player>`
**Aliases:** `/u`  
**Description:** Unjails a player and allows them to play on normal servers.  
**Permission:** `majestic.unjail`  
**Usage Examples:**
```
/prizonunjail Steve
/u Alex
```

**Notes:**
- Removes the active punishment but keeps the offense history
- Player will receive a message that they've been unjailed (if online)
- Player is automatically teleported to the lobby/hub server (configured in `lobby-server`) if they are online
- If the lobby server is not configured or doesn't exist, the player will be sent to their original server
- **Works with offline players** - you can unjail players who are not currently online, and they will be able to join any server when they next log in

---

### `/prizoncheckjail <player>`
**Aliases:** `/c`  
**Description:** Checks if a player is currently jailed and displays jail information.  
**Permission:** `majestic.checkjail`  
**Usage Examples:**
```
/prizoncheckjail Steve
/c Alex
```

**Output:**
- If jailed: Shows player name, jail expiration time, and reason
- If not jailed: Confirms player is not jailed

---

### `/prizonset <server>`
**Description:** Sets the jail server where jailed players will be sent.  
**Permission:** `majestic.set`  
**Usage Examples:**
```
/prizonset jail
/prizonset prison-server
```

**Notes:**
- The server must exist in your Velocity configuration
- This is required before you can jail any players

---

### `/prizondel`
**Description:** Removes the configured jail server.  
**Permission:** `majestic.delete`  
**Usage Examples:**
```
/prizondel
```

**Notes:**
- Players cannot be jailed if no jail server is set
- Existing jailed players will remain jailed but may not be transferred properly

---

### `/prizonjailip <player> <duration> [reason]`
**Description:** Jails a player's IP address. All accounts using this IP will be jailed.  
**Permission:** `majestic.jailip`  
**Usage Examples:**
```
/prizonjailip Steve 30d Alt Account Ban
/prizonjailip Alex 90d Ban Evasion
```

**Notes:**
- Affects all players connecting from the same IP address
- Useful for preventing ban evasion with alt accounts
- Duration format is the same as `/prizonjail`

---

### `/prizonunjailip <ip>`
**Description:** Removes an IP ban.  
**Permission:** `majestic.unjailip`  
**Usage Examples:**
```
/prizonunjailip 192.168.1.100
```

**Notes:**
- Requires the exact IP address
- All players using this IP will be able to connect normally again

---

### `/prizonjailreload`
**Description:** Reloads the plugin configuration from disk.  
**Permission:** `majestic.reload`  
**Usage Examples:**
```
/prizonjailreload
```

**Notes:**
- Reloads config.yml settings
- Does not reload player data (that's loaded from disk automatically)
- Useful after making configuration changes

---

### `/prizonoffense <add|remove|list> <player> [args]`
**Description:** Manages player offense records.  
**Permission:** `majestic.offense` (base permission)

#### Subcommands:

**`/prizonoffense add <player> <reason> <duration>`**  
**Permission:** `majestic.offense.add`  
**Description:** Manually adds an offense to a player's record.  
**Usage Examples:**
```
/prizonoffense add Steve X-Ray Ores/Dungeons 7d
/prizonoffense add Alex Chat Infraction 1h
```

**Notes:**
- Adds to the player's offense history
- Increments the offense count for that specific reason
- Does NOT automatically jail the player (use `/prizonjail` for that)

---

**`/prizonoffense remove <player>`**  
**Permission:** `majestic.offense.remove`  
**Description:** Removes all offenses from a player's record.  
**Usage Examples:**
```
/prizonoffense remove Steve
```

**Notes:**
- Clears all punishment history
- Resets all offense counts to zero
- Also unjails the player if they are currently jailed

---

**`/prizonoffense list <player>`**  
**Permission:** `majestic.offense.list`  
**Description:** Lists all offenses for a player.  
**Usage Examples:**
```
/prizonoffense list Steve
```

**Output Format:**
```
Offenses for Steve:
1. X-Ray Ores/Dungeons - Start: 01.01.2024 10:00 - End: 08.01.2024 10:00 - By: Admin
2. Chat Infraction - Start: 15.01.2024 14:30 - End: 15.01.2024 15:30 - By: Moderator
```

**Notes:**
- Shows all historical punishments, not just active ones
- Displays start time, end time (or "Never" for permanent), and who issued the punishment
- Time format is configurable in config.yml

---

## Permissions

### Permission Nodes

| Permission | Description | Default |
|------------|-------------|---------|
| `majestic.jail` | Allows jailing players | OP |
| `majestic.unjail` | Allows unjailing players | OP |
| `majestic.checkjail` | Allows checking jail status | OP |
| `majestic.set` | Allows setting the jail server | OP |
| `majestic.delete` | Allows removing the jail server | OP |
| `majestic.jailip` | Allows IP banning | OP |
| `majestic.unjailip` | Allows removing IP bans | OP |
| `majestic.reload` | Allows reloading the config | OP |
| `majestic.offense` | Base permission for offense commands | OP |
| `majestic.offense.add` | Allows adding offenses manually | OP |
| `majestic.offense.remove` | Allows removing all offenses from a player | OP |
| `majestic.offense.list` | Allows viewing player offense history | OP |

### Permission Setup Examples

**LuckPerms:**
```
/lp group admin permission set majestic.jail true
/lp group admin permission set majestic.unjail true
/lp group admin permission set majestic.offense true
/lp group admin permission set majestic.offense.add true
/lp group admin permission set majestic.offense.remove true
/lp group admin permission set majestic.offense.list true

/lp group moderator permission set majestic.jail true
/lp group moderator permission set majestic.unjail true
/lp group moderator permission set majestic.checkjail true
/lp group moderator permission set majestic.offense.list true
```

**Giving all permissions to a group:**
```
/lp group admin permission set majestic.* true
```

---

## Configuration

### config.yml Structure

```yaml
options:
  jail-on-login: true              # Automatically send jailed players to jail server on login
  timeformat: "dd.MM.yyyy HH:mm"   # Date/time format for displaying timestamps
  uuid: true                        # Use UUID for player identification (recommended)
  max-jail-duration: "18250d"      # Maximum allowed jail duration (~50 years)
  ip-ban-duration: "18250d"        # Default IP ban duration

jail-server: "jail"                # Name of the jail server (must match Velocity config)
lobby-server: "lobby"              # Name of the lobby/hub server (players are sent here when unjailed)

commands:
  filter: "whitelist"               # Filter type: "whitelist" or "blacklist"
  filtered-list:                    # Commands jailed players can use (if whitelist)
    - "help"
    - "discord"

messages:
  permission-message: "&cYou do not have permission to use this command."
  not-enough-arguments: "&cNot enough arguments."
  too-many-arguments: "&cToo many arguments."
  wrong-time-format: "&cWrong time format. Use: <number><s|m|h|d|w|M|y>"
  jail-server-not-set: "&cJail server has not been set! Use /prizonset."
  jail-success: "&aPlayer %player% has been jailed until %until%. Reason: %reason%."
  jail-fail: "&cFailed to jail %player%. Player already jailed or not found."
  unjail-success: "&aPlayer %player% has been unjailed."
  unjail-fail: "&cPlayer %player% is not jailed or could not be found."
  checkjail-is-jailed: "&e%player% is jailed until %until%. Reason: %reason%."
  checkjail-not-jailed: "&a%player% is not jailed."
  setjailserver-success: "&aJail server has been set to %server%!"
  delete-success: "&aJail server has been removed. No jail server is currently set."
  delete-fail: "&cNo jail server is currently set."
  jailip-success: "&aIP %ip% has been jailed. All players with this IP will be jailed."
  unjailip-success: "&aIP %ip% has been unjailed."
  unjailip-fail: "&cIP %ip% is not jailed."
  offense-add-success: "&aAdded offense to %player%: %reason% for %duration%."
  offense-remove-success: "&aRemoved offense from %player%."
  offense-list-header: "&eOffenses for %player%:"
  offense-list-empty: "&a%player% has no offenses."
  console-error: "&cYou can't use this command from console."
  you-have-been-jailed: "&4You are currently jailed and must only play on the hacker jail"
  you-have-been-jailed-details: "&fYou have been &4jailed for &e%reason% &fwhich will expire in &e%until%."
  you-have-been-unjailed: "&aYou have been unjailed! Welcome back."
  still-jailed: "&4You are currently jailed and must only play on the hacker jail"
  still-jailed-details: "&fYou have been &4jailed for &e%reason% &fwhich will expire in &e%until%."
  cant-use-this-command: "&cYou can't use this command while jailed!"
  cant-chat-while-jailed: "&cYou are currently jailed and cannot chat!"
  reload: "&aPrizonLogin config reloaded."
  indefinitely: "indefinitely"
  ip-ban-evade-message: "&cYou are jailed due to an active IP ban on this network."
```

### Configuration Options Explained

#### Options Section
- **jail-on-login**: If `true`, jailed players are automatically sent to the jail server when they log in
- **timeformat**: Java DateTimeFormatter pattern for displaying dates (e.g., "dd.MM.yyyy HH:mm" = "31.12.2024 23:59")
- **uuid**: Use UUID-based player identification (recommended to prevent name changes from bypassing jails)
- **max-jail-duration**: Maximum duration a player can be jailed (prevents accidental extremely long bans)
- **ip-ban-duration**: Default duration for IP bans

#### Jail Server
- **jail-server**: The name of the server where jailed players are sent. Must match a server name in your Velocity `velocity.toml` configuration.
- **lobby-server**: The name of the lobby/hub server where players are teleported when they are unjailed. Must match a server name in your Velocity `velocity.toml` configuration. If not set or the server doesn't exist, the plugin will attempt to send them to their original server.

#### Commands Section
- **filter**: 
  - `whitelist`: Only commands in `filtered-list` are allowed for jailed players
  - `blacklist`: All commands except those in `filtered-list` are allowed
- **filtered-list**: List of commands that are affected by the filter

#### Messages Section
All messages support color codes using `&` format:
- `&a` = Green
- `&c` = Red
- `&e` = Yellow
- `&4` = Dark Red
- `&f` = White

Placeholders:
- `%player%` - Player name
- `%until%` - Jail expiration time
- `%reason%` - Jail reason
- `%server%` - Server name
- `%ip%` - IP address
- `%duration%` - Duration text

---

## Offense System

The offense system automatically escalates punishments based on a player's history.

### How It Works

1. When you jail a player with a reason that matches an offense in the config, the plugin:
   - Checks how many times the player has been punished for that specific reason
   - Applies the duration from the offense configuration based on the offense count
   - Increments the offense count for that reason

2. Each offense type has a list of durations for the 1st, 2nd, 3rd, and 4th+ offense.

### Offense Configuration

```yaml
offense-system:
  blacklist-reasons:
    - "10+ Alts"
    - "IP Ban"
    - "Perm Ban"
    - "Manual Raid Ban"
    - "Webstore Fraud / Scamming / Chargeback"
  
  offenses:
    "X-Ray Ores/Dungeons":
      - "1d"    # 1st offense
      - "3d"    # 2nd offense
      - "7d"    # 3rd offense
      - "14d"   # 4th+ offense
    
    "Chat Infraction":
      - "Warn"  # 1st offense (no jail, just warning)
      - "Warn"  # 2nd offense
      - "Kick"  # 3rd offense (kick from server)
      - "1h"    # 4th+ offense (1 hour jail)
```

### Blacklist Reasons

Reasons listed in `blacklist-reasons` are considered permanent/severe offenses. When a player has any of these reasons in their history, they are treated as having a permanent ban.

### Special Duration Values

- **"Warn"**: No jail time, just a warning message
- **"Kick"**: Player is kicked from the server but not jailed
- **Any time duration**: Player is jailed for that duration (e.g., "1h", "7d", "30d")

### Example Offense Scenarios

**Scenario 1: First-time X-Ray offense**
```
/prizonjail Steve 1d X-Ray Ores/Dungeons
```
- Steve has no prior X-Ray offenses
- Plugin applies 1st offense duration: 1 day
- Steve's offense count for "X-Ray Ores/Dungeons" is now 1

**Scenario 2: Second X-Ray offense**
```
/prizonjail Steve 1d X-Ray Ores/Dungeons
```
- Steve has 1 prior X-Ray offense
- Plugin applies 2nd offense duration: 3 days (overrides the "1d" you entered)
- Steve's offense count for "X-Ray Ores/Dungeons" is now 2

**Scenario 3: Chat infraction progression**
```
/prizonjail Alex 1h Chat Infraction  # 1st: Warn only
/prizonjail Alex 1h Chat Infraction  # 2nd: Warn only
/prizonjail Alex 1h Chat Infraction  # 3rd: Kicked
/prizonjail Alex 1h Chat Infraction  # 4th: Jailed for 1 hour
```

### Default Offenses Included

The plugin comes with 40+ pre-configured offense types including:
- **Hacking**: X-Ray, KillAura, Fly, Speed, Criticals, etc.
- **Exploiting**: Duping, Glitching, Phase, Freecam, etc.
- **Chat**: Spam, Harassment, Hate Speech, Advertising, etc.
- **Behavior**: Griefing, Scamming, Trolling, etc.
- **Special**: Manual bans, IP bans, Permanent bans, etc.

See the full list in your `config.yml` after first run.

---

## Time Format

### Duration Input Format

When entering durations in commands, use the following format:
```
<number><unit>
```

**Units:**
- `s` - seconds (e.g., `30s` = 30 seconds)
- `m` - minutes (e.g., `15m` = 15 minutes)
- `h` - hours (e.g., `2h` = 2 hours)
- `d` - days (e.g., `7d` = 7 days)
- `w` - weeks (e.g., `2w` = 14 days)
- `M` - months (e.g., `3M` = 90 days)
- `y` - years (e.g., `1y` = 365 days)

**Examples:**
- `30s` - 30 seconds
- `5m` - 5 minutes
- `1h` - 1 hour
- `7d` - 7 days
- `2w` - 2 weeks (14 days)
- `3M` - 3 months (90 days)
- `1y` - 1 year (365 days)
- `50y` - 50 years (effectively permanent)

### Display Time Format

The `timeformat` option in config.yml controls how dates are displayed in messages and offense lists.

**Default:** `dd.MM.yyyy HH:mm`

**Common Patterns:**
- `dd.MM.yyyy HH:mm` → `31.12.2024 23:59`
- `MM/dd/yyyy hh:mm a` → `12/31/2024 11:59 PM`
- `yyyy-MM-dd HH:mm:ss` → `2024-12-31 23:59:59`
- `EEEE, MMMM dd, yyyy` → `Tuesday, December 31, 2024`

**Pattern Letters:**
- `dd` - Day of month (01-31)
- `MM` - Month (01-12)
- `yyyy` - Year (4 digits)
- `HH` - Hour (00-23, 24-hour format)
- `hh` - Hour (01-12, 12-hour format)
- `mm` - Minute (00-59)
- `ss` - Second (00-59)
- `a` - AM/PM marker
- `EEEE` - Day of week (Monday, Tuesday, etc.)
- `MMMM` - Month name (January, February, etc.)

---

## Player Data Storage

Player data is stored in `plugins/prizonplogin/playerdata/` directory:
- Each player has a separate YAML file named by their UUID
- Contains offense history, active punishments, and offense counts
- Automatically saved when changes are made
- Loaded on plugin startup

**Example player data file:**
```yaml
playerName: "Steve"
offenseCounts:
  "X-Ray Ores/Dungeons": 2
  "Chat Infraction": 1
punishments:
  - reason: "X-Ray Ores/Dungeons"
    startTimeMillis: 1704067200000
    endTimeMillis: 1704672000000
    issuedBy: "Admin"
activePunishment:
  reason: "X-Ray Ores/Dungeons"
  startTimeMillis: 1704067200000
  endTimeMillis: 1704672000000
  issuedBy: "Admin"
```

---

## Tips & Best Practices

1. **Set up the jail server first** using `/prizonset` before jailing any players
2. **Use the offense system** by matching your jail reasons to the configured offenses for automatic escalation
3. **Review offense history** with `/prizonoffense list` before issuing punishments
4. **Use IP bans** for ban evaders with multiple alt accounts
5. **Customize messages** in config.yml to match your server's style
6. **Back up player data** regularly from the `playerdata/` folder
7. **Test permissions** with different staff ranks to ensure proper access control
8. **Use descriptive reasons** when jailing players for better record keeping

---

## Troubleshooting

**Players aren't being sent to jail server:**
- Check that jail server is set with `/prizonset`
- Verify the server name matches your Velocity configuration
- Ensure `jail-on-login` is set to `true` in config.yml

**Offense system not working:**
- Make sure the reason you use matches exactly with an offense in config.yml (case-sensitive)
- Check that the offense has valid duration values configured

**Permissions not working:**
- Verify your permission plugin is properly installed on Velocity
- Check that permission nodes are spelled correctly (they're case-sensitive)
- Restart Velocity after making permission changes

**Time format errors:**
- Ensure your `timeformat` in config.yml uses valid Java DateTimeFormatter patterns
- Check the console for warnings about invalid time formats

---

## Support

For issues, questions, or feature requests, please contact the plugin developer or check your server's support channels.

**Plugin Version:** 1.0  
**Author:** MrAngelz  
**Platform:** Velocity Proxy