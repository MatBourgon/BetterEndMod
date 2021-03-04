package net.matt_with_a_hat.models;

import net.matt_with_a_hat.entity.EntityChorusENT;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ChorusENTModel extends AnimatedGeoModel<EntityChorusENT> {

    @Override
    public Identifier getModelLocation(EntityChorusENT entity)
    {
        return new Identifier("betterend", "geo/chorusent.geo.json");
    }

    @Override
    public Identifier getTextureLocation(EntityChorusENT entity)
    {
        return new Identifier("betterend", "textures/entity/chorusent.png");
    }

    @Override
    public Identifier getAnimationFileLocation(EntityChorusENT entity)
    {
        return new Identifier("betterend", "animations/chorusent.animation.json");
    }
    
}
