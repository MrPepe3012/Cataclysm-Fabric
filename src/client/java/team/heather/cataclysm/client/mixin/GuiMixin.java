package team.heather.cataclysm.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import team.heather.cataclysm.client.effect.MobEffectHeartRenderer;
import team.heather.cataclysm.effect.harmful.PaleCorrosionMobEffect;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private int tickCount;

    /** Keeps the slots of the hearts disabled by Pale Corrosion in the health bar layout. */
    @ModifyVariable(method = "renderPlayerHealth", at = @At("STORE"), ordinal = 0)
    private float renderPlayerHealth$0(float maxHealth, @Local Player player) {
        MobEffectHeartRenderer.updatePaleHearts(player);

        float fullMaxHealth = (float) player.getAttributeValue(Attributes.MAX_HEALTH) + PaleCorrosionMobEffect.getDisabledHealth(player);
        return Math.max(maxHealth, fullMaxHealth);
    }

    @Inject(
        method = "renderHearts",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
            ordinal = 0,
            shift = At.Shift.AFTER
        )
    )
    private void renderHearts$0(GuiGraphics graphics,
                                Player player,
                                int x,
                                int y,
                                int rowHeight,
                                int regenerationSlot,
                                float maxHealth,
                                int health,
                                int displayHealth,
                                int absorption,
                                boolean blinking,
                                CallbackInfo callback,
                                @Local(ordinal = 10) int slot,
                                @Local(ordinal = 13) int heartX,
                                @Local(ordinal = 14) int heartY) {
        MobEffectHeartRenderer.renderPaleHeart(graphics, player, slot, heartX, heartY);
    }

    @Inject(method = "renderHearts", at = @At("TAIL"))
    private void renderHearts$1(GuiGraphics graphics,
                                Player player,
                                int x,
                                int y,
                                int rowHeight,
                                int regenerationSlot,
                                float maxHealth,
                                int health,
                                int displayHealth,
                                int absorption,
                                boolean blinking,
                                CallbackInfo callback) {
        var bar = new MobEffectHeartRenderer.HeartBar(x, y, rowHeight, regenerationSlot);
        MobEffectHeartRenderer.renderSuppressedDamage(graphics, player, bar, maxHealth, health, absorption, this.tickCount);
    }

    @ModifyExpressionValue(
        method = "renderHeart",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui$HeartType;getSprite(ZZZ)Lnet/minecraft/resources/ResourceLocation;")
    )
    private ResourceLocation renderHeart$0(ResourceLocation sprite) {
        return this.minecraft.getCameraEntity() instanceof Player player ? MobEffectHeartRenderer.immunitySprite(player, sprite) : sprite;
    }
}
