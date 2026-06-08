package com.cataclysm.chat;

import com.cataclysm.Cataclysm;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public final class CataclysmBroadcast {
    public static final String TOTEM_USE = key("totem_use");
    public static final String TOTEM_FAIL = key("totem_fail");


    private CataclysmBroadcast() {}

    public static void broadcast(String key, Object... args) {
        Cataclysm.getServer().getPlayerList().getPlayers().forEach(player -> player.sendSystemMessage(build(key, args)));
    }

    public static void send(ServerPlayer player, String key, Object... args) {
        player.sendSystemMessage(build(key, args));
    }

    private static String key(String key) {
        return "broadcast.cataclysm." + key;
    }

    private static Component build(String key, Object... args) {
        return parse(String.format(Component.translatable(key).getString(), args));
    }

    private static MutableComponent text(String message) {
        return Component.literal(message).withStyle(Style.EMPTY.withColor(ChatFormatting.WHITE));
    }

    private static MutableComponent parse(String message) {
        MutableComponent result = Component.empty();
        int i = 0;
        while (i < message.length()) {
            int open = message.indexOf('<', i);
            int close = open == -1 ? -1 : message.indexOf('>', open);

            if (open == -1 || close == -1) {
                result.append(text(message.substring(i)));
                i = message.length();
            } else {
                if (open > i) result.append(text(message.substring(i, open)));
                result.append(Component.literal(message.substring(open + 1, close)).withStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
                i = close + 1;
            }
        }
        return result;
    }
}