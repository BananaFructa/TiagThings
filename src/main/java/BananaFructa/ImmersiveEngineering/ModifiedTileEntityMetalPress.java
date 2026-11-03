package BananaFructa.ImmersiveEngineering;

import BananaFructa.TTIEMultiblocks.Utils.TTEUtils;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.api.crafting.MetalPressRecipe;
import blusunrize.immersiveengineering.api.crafting.MultiblockRecipe;
import blusunrize.immersiveengineering.api.energy.immersiveflux.FluxStorageAdvanced;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMetalPress;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nullable;

public class ModifiedTileEntityMetalPress extends TileEntityMetalPress {

    private RotaryStorage rotaryStorage;

    static {
        for (MetalPressRecipe recipe : MetalPressRecipe.recipeList.values()) {
            Utils.writeDeclaredField(MultiblockRecipe.class,recipe,"totalProcessEnergy",0,false);
        }
    }

    public ModifiedTileEntityMetalPress() {
        super();
        rotaryStorage = new RotaryStorage() {
            @Override
            public RotationSide getSide(@Nullable EnumFacing facing) {
                return RotationSide.INPUT;
            }
        };
        Utils.writeDeclaredField(TileEntityMultiblockMetal.class,this,"energyStorage",new FluxStorageAdvanced(0),true);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setTag("rotary",rotaryStorage.serializeNBT());
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        rotaryStorage.deserializeNBT(nbt.getCompoundTag("rotary"));
    }

    @Override
    public void update() {
        super.func_73660_a();
        if (isDummy()) return;
        TTEUtils.inputRotaryPower(this,getPosInput(),getMinTorque(),rotaryStorage);
    }

    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess<MetalPressRecipe> process) {
        return rotaryStorage.getOutputTorque() >= getMinTorque() && rotaryStorage.getOutputRotationSpeed() >= getMinSpeed() && rotaryStorage.getOutputRotationSpeed() <= getMaxSpeed();
    }

    public static float getMinTorque() {
        return 10;
    }

    public static float getMinSpeed() {
        return 30;
    }

    public static float getMaxSpeed() {
        return 60;
    }

    public static int getPosInput() {return 7;}
}
