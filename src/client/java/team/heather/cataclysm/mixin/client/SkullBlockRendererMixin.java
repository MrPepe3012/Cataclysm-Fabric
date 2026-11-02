package team.heather.cataclysm.mixin.client;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.SkullBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import team.heather.cataclysm.model.GolemSkullModel;
import team.heather.cataclysm.registry.block.GolemSkullType;

@Mixin(SkullBlockRenderer.class)
public abstract class SkullBlockRendererMixin {

    @Inject(method = "createModel", at = @At("HEAD"), cancellable = true)
    private static void createModel$0(EntityModelSet models, SkullBlock.Type type,
                                      CallbackInfoReturnable<SkullModelBase> callback) {
        if (type == GolemSkullType.GOLEM) {
            callback.setReturnValue(new GolemSkullModel(models));
        }
    }

    @Inject(method = "getRenderType(Lnet/minecraft/world/level/block/SkullBlock$Type;Lnet/minecraft/world/item/component/ResolvableProfile;Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/RenderType;",
            at = @At("HEAD"), cancellable = true)
    private static void getRenderType$0(SkullBlock.Type type, ResolvableProfile profile, ResourceLocation override,
                                        CallbackInfoReturnable<RenderType> callback) {
        if (type == GolemSkullType.GOLEM) {
            ResourceLocation texture = override != null
                    ? override
                    : ResourceLocation.withDefaultNamespace("textures/entity/iron_golem/iron_golem.png");
            callback.setReturnValue(RenderType.entityCutoutNoCull(texture));
        }
    }
}
