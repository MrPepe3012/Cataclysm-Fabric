package team.heather.cataclysm.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import team.heather.cataclysm.registry.item.CataclysmItems;
import team.heather.cataclysm.registry.item.paragon.ParagonEffects;

@Mixin(ThrownEnderpearl.class)
public abstract class ParagonPearlMixin {
    @WrapOperation(method = "onHit", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;teleport(Lnet/minecraft/world/level/portal/TeleportTransition;)Lnet/minecraft/server/level/ServerPlayer;"))
    private ServerPlayer cataclysm$paragonTeleport(ServerPlayer player, TeleportTransition transition,
                                                   Operation<ServerPlayer> original) {
        ServerPlayer teleported = original.call(player, transition);
        if (teleported != null && ((ThrownEnderpearl) (Object) this).getItem().is(CataclysmItems.PARAGON_PEARL)) {
            ParagonEffects.onPearlTeleport(teleported);
        }
        return teleported;
    }
}
