package BananaFructa.ImmersivePetroleum;

import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import flaxbeard.immersivepetroleum.common.Config;
import flaxbeard.immersivepetroleum.common.blocks.metal.TileEntityPumpjack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;

public class ModifiedTileEntityPumpjackParent extends TileEntityPumpjack.TileEntityPumpjackParent {

    RotaryStorage rotaryStorage;

    static {
        Config.IPConfig.Extraction.pumpjack_consumption = 0;
    }

    public ModifiedTileEntityPumpjackParent() {
        Utils.writeDeclaredField(TileEntityMultiblockMetal.class,this,"energyStorage",new FluxStorageAdvanced(0),true);
        rotaryStorage = new RotaryStorage() {
            @Override
            public RotationSide getSide(@Nullable EnumFacing facing) {
                return RotationSide.INPUT;
            }
        };
    }

    @Override
    public void update() {
        super.func_73660_a();
        IEUtils.inputRotaryPower(this,getPosInput(),rotaryStorage);
    }

    @Override
    public boolean canExtract() {
        return rotaryStorage.getOutputTorque() >= getMinTorque() && rotaryStorage.getOutputRotationSpeed() >= getMinSpeed() && rotaryStorage.getOutputRotationSpeed() <= getMaxSpeed();
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setTag("rotary",rotaryStorage.toNBT());
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        rotaryStorage.fromNBT(nbt.getCompoundTag("rotary"));
    }

    public static float getMinTorque() {
        return 25;
    }

    public static float getMinSpeed() {
        return 60;
    }

    public static float getMaxSpeed() {
        return 80;
    }

    public static int getPosInput() {return 20;}
}
