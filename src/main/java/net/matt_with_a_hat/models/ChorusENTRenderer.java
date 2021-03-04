package net.matt_with_a_hat.models;

import net.matt_with_a_hat.entity.EntityChorusENT;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class ChorusENTRenderer extends GeoEntityRenderer<EntityChorusENT> {
    
    public ChorusENTRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager, new ChorusENTModel());
        this.shadowRadius = 0.7f;

    }

}
