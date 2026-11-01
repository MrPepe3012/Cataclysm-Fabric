package team.heather.cataclysm.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.WingsLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import team.heather.cataclysm.model.MirageElytraModel;
import team.heather.cataclysm.registry.item.CataclysmItems;

@Mixin(WingsLayer.class)
public abstract class WingsLayerMixin {
    @Unique private ElytraModel cataclysm$mirage;
    @Unique private ElytraModel cataclysm$mirageBaby;

    @ModifyVariable(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/state/HumanoidRenderState;FF)V",
            at = @At("STORE"), ordinal = 0)
    private ElytraModel cataclysm$selectWings(ElytraModel vanilla, PoseStack pose, MultiBufferSource buffers,
                                             int light, HumanoidRenderState state, float yaw, float pitch) {
        if (!state.chestEquipment.is(CataclysmItems.MIRAGE_ELYTRA)) return vanilla;
        if (state.isBaby) {
            if (this.cataclysm$mirageBaby == null) this.cataclysm$mirageBaby = new ElytraModel(
                    MirageElytraModel.createLayer().apply(ElytraModel.BABY_TRANSFORMER).bakeRoot());
            return this.cataclysm$mirageBaby;
        }
        if (this.cataclysm$mirage == null) this.cataclysm$mirage = new ElytraModel(MirageElytraModel.createLayer().bakeRoot());
        return this.cataclysm$mirage;
    }
}
