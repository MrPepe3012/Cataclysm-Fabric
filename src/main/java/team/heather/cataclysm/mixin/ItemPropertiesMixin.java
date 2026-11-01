package team.heather.cataclysm.mixin;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.heather.cataclysm.Cataclysm;

@Mixin(Item.Properties.class)
public abstract class ItemPropertiesMixin {
    @Shadow @Final private DataComponentMap.Builder components;
    @Shadow private ResourceKey<Item> id;

    /** 1.21.5 replaces ITEM_NAME with its default translation during construction. */
    @ModifyVariable(method = "buildAndValidateComponents", at = @At("HEAD"), argsOnly = true)
    private Component cataclysm$preserveConfiguredName(Component defaultName) {
        if (this.id == null || !this.id.location().getNamespace().equals(Cataclysm.MOD_ID)) return defaultName;
        Component configured = this.components.build().get(DataComponents.ITEM_NAME);
        return configured == null ? defaultName : configured;
    }
}
