package team.heather.cataclysm.network.payload;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public interface CataclysmPayloads {

    static void initialize() {
        PayloadTypeRegistry.playC2S().register(OpenLemegetonPayload.PAYLOAD_TYPE, OpenLemegetonPayload.PAYLOAD_CODEC);
        PayloadTypeRegistry.playS2C().register(SuppressedDamagePayload.PAYLOAD_TYPE, SuppressedDamagePayload.PAYLOAD_CODEC);
    }
}
