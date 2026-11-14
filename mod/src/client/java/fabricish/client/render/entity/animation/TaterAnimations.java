package fabricish.client.render.entity.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

public class TaterAnimations {
    public static final AnimationDefinition USING_KNIFE = AnimationDefinition.Builder.withLength(0.5f)
        .addAnimation("main", new AnimationChannel(
            AnimationChannel.Targets.ROTATION,
            new Keyframe(0, KeyframeAnimations.degreeVec(0,0,0), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.25f, KeyframeAnimations.degreeVec(90,0,0), AnimationChannel.Interpolations.LINEAR),
            new Keyframe(0.5f, KeyframeAnimations.degreeVec(0,0,0), AnimationChannel.Interpolations.LINEAR)
            ))
        .build();
}
