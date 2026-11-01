package team.heather.cataclysm.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

/** Segmented, tapered wings; the vanilla ElytraModel animates the two root parts. */
public final class MirageElytraModel {
    private MirageElytraModel() {}

    public static LayerDefinition createLayer() {
        var mesh = new MeshDefinition();
        for (boolean right : new boolean[]{false, true}) {
            float side = right ? 1 : -1;
            var wing = mesh.getRoot().addOrReplaceChild(right ? "right_wing" : "left_wing",
                    CubeListBuilder.create().texOffs(0, 0).mirror(right)
                            .addBox(right ? 0 : -9, 0, 0, 9, 13, 2),
                    PartPose.offsetAndRotation(-side * 5, 0, 0, 0.2617994F, 0, side * 0.2617994F));
            wing.addOrReplaceChild("lower_membrane", CubeListBuilder.create().texOffs(0, 12).mirror(right)
                            .addBox(right ? 0.5F : -7.5F, 12, 0, 7, 7, 2), PartPose.ZERO);
            for (int feather = 0; feather < 3; feather++) {
                float offset = 1 + feather * 2.5F;
                wing.addOrReplaceChild("tip_" + feather, CubeListBuilder.create().texOffs(24 + feather * 8, 18 - feather).mirror(right)
                                .addBox(right ? 0 : -2, 0, 0, 2, 7 - feather, 1),
                        PartPose.offsetAndRotation(side * offset, 17 - feather, 0.5F,
                                0, 0, -side * (0.08F + feather * 0.12F)));
            }
        }
        return LayerDefinition.create(mesh, 64, 32);
    }
}
