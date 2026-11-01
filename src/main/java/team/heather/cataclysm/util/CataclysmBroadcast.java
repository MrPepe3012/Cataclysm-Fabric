package team.heather.cataclysm.util;

import team.heather.cataclysm.Cataclysm;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;

public final class CataclysmBroadcast {
    public static final String TOTEM_USE = key("totem_use");
    public static final String TOTEM_FAIL = key("totem_fail");

    private static final TextColor BRACKET_COLOR = TextColor.fromRgb(0x8c8c8c);
    private static final TextColor CATACLYSM_COLOR = TextColor.fromRgb(0xA03E3E);
    private static final TextColor TEXT_COLOR = TextColor.fromRgb(0xB0B0B0);
    private static final TextColor ARROW_COLOR = TextColor.fromRgb(0x727272);
    private static final TextColor DARK_GRAY = TextColor.fromRgb(0x404040);
    private static final MutableComponent PREFIX = buildPrefix();

    private CataclysmBroadcast() {}

    public static void broadcast(String key, Object... args) {
        Cataclysm.minecraftServer().getPlayerList().getPlayers().forEach(player -> player.sendSystemMessage(build(key, args)));
    }

    public static void send(ServerPlayer player, String key, Object... args) {
        player.sendSystemMessage(build(key, args));
    }

    private static String key(String key) {
        return "broadcast.cataclysm." + key;
    }

    private static Component build(String key, Object... args) {
        return Component.empty()
                .withStyle(Style.EMPTY.withBold(false))
                .append(PREFIX.copy())
                .append(parse(String.format(Component.translatable(key).getString(), args)));
    }

    private static MutableComponent buildPrefix() {
        return Component.literal("[")
                .withStyle(Style.EMPTY.withColor(BRACKET_COLOR).withBold(true))
                .append(Component.literal("Cataclysm").withStyle(Style.EMPTY.withColor(CATACLYSM_COLOR).withBold(false)))
                .append(Component.literal("]").withStyle(Style.EMPTY.withColor(BRACKET_COLOR).withBold(true)))
                .append(Component.literal(" » ").withStyle(Style.EMPTY.withColor(ARROW_COLOR).withBold(false)));
    }

    private static MutableComponent text(String message) {
        return Component.literal(message).withStyle(Style.EMPTY.withColor(TEXT_COLOR).withBold(false));
    }

    private static MutableComponent highlight(String message) {
        return Component.literal(message).withStyle(Style.EMPTY.withColor(CATACLYSM_COLOR).withBold(false));
    }

    private static MutableComponent darkGray(String message) {
        return Component.literal(message).withStyle(Style.EMPTY.withColor(DARK_GRAY).withBold(false));
    }

    private static MutableComponent parse(String message) {
        MutableComponent result = Component.empty();
        int i = 0;
        while (i < message.length()) {
            int openAngle = message.indexOf('<', i);
            int closeAngle = openAngle == -1 ? -1 : message.indexOf('>', openAngle);
            boolean hasAngle = openAngle != -1 && closeAngle != -1;

            int openStar = message.indexOf('*', i);
            int closeStar = openStar == -1 ? -1 : message.indexOf('*', openStar + 1);
            boolean hasStar = openStar != -1 && closeStar != -1;

            if (!hasAngle && !hasStar) {
                result.append(text(message.substring(i)));
                i = message.length();
            } else if (hasAngle && (!hasStar || openAngle <= openStar)) {
                if (openAngle > i) result.append(text(message.substring(i, openAngle)));
                result.append(highlight(message.substring(openAngle + 1, closeAngle)));
                i = closeAngle + 1;
            } else {
                if (openStar > i) result.append(text(message.substring(i, openStar)));
                result.append(darkGray(message.substring(openStar + 1, closeStar)));
                i = closeStar + 1;
            }
        }
        return result;
    }
}