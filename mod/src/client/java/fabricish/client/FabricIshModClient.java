package fabricish.client;

import com.mojang.blaze3d.Blaze3D;
import fabricish.FabricIshMod;
import fabricish.server.payload.CrashPayload;
import fabricish.utils.MainThreadChecker;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public class FabricIshModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
        ClientPlayNetworking.registerGlobalReceiver(CrashPayload.TYPE, (_, _) -> Blaze3D.youJustLostTheGame());
        var originalSafety = FabricIshMod.safety;
        MainThreadChecker betterSafety = level -> level instanceof ClientLevel && Minecraft.getInstance().isSameThread();
        FabricIshMod.safety = level -> originalSafety.isSameThread(level) || betterSafety.isSameThread(level);
	}
}
