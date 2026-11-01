package team.heather.cataclysm.registry.block;

import net.minecraft.world.level.block.SkullBlock;

public enum GolemSkullType implements SkullBlock.Type {
    GOLEM;

    static {
        SkullBlock.Type.TYPES.put("cataclysm:golem", GOLEM);
    }

    @Override
    public String getSerializedName() {
        return "cataclysm:golem";
    }
}
