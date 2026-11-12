package fabricish.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import fabricish.server.payload.CrashPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.gameevent.GameEvent;

import java.lang.foreign.MemorySegment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CrashCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext buildContext, Commands.CommandSelection selection) {
        dispatcher.register(Commands.literal("crash").executes(c -> crash(c.getSource())).then(Commands.literal("vote").then(Commands.argument("victim", EntityArgument.player()).executes(CrashCommand::crashVote))).then(Commands.literal("unvote").then(Commands.argument("victim", EntityArgument.player()).executes(CrashCommand::crashUnvote))));
    }

    public static final Map<UUID, Set<UUID>> VOTES = new ConcurrentHashMap<>(); // performance? for real?

    private static int crash(CommandSourceStack source) {
        if (source.getEntity() == null) {
            MemorySegment.ofAddress(0).reinterpret(1000).setString(0, "get crashed");
            return 0; // we are never getting here
        } else if (source.getPlayer() instanceof ServerPlayer player) {
            ServerPlayNetworking.send(player, CrashPayload.INSTANCE);
            return 0;
        }
        return -1;
    }

    private static int crashVote(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer voterPlayer = source.getPlayerOrException();
        UUID voter = voterPlayer.getUUID();
        ServerPlayer victimPlayer = EntityArgument.getPlayer(ctx, "victim");
        UUID victim = victimPlayer.getUUID();
        Set<UUID> judges = VOTES.computeIfAbsent(voter, _ -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
        if (!judges.contains(victim)) {
            judges.add(victim);
            String progress = " (" + judges.size() + "/" + ((source.getServer().getPlayerCount() + 1) / 2) + ")";
            source.getServer().getPlayerList().broadcastSystemMessage(Component.literal("").append(voterPlayer.getName()).append(" has voted to crash ").append(victimPlayer.getName()).append(Component.literal(progress).withStyle(ChatFormatting.GRAY)), false);
            return 0;
        }
        source.sendFailure(Component.literal("You have already voted for ").append(victimPlayer.getName()));
        return 1;
    }

    private static int crashUnvote(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        ServerPlayer voterPlayer = source.getPlayerOrException();
        UUID voter = voterPlayer.getUUID();
        ServerPlayer victimPlayer = EntityArgument.getPlayer(ctx, "victim");
        UUID victim = victimPlayer.getUUID();
        Set<UUID> judges = VOTES.computeIfAbsent(voter, _ -> Collections.newSetFromMap(new ConcurrentHashMap<>()));
        if (judges.contains(victim)) {
            judges.remove(victim);
            String progress = " (" + judges.size() + "/" + ((source.getServer().getPlayerCount() + 1) / 2) + ")";
            source.getServer().getPlayerList().broadcastSystemMessage(Component.literal("").append(voterPlayer.getName()).append(" has removed their vote to crash ").append(victimPlayer.getName()).append(Component.literal(progress).withStyle(ChatFormatting.GRAY)), false);
            return 0;
        }
        source.sendFailure(Component.literal("You have not voted for ").append(victimPlayer.getName()));
        return 1;
    }

    private static boolean byeBye(int amount, int max) {
        return amount > 0 && amount >= ((max + 1) / 2);
    }

    public static void tickServer(MinecraftServer server) {
        List<Runnable> defer = new ArrayList<>();
        PlayerList playerList = server.getPlayerList();
        int playerCount = playerList.getPlayerCount();
        VOTES.forEach((uuid, judges) -> {
            if (!(playerList.getPlayer(uuid) instanceof ServerPlayer victim)) defer.add(() -> VOTES.remove(uuid));
            else {
                List<UUID> judgesButALittleMoreSafe = List.copyOf(judges);
                int i = 0;
                for (UUID judge : judgesButALittleMoreSafe) {
                    if (playerList.getPlayer(uuid) == null) defer.add(() -> judges.remove(judge));
                    else i++;
                }
                if (byeBye(i, playerCount)) {
                    defer.add(() -> VOTES.remove(uuid));
                    defer.add(() -> {
                        playerList.broadcastSystemMessage(Component.literal("The people have spoken, ").append(victim.getName()).append(" has been sent to touch grass!"), false);
                        victim.gameEvent(GameEvent.LIGHTNING_STRIKE);
                        ServerPlayNetworking.send(victim, CrashPayload.INSTANCE);
                    });
                }
            }
        });
        defer.forEach(Runnable::run);
    }
}
