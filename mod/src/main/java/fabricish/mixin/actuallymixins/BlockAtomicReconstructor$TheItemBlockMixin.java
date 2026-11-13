package fabricish.mixin.actuallymixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fabricish.FabricIshMod;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "de.ellpeck.actuallyadditions.mod.blocks.BlockAtomicReconstructor$TheItemBlock")
public class BlockAtomicReconstructor$TheItemBlockMixin {
    @Unique
    private final ThreadLocal<RandomSource> random = ThreadLocal.withInitial(() -> RandomSource.createThreadLocalInstance(System.currentTimeMillis()));

    private BlockAtomicReconstructor$TheItemBlockMixin() {
    }

    @WrapOperation(
        method = "appendHoverText",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getRandom()Lnet/minecraft/util/RandomSource;")
    )
    private RandomSource replaceTooltipRandomSoC2MEDoesntDie(Level instance, Operation<RandomSource> original) {
        if (FabricIshMod.safety.isSameThread(instance))
            return original.call(instance);
        else return random.get();
    }
}
