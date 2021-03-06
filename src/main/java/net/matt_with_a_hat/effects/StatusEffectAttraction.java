package net.matt_with_a_hat.effects;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.math.Vec3d;

public class StatusEffectAttraction extends StatusEffect {

    public StatusEffectAttraction()
    {
        super(StatusEffectType.BENEFICIAL, 0xE16FEE);
    }

    private boolean isValidEntity(Entity entity)
    {
        return (entity instanceof ExperienceOrbEntity || entity instanceof ItemEntity);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier)
    {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        List<? extends Entity> items = entity.world.getEntitiesByType(null, entity.getBoundingBox().expand(3f + amplifier * 2f, 1f + amplifier * 1f, 3f + amplifier * 2f), this::isValidEntity);

        for(Entity ent : items)
        {
            Vec3d vel = entity.getPos().subtract(ent.getPos()).normalize();
            ent.addVelocity(vel.x * 0.1F, vel.y * 0.1f, vel.z * 0.1f);
        }
    }
}
