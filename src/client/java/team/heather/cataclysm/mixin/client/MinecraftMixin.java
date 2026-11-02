package team.heather.cataclysm.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.registry.item.lemegeton.LemegetonItem;
import team.heather.cataclysm.registry.item.lemegeton.OpenLemegetonPayload;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Inject(method = "startAttack", at = @At("HEAD"), cancellable = true)
    private void startAttack$0(CallbackInfoReturnable<Boolean> callback) {
        if (this.isCatalogueGesture$0()) {
            ClientPlayNetworking.send(OpenLemegetonPayload.INSTANCE);
            callback.setReturnValue(false);
        }
    }

    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void continueAttack$0(boolean attacking, CallbackInfo callback) {
        if (this.isCatalogueGesture$0()) {
            callback.cancel();
        }
    }

    @Unique
    private boolean isCatalogueGesture$0() {
        Minecraft client = (Minecraft) (Object) this;
        return client.player != null
                && client.player.isShiftKeyDown()
                && client.player.getMainHandItem().getItem() instanceof LemegetonItem
                && ClientPlayNetworking.canSend(OpenLemegetonPayload.TYPE);
    }
}
