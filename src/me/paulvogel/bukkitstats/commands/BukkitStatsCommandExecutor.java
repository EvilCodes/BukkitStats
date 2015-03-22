package me.paulvogel.bukkitstats.commands;

import me.paulvogel.bukkitstats.handlers.DBHandler;
import me.paulvogel.bukkitstats.handlers.MessagesHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class BukkitStatsCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (cs instanceof Player) {
            //CommandSender is a player
            final Player player = (Player) cs;
            if (args.length == 0) {
                //Display help
            } else if (args[0].equalsIgnoreCase("info")) {
                if (args[1].equalsIgnoreCase("general")) {
                    if (args[2] != null && args[2] != "") {
                        //Give info about the given player or uuid
                        OfflinePlayer offlinePlayer;
                        if (args[2].contains("-") && args[2].split("-")[0].length() == 8) 
                            //It's a uuid
                            offlinePlayer = Bukkit.getOfflinePlayer(UUID.fromString(args[2]));
                        else 
                            //It's a playername
                            offlinePlayer = Bukkit.getOfflinePlayer(args[2]);
                        final String uuid = offlinePlayer.getUniqueId().toString();
                        final ArrayList<String> stringList = new ArrayList<String>();
                        /*
                        <HEADER>
                            Name: <name>
                            UUID: <uuid>
                            First Seen: <first_login>
                            Last Seen: <last_login>
                            Times played: <times_logged_in>
                        <FOOTER>
                         */
                        stringList.add(MessagesHandler.convert("Stats.Header").replace("%name%", offlinePlayer.getName()));
                        //stringList.add("")
                    } else {
                        player.sendMessage(MessagesHandler.convert("Info.NoPlayer"));
                    }
                //} else if (args[1].equalsIgnoreCase()) {
                } else {
                    //Used for making a nice list (table1, table2, etc.)
                    String tables = "";
                    for (final String table : DBHandler.dbtables) {
                        if (tables.length() == 0)
                            tables = table;
                        else
                            tables = tables + ", " + table;
                    }
                    player.sendMessage(MessagesHandler.convert("Info.NoTable").replace("%tables%", tables));
                }
            }
        } else {
            
        }
        return true;
    }
}