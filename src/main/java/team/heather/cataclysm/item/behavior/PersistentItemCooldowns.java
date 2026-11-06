package team.heather.cataclysm.item.behavior;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import team.heather.cataclysm.mixin.ItemCooldownsMixin;

import java.util.HashMap;
import java.util.Map;

/** Keeps every vanilla item cooldown across restarts, reconnections and deaths. */
public final class PersistentItemCooldowns {

    private static final String SAVE_KEY = "cataclysm:item_cooldowns";
    private static final Map<ServerPlayer, CompoundTag> AWAITING_CONNECTION = new HashMap<>();

    private PersistentItemCooldowns() {}

    public static void initialize() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> restore(handler.player));
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) ->
            remainingTicks(oldPlayer).forEach(newPlayer.getCooldowns()::addCooldown));
    }

    public static void save(ServerPlayer player, CompoundTag tag) {
        var saved = new CompoundTag();
        remainingTicks(player).forEach((group, ticks) -> saved.putInt(group.toString(), ticks));
        tag.put(SAVE_KEY, saved);
    }

    /** A player is loaded before it has a connection, and starting a cooldown needs one to tell the client. */
    public static void load(ServerPlayer player, CompoundTag tag) {
        tag.getCompound(SAVE_KEY).ifPresent(saved -> AWAITING_CONNECTION.put(player, saved));
    }

    private static void restore(ServerPlayer player) {
        var saved = AWAITING_CONNECTION.remove(player);
        if (saved == null) {
            return;
        }
        for (var group : saved.keySet()) {
            player.getCooldowns().addCooldown(ResourceLocation.parse(group), saved.getIntOr(group, 0));
        }
    }

    public static void reset(ServerPlayer player) {
        remainingTicks(player).keySet().forEach(player.getCooldowns()::removeCooldown);
    }

    private static Map<ResourceLocation, Integer> remainingTicks(ServerPlayer player) {
        var cooldowns = (ItemCooldownsMixin) player.getCooldowns();
        var remaining = new HashMap<ResourceLocation, Integer>();
        cooldowns.getCooldowns$0().forEach((group, cooldown) ->
            remaining.put(group, cooldown.endTime() - cooldowns.getTickCount$0()));
        return remaining;
    }
}
