package net.matt_with_a_hat.entity;

import org.apache.commons.lang3.ThreadUtils;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder.Living;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class EntityChorusENT extends HostileEntity implements IAnimatable {
    
    private AnimationFactory factory = new AnimationFactory(this);

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event)
    {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.ChorusENT.new", true));
        this.ignoreCameraFrustum = true;
        return PlayState.CONTINUE;
    }

    @Override
    protected int getCurrentExperience(PlayerEntity player)
    {
        return 7;
    }

    public EntityChorusENT(EntityType<? extends EntityChorusENT> type, World world)
    {
        super(type, world);
        //this.stepHeight = 1f;
    }

    @Override
    public double getHeightOffset() {
        return 2.0;
    }

    @Override
    public AnimationFactory getFactory()
    {
        return factory;
    }

    @Override
    public void registerControllers(AnimationData data)
    {
        data.addAnimationController(new AnimationController<IAnimatable>(this, "controller", 0, this::predicate));
    }


    @Override
    protected void initGoals()
    {
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.f));
        //this.goalSelector.add(8, new LookAroundGoal(this));
        
        //this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.f, 0.2f));
        //this.goalSelector.add(7, new WanderAroundGoal(this, 1.f, 1));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void onPlayerCollision(PlayerEntity player)
    {
        if (!isDead())
            player.damage(DamageSource.mob(this), 2);
    }

}
