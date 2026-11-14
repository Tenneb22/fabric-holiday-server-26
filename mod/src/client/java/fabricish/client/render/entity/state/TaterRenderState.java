package fabricish.client.render.entity.state;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.AnimationState;

public class TaterRenderState extends LivingEntityRenderState {
    public boolean isAngry;
    public final AnimationState usingKnife = new AnimationState();
}
