package net.matt_with_a_hat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.matt_with_a_hat.entity.EntityChorusENT;
import net.matt_with_a_hat.models.ChorusENTRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

public class BetterEndClient implements ClientModInitializer {
    

    @Override
    public void onInitializeClient()
    {
        EntityRendererRegistry.INSTANCE.register(BetterEnd.CHORUS_ENT, (entityRenderDispatcher, context) -> new ChorusENTRenderer(entityRenderDispatcher));
        
        ClientPlayNetworking.registerGlobalReceiver(new Identifier("betterend", "plyvel"), (client, handler, buf, sender) -> {
            Vec3d vel = new Vec3d(buf.readFloat(), buf.readFloat(), buf.readFloat());
            client.execute(() -> {
                client.player.addVelocity(vel.x * 0.1f, vel.y * 0.1f, vel.z * 0.1f);
            });
        });
        
        ClientPlayNetworking.registerGlobalReceiver(new Identifier("betterend", "ces"), (client, handler, buf, sender) -> {
            int entID = buf.readInt();
            boolean newstate = buf.readBoolean();
            client.execute(() -> {
                for(Entity ent : client.world.getEntities())
                {
                    if (ent.getEntityId() != entID)
                        continue;
                    ((EntityChorusENT)ent).isMenacing = newstate;
                    break;
                }
            });
        });
    }
}
