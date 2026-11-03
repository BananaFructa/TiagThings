package BananaFructa.ImmersiveIntelligence;

import BananaFructa.TTIEMultiblocks.Utils.TTEUtils;
import mctmods.immersivetechnology.api.client.MechanicalEnergyAnimation;
import mctmods.immersivetechnology.common.Config;
import mctmods.immersivetechnology.common.shared.interfaces.ITBlockInterfaces;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import pl.pabilo8.immersiveintelligence.common.block.rotary_device.tileentity.TileEntityWheelIron;

public class ModifiedWheelTileEntityIron extends TileEntityWheelIron implements ITBlockInterfaces.IMechanicalEnergy, IRotaryLoadConnector {
    //boolean isFeeding = false;
    BlockPos feedingTo = new BlockPos(0,0,0);

    public ModifiedWheelTileEntityIron() {
        super();
    }

    public ModifiedWheelTileEntityIron(EnumFacing facing) {
        this.facing = facing;
        //this.beltNetwork = (new MotorBeltTorqueFeedbackNetwork()).add(this);
    }

    @Override
    public void update() {
        super.func_73660_a();
        if (world.isRemote) return;
        BlockPos connectedTo = pos.offset(this.facing);
        TileEntity te = world.getTileEntity(connectedTo);
        if (world.getWorldTime() % 20 == 0) TTEUtils.notifyClientUpdate(world,getPos());
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isMechanicalEnergyTransmitter(EnumFacing enumFacing) {
        return true;
    }

    @Override
    public boolean isMechanicalEnergyReceiver(EnumFacing enumFacing) {
        return false;
    }

    @Override
    public int getSpeed() {
        return (int)(Math.pow((this.energy.getOutputRotationSpeed() * this.energy.getOutputTorque()/(60)*2*Math.PI)/ Config.ITConfig.Multiblocks.alternator.alternator_energy_perTick,1.0f/Config.ITConfig.Multiblocks.alternator.alternator_exponent)*Config.ITConfig.Multiblocks.mechanicalEnergy.mechanicalEnergy_speed_max);
    }

    @Override
    public float getTorqueMultiplier() {
        return 1;
    }

    @Override
    public MechanicalEnergyAnimation getAnimation() {
        return null;
    }

    @Override
    public float getLoadTorque() {
        return 0;
    }
}
