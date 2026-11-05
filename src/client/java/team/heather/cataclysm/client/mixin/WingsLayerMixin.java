package team.heather.cataclysm.client.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.heather.cataclysm.registry.CataclysmRegistry;
import team.heather.cataclysm.client.model.MirageElytraModel;

@Mixin(WingsLayer.class)
public abstract class WingsLayerMixin {

    @Unique private ElytraModel mirageModel$0;
    @Unique private ElytraModel mirageBabyModel$0;

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
            at = @At("STORE"), ordinal = 0)
    private ElytraModel render$0(ElytraModel vanilla, PoseStack pose, MultiBufferSource buffers, int light,
                                 HumanoidRenderState state, float yaw, float pitch) {
        if (!state.chestEquipment.is(CataclysmRegistry.ITEMS.MIRAGE_ELYTRA)) {
            return vanilla;
        }
        if (state.isBaby) {
            if (this.mirageBabyModel$0 == null) {
                this.mirageBabyModel$0 = new ElytraModel(
                        MirageElytraModel.createLayer().apply(ElytraModel.BABY_TRANSFORMER).bakeRoot());
            }
            return this.mirageBabyModel$0;
        }
        if (this.mirageModel$0 == null) {
            this.mirageModel$0 = new ElytraModel(MirageElytraModel.createLayer().bakeRoot());
        }
        return this.mirageModel$0;
    }
}
