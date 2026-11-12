package fabricish.server.payload;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class FabricIshPayloads {
    public static void register() {
        PayloadTypeRegistry.clientboundPlay().register(CrashPayload.TYPE, CrashPayload.STREAM_CODEC);
    }
}
