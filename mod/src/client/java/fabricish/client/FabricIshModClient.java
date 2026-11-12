package fabricish.client;

import com.mojang.blaze3d.Blaze3D;
import fabricish.server.payload.CrashPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class FabricIshModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ClientPlayNetworking.registerGlobalReceiver(CrashPayload.TYPE, (_, _) -> Blaze3D.youJustLostTheGame());
	}
}
