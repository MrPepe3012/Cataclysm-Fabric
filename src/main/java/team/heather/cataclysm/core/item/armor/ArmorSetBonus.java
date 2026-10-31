package team.heather.cataclysm.core.item.armor;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.core.item.CataclysmItems;

import java.util.List;

public record ArmorSetBonus(ResourceLocation id,
                            Item helmet,
                            Item chestplate,
                            Item leggings,
                            Item boots) {

    public static final ArmorSetBonus PALE = new ArmorSetBonus(
            Cataclysm.createNamespacedKey("pale_armor"),
            CataclysmItems.PALE_HELMET,
            CataclysmItems.PALE_CHESTPLATE,
            CataclysmItems.PALE_LEGGINGS,
            CataclysmItems.PALE_BOOTS
    );

    public static final ArmorSetBonus CALAMITY = new ArmorSetBonus(
            Cataclysm.createNamespacedKey("calamity_armor"),
            CataclysmItems.CALAMITY_HELMET,
            CataclysmItems.CALAMITY_CHESTPLATE,
            CataclysmItems.CALAMITY_LEGGINGS,
            CataclysmItems.CALAMITY_BOOTS
    );

    private static final List<ArmorSetBonus> SETS = List.of(PALE, CALAMITY);
    private static final double EXTRA_HEALTH = 4.0;

    public static void initialize() {
        ServerTickEvents.END_SERVER_TICK.register(server ->
                server.getPlayerList().getPlayers().forEach(player ->
                        SETS.forEach(set -> set.apply(player))));
    }

    public void apply(ServerPlayer player) {
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null) return;

        if (this.isWornBy(player)) {
            if (!maxHealth.hasModifier(this.id)) {
                maxHealth.addTransientModifier(new AttributeModifier(this.id, EXTRA_HEALTH, Operation.ADD_VALUE));
            }
        } else {
            maxHealth.removeModifier(this.id);
        }
    }

    public boolean isWornBy(ServerPlayer player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).is(this.helmet)
                && player.getItemBySlot(EquipmentSlot.CHEST).is(this.chestplate)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(this.leggings)
                && player.getItemBySlot(EquipmentSlot.FEET).is(this.boots);
    }
}
