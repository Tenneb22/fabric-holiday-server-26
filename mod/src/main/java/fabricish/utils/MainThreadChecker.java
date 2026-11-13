package fabricish.utils;

import net.minecraft.world.level.Level;

public interface MainThreadChecker {
    boolean isSameThread(Level level);
}
