package net.matt_with_a_hat.entity;

import java.util.EnumSet;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
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
        this.goalSelector.add(5, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.goalSelector.add(6, new PullEntityCloserGoal(this, PlayerEntity.class));
        
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.f, 0.2f));
        this.goalSelector.add(7, new WanderAroundGoal(this, 1.f, 1));
        this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public boolean tryAttack(Entity ent)
    {
        if (this.distanceTo(ent) < 2f)
            return super.tryAttack(ent);
        return false;
    }

    @Override
    public void pushAwayFrom(Entity entity) {
        return;
     }

    class PullEntityCloserGoal extends Goal
    {
        MobEntity mob;
        Class<? extends LivingEntity> targetClass;
        LivingEntity targetEntity;
        TargetPredicate targetPredicate;
        int attractionTimeLeft;
        PullEntityCloserGoal(MobEntity mob, Class<? extends LivingEntity> target) {
            this.mob = mob;
            this.targetClass = target;
            this.attractionTimeLeft = 0;
            this.setControls(EnumSet.of(Goal.Control.TARGET));
            this.targetPredicate = (new TargetPredicate()).setBaseMaxDistance(8.0f);
        }

        @Override
        public boolean canStart()
        {
            this.findClosestTarget();
            return this.targetEntity != null && this.targetEntity.isAlive();
        }

        protected void findClosestTarget() {
            if (this.targetClass != PlayerEntity.class && this.targetClass != ServerPlayerEntity.class) {
               this.targetEntity = this.mob.world.getClosestEntityIncludingUngeneratedChunks(this.targetClass, this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ(), this.getSearchBox(8.f));
            } else {
               this.targetEntity = this.mob.world.getClosestPlayer(this.targetPredicate, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
            }
      
         }

        protected Box getSearchBox(double distance) {
            return this.mob.getBoundingBox().expand(distance, 4.0D, distance);
        }

        

        @Override
        public boolean shouldContinue()
        {
            return attractionTimeLeft > 0f && targetEntity != null && targetEntity.isAlive();
        }

        @Override
        public void start()
        {
            this.mob.setTarget(targetEntity);
            attractionTimeLeft = (int)((random.nextFloat() * 3f + 2f) * 20f);
        }

        @Override
        public void tick()
        {
            --attractionTimeLeft;
            if (getTarget() != null)
            {
                Vec3d dirEntityToThis = (this.mob.getPos().subtract(getTarget().getPos())).normalize();
                PacketByteBuf buf = PacketByteBufs.create();
                buf.writeFloat((float)dirEntityToThis.x);
                buf.writeFloat((float)dirEntityToThis.y);
                buf.writeFloat((float)dirEntityToThis.z);
                ServerPlayNetworking.send((ServerPlayerEntity)getTarget(), new Identifier("betterend", "plyvel"), buf);
               getTarget().addVelocity(dirEntityToThis.x * 0.1f, dirEntityToThis.y * 0.1f, dirEntityToThis.z * 0.1f);
            }

        }
    }

}
