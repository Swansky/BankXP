package fr.swansky.bankxp.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.ChatColor;

public class MessageUtils {


    public static TextComponent WarningMessage(String content) {
        return Message(content, MessageType.WARNING);
    }

    public static TextComponent ErrorMessage(String content) {
        return Message(content, MessageType.ERROR);
    }

    public static TextComponent InfoMessage(String content) {
        return Message(content, MessageType.INFO);
    }


    public static TextComponent Message(String content, MessageType type) {
        return Component.text().content(type.color + content).build();
    }

    enum MessageType {
        INFO(ChatColor.GRAY),
        WARNING(ChatColor.YELLOW),
        ERROR(ChatColor.RED);

        public final ChatColor color;

        MessageType(ChatColor color) {
            this.color = color;
        }
    }

}
