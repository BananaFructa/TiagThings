package BananaFructa.TTIEMultiblocks.Particles;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

@Mod.EventBusSubscriber
public class WindCalculator {

    private final static float windMagnitude = 0.7f;

    private static final int refreshTickRate = 20*10;

    private static int ticksLeft = 0;

    private static Vector3f windDirection = new Vector3f(windMagnitude,0,0);


    public static Vector3f getWindDirection() {
        return windDirection;
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().world == null) return;
        if (ticksLeft == 0) {
            float angle = ((Minecraft.getMinecraft().world.getWorldTime() % 240000) / 239999.0f) * 2 * (float)Math.PI;
            windDirection = new Vector3f(windMagnitude * (float)Math.cos(angle),0,windMagnitude * (float)Math.sin(angle));
            ticksLeft = refreshTickRate;
        }
        ticksLeft--;
    }
}
