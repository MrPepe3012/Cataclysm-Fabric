package team.heather.cataclysm.client.network.receiver;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import team.heather.cataclysm.client.effect.MobEffectHeartRenderer;
import team.heather.cataclysm.network.payload.SuppressedDamagePayload;

/** What the client does when it receives a payload. */
public interface ClientReceiver {

    static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(SuppressedDamagePayload.PAYLOAD_TYPE, (payload, context) ->
            MobEffectHeartRenderer.showSuppressedDamage(context.player(), payload.damage()));
    }
}
