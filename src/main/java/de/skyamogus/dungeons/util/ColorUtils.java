package de.skyamogus.dungeons.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    public static void sendMessage(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static String color(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F\\d]{6}");
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, ChatColor.of(color) + "");
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
