package team.heather.cataclysm.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import team.heather.cataclysm.Cataclysm;

/** Tells a player how much damage Immortality just cancelled. */
public record SuppressedDamagePayload(float damage) implements CustomPacketPayload {

    public static final Type<SuppressedDamagePayload> PAYLOAD_TYPE = new Type<>(Cataclysm.resourceLocation("suppressed_damage"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SuppressedDamagePayload> PAYLOAD_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT,
        SuppressedDamagePayload::damage,
        SuppressedDamagePayload::new
    );

    @Override
    public @NotNull Type<SuppressedDamagePayload> type() {
        return PAYLOAD_TYPE;
    }
}
