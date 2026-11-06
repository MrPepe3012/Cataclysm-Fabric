package team.heather.cataclysm.network.payload;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import team.heather.cataclysm.Cataclysm;

/** The server reads the held stack itself; the client supplies no levels, costs, tier or item identity. */
public record OpenLemegetonPayload() implements CustomPacketPayload {

    public static final OpenLemegetonPayload INSTANCE = new OpenLemegetonPayload();

    public static final Type<OpenLemegetonPayload> PAYLOAD_TYPE = new Type<>(Cataclysm.resourceLocation("open_lemegeton"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenLemegetonPayload> PAYLOAD_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public @NotNull Type<OpenLemegetonPayload> type() {
        return PAYLOAD_TYPE;
    }
}
