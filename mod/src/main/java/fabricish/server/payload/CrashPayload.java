package fabricish.server.payload;

import fabricish.FabricIshMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record CrashPayload() implements CustomPacketPayload {
    public static final CrashPayload INSTANCE = new CrashPayload();
    public static final StreamCodec<ByteBuf, CrashPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);
    public static final Type<CrashPayload> TYPE = new Type<>(FabricIshMod.id("crash_payload"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
