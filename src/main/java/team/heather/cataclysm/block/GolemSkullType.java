package team.heather.cataclysm.block;

import net.minecraft.world.level.block.SkullBlock;
import org.jetbrains.annotations.NotNull;

public enum GolemSkullType implements SkullBlock.Type {

    GOLEM;

    static {
        SkullBlock.Type.TYPES.put("cataclysm:golem", GOLEM);
    }

    @Override
    public @NotNull String getSerializedName() {
        return "cataclysm:golem";
    }
}
