package team.heather.cataclysm.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.heather.cataclysm.registry.item.twisted.TwistedMining;

@Mixin(Block.class)
public abstract class TwistedMiningMixin {
    @ModifyVariable(method = "getDrops(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/item/ItemStack;)Ljava/util/List;",
            at = @At("HEAD"), argsOnly = true)
    private static ItemStack cataclysm$miningLoot(ItemStack tool, @Local(argsOnly = true) ServerLevel level) {
        return TwistedMining.lootTool(tool, level.registryAccess());
    }
}
