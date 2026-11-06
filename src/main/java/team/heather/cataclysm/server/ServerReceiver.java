package team.heather.cataclysm.server;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import team.heather.cataclysm.item.feature.lemegeton.LemegetonProcessor;
import team.heather.cataclysm.network.payload.OpenLemegetonPayload;

/** What the server does when it receives a payload. */
public interface ServerReceiver {

    static void initialize() {
        ServerPlayNetworking.registerGlobalReceiver(OpenLemegetonPayload.PAYLOAD_TYPE, (payload, context) ->
            LemegetonProcessor.open(context.player(), CataclysmServer::getPhase));
    }
}
