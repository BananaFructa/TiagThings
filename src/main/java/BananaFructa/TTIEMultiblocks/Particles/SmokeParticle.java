package BananaFructa.TTIEMultiblocks.Particles;

import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

public class SmokeParticle extends Particle {
    private Vector3d initialSpeed;
    private double initialSpeedFactor = 1;
    private double windSpeedFactor = 1;

    protected SmokeParticle(World worldIn, double posXIn, double posYIn, double posZIn) {
        super(worldIn, posXIn, posYIn, posZIn);
    }

    public SmokeParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn,double xSpeedIn, double ySpeedIn, double zSpeedIn, int age,float scale) {
        this(worldIn, xCoordIn, yCoordIn, zCoordIn);
        initialSpeed = new Vector3d(xSpeedIn,ySpeedIn,zSpeedIn);
        this.particleMaxAge = age;
        this.canCollide = true;
        this.setParticleTextureIndex(7);
        this.particleRed = 0F;
        this.particleGreen = 0F;
        this.particleBlue = 0F;
        this.particleScale = scale;
    }

    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setExpired();
        }

        //initialSpeedFactor *=0.995;
        if(particleAge%2 == 0){
            initialSpeedFactor *= 0.995;
        }
        if (particleAge%4==0) {
            windSpeedFactor *= 0.995;
        }

        Vector3f windSpeed = WindCalculator.getWindDirection();

        this.motionX = initialSpeedFactor * initialSpeed.x + (1-windSpeedFactor)*windSpeed.x;
        this.motionY = initialSpeedFactor * initialSpeed.y + (1-windSpeedFactor)*windSpeed.y;
        this.motionZ = initialSpeedFactor * initialSpeed.z+ (1-windSpeedFactor)*windSpeed.z;

        this.move(this.motionX, this.motionY, this.motionZ);
    }
}
