package team.heather.cataclysm.client.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import team.heather.cataclysm.Cataclysm;
import team.heather.cataclysm.client.model.ResourcePackArmorModel;
import team.heather.cataclysm.item.CataclysmItems;

import java.util.function.Supplier;

/** Shared renderer for Calamity, Pale and Mirage; vanilla poses animate the custom resource-pack geometry. */
public final class ResourcePackArmorRenderer implements ArmorRenderer, SimpleSynchronousResourceReloadListener {
    private final String name;
    private final ModelLayerLocation outerLayer;
    private final ModelLayerLocation innerLayer;
    private final ModelLayerLocation outerBabyLayer;
    private final ModelLayerLocation innerBabyLayer;
    private final ResourceLocation outerTexture;
    private final ResourceLocation innerTexture;

    private ResourcePackArmorRenderer(String name) {
        this.name = name;
        this.outerLayer = layer("outer");
        this.innerLayer = layer("inner");
        this.outerBabyLayer = layer("outer_baby");
        this.innerBabyLayer = layer("inner_baby");
        this.outerTexture = Cataclysm.resourceLocation("textures/entity/equipment/humanoid/" + name + ".png");
        this.innerTexture = Cataclysm.resourceLocation("textures/entity/equipment/humanoid_leggings/" + name + ".png");
    }

    private HumanoidModel<HumanoidRenderState> outer;
    private HumanoidModel<HumanoidRenderState> inner;
    private HumanoidModel<HumanoidRenderState> outerBaby;
    private HumanoidModel<HumanoidRenderState> innerBaby;

    public static void register() {
        register("calamity", ResourcePackArmorModel::calamityLayer,
                CataclysmItems.CALAMITY_HELMET,
                CataclysmItems.CALAMITY_CHESTPLATE,
                CataclysmItems.CALAMITY_LEGGINGS,
                CataclysmItems.CALAMITY_BOOTS
        );
        register("pale", ResourcePackArmorModel::paleLayer,
                CataclysmItems.PALE_HELMET,
                CataclysmItems.PALE_CHESTPLATE,
                CataclysmItems.PALE_LEGGINGS,
                CataclysmItems.PALE_BOOTS
        );
        register("mirage", ResourcePackArmorModel::mirageLayer, CataclysmItems.MIRAGE_HELMET);
    }

    private static void register(String name,
                                 Supplier<LayerDefinition> outer,
                                 Item... items) {
        var renderer = new ResourcePackArmorRenderer(name);
        EntityModelLayerRegistry.registerModelLayer(renderer.outerLayer, outer::get);
        EntityModelLayerRegistry.registerModelLayer(renderer.innerLayer, ResourcePackArmorModel::innerLayer);
        EntityModelLayerRegistry.registerModelLayer(renderer.outerBabyLayer, () -> outer.get().apply(HumanoidModel.BABY_TRANSFORMER));
        EntityModelLayerRegistry.registerModelLayer(renderer.innerBabyLayer, () -> ResourcePackArmorModel.innerLayer().apply(HumanoidModel.BABY_TRANSFORMER));
        ArmorRenderer.register(renderer, items);
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(renderer);
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffers, ItemStack stack, HumanoidRenderState state,
                       EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> context) {
        boolean leggings = slot == EquipmentSlot.LEGS;
        var model = model(leggings, state.isBaby);
        context.copyPropertiesTo(model);
        model.setAllVisible(false);
        switch (slot) {
            case HEAD -> model.head.visible = true;
            case CHEST -> {
                model.body.visible = true;
                model.rightArm.visible = true;
                model.leftArm.visible = true;
            }
            case LEGS -> {
                model.body.visible = true;
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            case FEET -> {
                model.rightLeg.visible = true;
                model.leftLeg.visible = true;
            }
            default -> { return; }
        }
        ArmorRenderer.renderPart(pose, buffers, light, stack, model, leggings ? this.innerTexture : this.outerTexture);
    }

    private HumanoidModel<HumanoidRenderState> model(boolean leggings, boolean baby) {
        var models = Minecraft.getInstance().getEntityModels();
        if (baby) {
            if (leggings) {
                if (this.innerBaby == null) this.innerBaby = new HumanoidModel<>(models.bakeLayer(this.innerBabyLayer));
                return this.innerBaby;
            }
            if (this.outerBaby == null) this.outerBaby = new HumanoidModel<>(models.bakeLayer(this.outerBabyLayer));
            return this.outerBaby;
        }
        if (leggings) {
            if (this.inner == null) this.inner = new HumanoidModel<>(models.bakeLayer(this.innerLayer));
            return this.inner;
        }
        if (this.outer == null) this.outer = new HumanoidModel<>(models.bakeLayer(this.outerLayer));
        return this.outer;
    }

    @Override
    public ResourceLocation getFabricId() {
        return Cataclysm.resourceLocation(this.name + "_armor_renderer");
    }

    @Override
    public void onResourceManagerReload(ResourceManager resources) {
        this.outer = this.inner = this.outerBaby = this.innerBaby = null;
    }

    private ModelLayerLocation layer(String name) {
        return new ModelLayerLocation(Cataclysm.resourceLocation(this.name + "_armor"), name);
    }
}
