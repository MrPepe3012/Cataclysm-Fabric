package team.heather.cataclysm.client.model;

import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;

import java.util.List;
import java.util.Map;

/** The vanilla golem head, normalized to the same origin used by skulls. */
public final class GolemSkullModel extends SkullModelBase {
    public GolemSkullModel(EntityModelSet models) {
        super(normalize(models.bakeLayer(ModelLayers.IRON_GOLEM).getChild("head")));
    }

    private static ModelPart normalize(ModelPart head) {
        // Vanilla head: body z=-5.5..2.5, nose bottom y=-1. Center the body and sink the nose one pixel below y=0.
        head.setPos(0, 2, 1.5F);
        // Rotate around the normalized skull origin, not the golem's original off-center pivot.
        return new ModelPart(List.of(), Map.of("head", head));
    }

    @Override
    public void setupAnim(float animation, float yaw, float pitch) {
        this.root.yRot = yaw * ((float) Math.PI / 180);
        this.root.xRot = pitch * ((float) Math.PI / 180);
    }
}
