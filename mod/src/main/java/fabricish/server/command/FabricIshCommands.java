package fabricish.server.command;

import fabricish.server.payload.CrashPayload;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class FabricIshCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register(CrashCommand::register);
    }
}
