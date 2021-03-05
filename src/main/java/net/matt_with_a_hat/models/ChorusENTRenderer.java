package net.matt_with_a_hat.models;

import net.matt_with_a_hat.entity.EntityChorusENT;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class ChorusENTRenderer extends GeoEntityRenderer<EntityChorusENT> {
    
    public ChorusENTRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager, new ChorusENTModel());
        this.shadowRadius = 0.1f;
    }

    @Override
    public void render(EntityChorusENT entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn)
    {
        stack.scale(2f, 2f, 2f);
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

}
