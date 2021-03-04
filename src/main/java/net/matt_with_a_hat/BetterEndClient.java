package net.matt_with_a_hat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.matt_with_a_hat.models.ChorusENTRenderer;

public class BetterEndClient implements ClientModInitializer {
    

    @Override
    public void onInitializeClient()
    {
        EntityRendererRegistry.INSTANCE.register(BetterEnd.CHORUS_ENT, (entityRenderDispatcher, context) -> new ChorusENTRenderer(entityRenderDispatcher));
    }
}
