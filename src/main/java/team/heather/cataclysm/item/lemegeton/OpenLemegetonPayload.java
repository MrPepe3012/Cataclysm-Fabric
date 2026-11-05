package team.heather.cataclysm.item.lemegeton;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import team.heather.cataclysm.Cataclysm;

/** The server reads the held stack itself; the client supplies no levels, costs, tier or item identity. */
public record OpenLemegetonPayload() implements CustomPacketPayload {

    public static final OpenLemegetonPayload INSTANCE = new OpenLemegetonPayload();
    public static final Type<OpenLemegetonPayload> TYPE = new Type<>(Cataclysm.createNamespacedKey("open_lemegeton"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenLemegetonPayload> CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<OpenLemegetonPayload> type() {
        return TYPE;
    }
}
