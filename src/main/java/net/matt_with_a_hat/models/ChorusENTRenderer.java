package net.matt_with_a_hat.models;

import net.matt_with_a_hat.entity.EntityChorusENT;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import software.bernie.geckolib3.renderer.geo.GeoEntityRenderer;

public class ChorusENTRenderer extends GeoEntityRenderer<EntityChorusENT> {
    
    //Last position to make shaking not as jarring
    double lastX, lastY, lastZ;

    public ChorusENTRenderer(EntityRenderDispatcher renderManager)
    {
        super(renderManager, new ChorusENTModel());
        this.shadowRadius = .8f;
        lastX = 0.0;
        lastY = 0.0;
        lastZ = 0.0;
    }

    @Override
    public void render(EntityChorusENT entity, float entityYaw, float partialTicks, MatrixStack stack, VertexConsumerProvider bufferIn, int packedLightIn)
    {
        //Push to the stack and pop at the end, or else scaling the mob scales the bounding box without scaling the actual hit box.
        stack.push();

        //Increase scale
        stack.scale(2f, 2f, 2f);

        //Shaking if shaking and not paused
        if (entity.isMenacing && !MinecraftClient.getInstance().isPaused())
        {

            //Apply shaking
            double newX, newY, newZ;
            newX = Math.max(Math.min(((Math.random() - 0.5) / 25.0 + lastX) / 2.0, 0.025), -0.025);
            newY = Math.max(Math.min(((Math.random() - 0.5) / 25.0 + lastY) / 2.0, 0.025), -0.025);
            newZ = Math.max(Math.min(((Math.random() - 0.5) / 25.0 + lastZ) / 2.0, 0.025), -0.025);
            lastX = newX;
            lastY = newY;
            lastZ = newZ;
            stack.translate(newX, newY, newZ);
        }
        //Render
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);

        //Pop the stack
        stack.pop();
    }

}
