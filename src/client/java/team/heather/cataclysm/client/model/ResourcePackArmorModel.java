package team.heather.cataclysm.client.model;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;

/** Calamity, Pale and Mirage geometry ported from the resource pack's player armor models, with their 64x64 UVs. */
public final class ResourcePackArmorModel {
    private ResourcePackArmorModel() {}

    public static LayerDefinition calamityLayer() {
        var mesh = skeleton();
        var root = mesh.getRoot();
        var helmet = root.getChild("head").addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4F, -31F, -4F, 8F, 8F, 8F, new CubeDeformation(0.8F)), PartPose.offsetAndRotation(0F, 23F, 0F, 0F, 0F, 0F));
        helmet.addOrReplaceChild("layer2", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4F, -4F, -4F, 8F, 8F, 8F, new CubeDeformation(1F)), PartPose.offsetAndRotation(0F, -27F, 0F, 0F, 0F, 0F));
        var crest = helmet.addOrReplaceChild("calamity_head", CubeListBuilder.create()
                .texOffs(0, 53).addBox(-1F, -35F, -4F, 2F, 3F, 8F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 1F, 0F, 0F, 0F, 0F));
        var crestPlanes = crest.addOrReplaceChild("2D", CubeListBuilder.create(), PartPose.offsetAndRotation(-2F, 0F, -3F, 0F, 0F, 0F));
        crestPlanes.addOrReplaceChild("r", CubeListBuilder.create()
                .texOffs(50, 49).addBox(0F, -4F, -3.5F, 0F, 8F, 7F, new CubeDeformation(0F)), PartPose.offsetAndRotation(-1F, -34F, -2.5F, 0F, 0F, 0F));
        crestPlanes.addOrReplaceChild("l", CubeListBuilder.create()
                .texOffs(50, 49).addBox(0F, -4F, -3.5F, 0F, 8F, 7F, new CubeDeformation(0F)), PartPose.offsetAndRotation(5F, -34F, -2.5F, 0F, 0F, 0F));
        crestPlanes.addOrReplaceChild("middle", CubeListBuilder.create()
                .texOffs(48, 45).addBox(-2F, -36F, -1.5F, 8F, 5F, 0F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, -1F, 0F, 0F, 0F, 0F));
        var crestHorns = crest.addOrReplaceChild("3D", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, -35F, -2F, 0F, 0F, 0F));
        var rightHorn = crestHorns.addOrReplaceChild("r2", CubeListBuilder.create()
                .texOffs(30, 58).addBox(0F, -1.5F, -1F, 1F, 4F, 2F, new CubeDeformation(0F))
                .texOffs(31, 55).addBox(1F, -3.5F, 1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(-6F, 1.5F, 0F, 0.567232F, 0F, 0F));
        rightHorn.addOrReplaceChild("r2_2", CubeListBuilder.create()
                .texOffs(30, 53).addBox(-1F, -0.5F, -1F, 1F, 3F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(2F, -3F, 0F, 0F, 0F, 0F));
        var leftHorn = crestHorns.addOrReplaceChild("l2", CubeListBuilder.create()
                .texOffs(30, 53).addBox(-0.5F, -3.5F, -1F, 1F, 3F, 2F, new CubeDeformation(0F))
                .texOffs(31, 55).addBox(-0.5F, -3.5F, 1F, 1F, 1F, 1F, new CubeDeformation(0F)), PartPose.offsetAndRotation(4.5F, 1.5F, 0F, 0.567232F, 0F, 0F));
        leftHorn.addOrReplaceChild("l2_2", CubeListBuilder.create()
                .texOffs(30, 58).addBox(-0.5F, -1.5F, -1F, 1F, 4F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(1F, 0F, 0F, 0F, 0F, 0F));
        root.getChild("body").addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(16, 16).addBox(-4F, -24F, -2F, 8F, 12F, 4F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));
        var rightShoulder = root.getChild("right_arm").addOrReplaceChild("right_arm", CubeListBuilder.create().mirror(), PartPose.offsetAndRotation(5F, 22F, 0F, 0F, 0F, 0F));
        var rightSleeve = rightShoulder.addOrReplaceChild("right_arm_sub_0", CubeListBuilder.create()
                .texOffs(40, 16).addBox(-8F, -24F, -2F, 4F, 12F, 4F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        rightSleeve.addOrReplaceChild("calamity_r", CubeListBuilder.create()
                .texOffs(20, 61).addBox(-0.33333F, -4.83333F, -1F, 3F, 1F, 2F, new CubeDeformation(0F))
                .texOffs(21, 56).addBox(-1.58333F, -2.83333F, -1F, 2F, 3F, 2F, new CubeDeformation(0F))
                .texOffs(21, 56).addBox(-1.58333F, 4.16667F, -1F, 2F, 3F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(-7.66667F, -20.16667F, 0F, 0F, 0F, 0F));
        var leftShoulder = root.getChild("left_arm").addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5F, 22F, 0F, 0F, 0F, 0F));
        var leftSleeve = leftShoulder.addOrReplaceChild("left_arm_sub_0", CubeListBuilder.create().mirror()
                .texOffs(40, 16).addBox(4F, -24F, -2F, 4F, 12F, 4F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        var leftDetails = leftSleeve.addOrReplaceChild("calamity_l", CubeListBuilder.create()
                .texOffs(20, 61).addBox(-2.66667F, -4.83333F, -1F, 3F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(7.66667F, -20.16667F, 0F, 0F, 0F, 0F));
        leftDetails.addOrReplaceChild("left_arm_sub_2", CubeListBuilder.create().mirror()
                .texOffs(21, 56).addBox(-0.41667F, -2.83333F, -1F, 2F, 3F, 2F, new CubeDeformation(0F))
                .texOffs(21, 56).addBox(-0.41667F, 4.16667F, -1F, 2F, 3F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        root.getChild("left_leg").addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
                .texOffs(0, 16).addBox(0F, -12F, -2F, 4F, 12F, 4F, new CubeDeformation(0.8F)), PartPose.offsetAndRotation(-2F, 12F, 0F, 0F, 0F, 0F));
        root.getChild("right_leg").addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-4F, -12F, -2F, 4F, 12F, 4F, new CubeDeformation(0.8F)), PartPose.offsetAndRotation(2F, 12F, 0F, 0F, 0F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition mirageLayer() {
        var mesh = skeleton();
        var helmet = mesh.getRoot().getChild("head").addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4F, -31F, -4F, 8F, 8F, 8F, new CubeDeformation(0.8F)), PartPose.offset(0F, 23F, 0F));
        helmet.addOrReplaceChild("layer2", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4F, -4F, -4F, 8F, 8F, 8F, new CubeDeformation(1F)), PartPose.offset(0F, -27F, 0F));
        helmet.addOrReplaceChild("crest", CubeListBuilder.create()
                .texOffs(0, 53).addBox(-1F, -35F, -4F, 2F, 3F, 8F, new CubeDeformation(0F)), PartPose.offset(0F, 1F, 0F));
        var mirage = helmet.addOrReplaceChild("mirage_helmet", CubeListBuilder.create()
                .texOffs(8, 32).addBox(-5.9F, -3.5F, -6.2F, 12F, 3F, 4F, new CubeDeformation(0F))
                .texOffs(8, 45).addBox(-5.9F, -2.5F, -2.2F, 2F, 2F, 4F, new CubeDeformation(0F))
                .texOffs(50, 38).addBox(-1.9F, -7.5F, -6.2F, 4F, 4F, 3F, new CubeDeformation(0F))
                .texOffs(8, 39).addBox(-5.9F, 1.5F, -6.2F, 2F, 2F, 12F, new CubeDeformation(0F))
                .texOffs(25, 39).addBox(-3.9F, 1.5F, 4.8F, 8F, 2F, 1F, new CubeDeformation(0F)), PartPose.offset(-0.1F, -27.5F, 0.2F));
        mirage.addOrReplaceChild("mirrored", CubeListBuilder.create().mirror()
                .texOffs(8, 45).addBox(4.1F, -2.5F, -2.2F, 2F, 2F, 4F, new CubeDeformation(0F))
                .texOffs(8, 39).addBox(4.1F, 1.5F, -6.2F, 2F, 2F, 12F, new CubeDeformation(0F)), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition paleLayer() {
        var mesh = skeleton();
        var root = mesh.getRoot();
        var helmet = root.getChild("head").addOrReplaceChild("head", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-4F, -31F, -4F, 8F, 8F, 8F, new CubeDeformation(0.8F)), PartPose.offsetAndRotation(0F, 23F, 0F, 0F, 0F, 0F));
        helmet.addOrReplaceChild("layer2", CubeListBuilder.create()
                .texOffs(32, 0).addBox(-4F, -4F, -4F, 8F, 8F, 8F, new CubeDeformation(1F)), PartPose.offsetAndRotation(0F, -27F, 0F, 0F, 0F, 0F));
        var horns = helmet.addOrReplaceChild("pale_head", CubeListBuilder.create()
                .texOffs(39, 48).addBox(-9F, -6F, 2F, 2F, 5F, 2F, new CubeDeformation(0F))
                .texOffs(41, 45).addBox(-8F, -8F, 3F, 1F, 2F, 1F, new CubeDeformation(0F))
                .texOffs(41, 45).addBox(-2F, -8F, 3F, 1F, 2F, 1F, new CubeDeformation(0F))
                .texOffs(39, 48).addBox(-2F, -6F, 2F, 2F, 5F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(4.5F, -32F, -3F, -0.698132F, 0F, 0F));
        var faceBones = horns.addOrReplaceChild("bone3", CubeListBuilder.create()
                .texOffs(38, 55).addBox(-8.5F, -4.5F, 0F, 5F, 9F, 0F, new CubeDeformation(0F)), PartPose.offsetAndRotation(-4.5F, -0.5F, 2F, 0F, 0F, 0F));
        faceBones.addOrReplaceChild("head_sub_13", CubeListBuilder.create().mirror()
                .texOffs(38, 55).addBox(3.5F, -4.5F, 0F, 5F, 9F, 0F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        root.getChild("body").addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(16, 16).addBox(-4F, -24F, -2F, 8F, 12F, 4F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));
        var rightShoulder = root.getChild("right_arm").addOrReplaceChild("right_arm", CubeListBuilder.create().mirror(), PartPose.offsetAndRotation(5F, 22F, 0F, 0F, 0F, 0F));
        var rightSleeve = rightShoulder.addOrReplaceChild("right_arm_sub_0", CubeListBuilder.create()
                .texOffs(40, 16).addBox(-8F, -24F, -2F, 4F, 12F, 4F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        var rightBonePanel = rightSleeve.addOrReplaceChild("pale_r", CubeListBuilder.create()
                .texOffs(0, 32).addBox(-2F, -6.5F, 0F, 4F, 13F, 0F, new CubeDeformation(0F)), PartPose.offsetAndRotation(-10F, -17.5F, 0F, 0F, 0F, 0F));
        var rightBoneDetails = rightBonePanel.addOrReplaceChild("cositospale_r", CubeListBuilder.create(), PartPose.offsetAndRotation(10F, 17.5F, 0F, 0F, 0F, 0F));
        rightBoneDetails.addOrReplaceChild("1", CubeListBuilder.create()
                .texOffs(1, 54).addBox(-0.5F, -2.8883F, -0.89715F, 1F, 4F, 1F, new CubeDeformation(0F))
                .texOffs(0, 51).addBox(-0.5F, -2.8883F, -2.89715F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(-7.5F, -24.25776F, 1.22539F, -0.492334F, -0.377597F, -0.602186F));
        rightBoneDetails.addOrReplaceChild("2", CubeListBuilder.create()
                .texOffs(1, 54).addBox(-0.5F, -2.8883F, -0.89715F, 1F, 4F, 1F, new CubeDeformation(0F))
                .texOffs(0, 51).addBox(-0.5F, -2.8883F, -2.89715F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(-8.5F, -21.25776F, 2.22539F, -0.492334F, -0.377597F, -0.602186F));
        var leftShoulder = root.getChild("left_arm").addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-5F, 22F, 0F, 0F, 0F, 0F));
        var leftSleeve = leftShoulder.addOrReplaceChild("left_arm_sub_0", CubeListBuilder.create().mirror()
                .texOffs(40, 16).addBox(4F, -24F, -2F, 4F, 12F, 4F, new CubeDeformation(0.4F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        var leftBonePanel = leftSleeve.addOrReplaceChild("pale_l", CubeListBuilder.create().mirror()
                .texOffs(0, 32).addBox(8F, -24F, 0F, 4F, 13F, 0F, new CubeDeformation(0F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        var leftBoneDetails = leftBonePanel.addOrReplaceChild("cositospale_l", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        leftBoneDetails.addOrReplaceChild("bone", CubeListBuilder.create()
                .texOffs(1, 54).addBox(-0.5F, -2.8883F, -0.89715F, 1F, 4F, 1F, new CubeDeformation(0F))
                .texOffs(0, 51).addBox(-0.5F, -2.8883F, -2.89715F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(7.5F, -24.25776F, 1.22539F, -0.492335F, 0.377597F, 0.602186F));
        leftBoneDetails.addOrReplaceChild("bone2", CubeListBuilder.create()
                .texOffs(1, 54).addBox(-0.5F, -2.8883F, -0.89715F, 1F, 4F, 1F, new CubeDeformation(0F))
                .texOffs(0, 51).addBox(-0.5F, -2.8883F, -2.89715F, 1F, 1F, 2F, new CubeDeformation(0F)), PartPose.offsetAndRotation(8.5F, -21.25776F, 2.22539F, -0.492334F, 0.377597F, 0.602186F));
        root.getChild("left_leg").addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
                .texOffs(0, 16).addBox(0F, -12F, -2F, 4F, 12F, 4F, new CubeDeformation(0.8F)), PartPose.offsetAndRotation(-2F, 12F, 0F, 0F, 0F, 0F));
        root.getChild("right_leg").addOrReplaceChild("right_leg", CubeListBuilder.create()
                .texOffs(0, 16).addBox(-4F, -12F, -2F, 4F, 12F, 4F, new CubeDeformation(0.8F)), PartPose.offsetAndRotation(2F, 12F, 0F, 0F, 0F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    public static LayerDefinition innerLayer() {
        var mesh = skeleton();
        var root = mesh.getRoot();
        root.getChild("head").addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));
        root.getChild("body").addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(40, 48).addBox(-4F, -24F, -2F, 8F, 12F, 4F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0F, 24F, 0F, 0F, 0F, 0F));
        root.getChild("right_arm").addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(6F, 22F, 0F, 0F, 0F, 0F));
        root.getChild("left_arm").addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offsetAndRotation(-6F, 22F, 0F, 0F, 0F, 0F));
        var leftLeg = root.getChild("left_leg").addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(-2F, 12F, 0F, 0F, 0F, 0F));
        leftLeg.addOrReplaceChild("left_leg_sub_1", CubeListBuilder.create()
                .texOffs(24, 48).addBox(0.25F, -12F, -2F, 4F, 12F, 4F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        var rightLeg = root.getChild("right_leg").addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offsetAndRotation(2F, 12F, 0F, 0F, 0F, 0F));
        rightLeg.addOrReplaceChild("right_leg_sub_1", CubeListBuilder.create().mirror()
                .texOffs(24, 48).addBox(-4.25F, -12F, -2F, 4F, 12F, 4F, new CubeDeformation(0.3F)), PartPose.offsetAndRotation(0F, 0F, 0F, 0F, 0F, 0F));
        return LayerDefinition.create(mesh, 64, 64);
    }

    private static MeshDefinition skeleton() {
        var mesh = new MeshDefinition();
        var root = mesh.getRoot();
        var head = root.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
        root.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5, 2, 0));
        root.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5, 2, 0));
        root.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12, 0));
        root.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12, 0));
        return mesh;
    }
}
