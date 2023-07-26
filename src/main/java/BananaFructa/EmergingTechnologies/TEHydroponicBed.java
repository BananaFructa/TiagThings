package BananaFructa.EmergingTechnologies;

import BananaFructa.TiagThings.TTMain;
import BananaFructa.tfcfarming.CropNutrients;
import BananaFructa.tfcfarming.NutrientClass;
import io.moonman.emergingtechnology.config.EmergingTechnologyConfig;
import io.moonman.emergingtechnology.helpers.PlantHelper;
import io.moonman.emergingtechnology.helpers.machines.HydroponicHelper;
import io.moonman.emergingtechnology.machines.hydroponic.Hydroponic;
import io.moonman.emergingtechnology.machines.hydroponic.HydroponicTileEntity;
import io.moonman.emergingtechnology.machines.light.Light;
import io.moonman.emergingtechnology.machines.light.LightTileEntity;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.util.calendar.CalendarTFC;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.Random;

public class TEHydroponicBed extends HydroponicTileEntity {

    private long lastUpdate = CalendarTFC.PLAYER_TIME.getTicks();

    @Override
    public void validate() {
        super.validate();
        FluidStack stack = fluidHandler.getFluid();
        fluidHandler = new FluidStorageHandlerNutrient(fluidHandler.getCapacity());
        fluidHandler.setFluid(stack);
    }

    @Override
    public boolean doGrowthMultiplierProcess() {
        IBlockState aboveBlockState = this.world.getBlockState(this.pos.add(0, 1, 0));
        if (aboveBlockState == null) {
            return false;
        } else {
            Block aboveBlock = aboveBlockState.getBlock();
            ItemStack growthMedium = this.getItemStack();
            if (!(aboveBlock instanceof BlockCropTFC)) {
                return false;
            } else {

                int totalBoost = 0;
                int growthProbabilityThreshold = HydroponicHelper.getGrowthProbabilityForMedium(growthMedium);
                int growthProbabilityBoostModifierFromFluid = HydroponicHelper.getSpecificPlantGrowthBoostForFluidStack(this.fluidHandler.getFluid(), aboveBlockState);
                totalBoost += growthProbabilityBoostModifierFromFluid;
                totalBoost += growthProbabilityThreshold;

                int totalBulbBoost = getTotalGrowthFromAdjacentLight();

                NutrientClass requiredNutrient = CropNutrients.getCropNValues(((BlockCropTFC)aboveBlock).getCrop()).favouriteNutrient;
                Fluid f = null;
                if (fluidHandler.getFluid() != null) f = fluidHandler.getFluid().getFluid();

                boolean invalidNutrient = f == null || (
                            (requiredNutrient == NutrientClass.NITROGEN && f != TTMain.nitrogenSolution) ||
                            (requiredNutrient == NutrientClass.POTASSIUM && f != TTMain.potassiumSolution) ||
                            (requiredNutrient == NutrientClass.PHOSPHORUS && f != TTMain.phosphorusSolution)
                );

                if (totalBulbBoost == 0 || invalidNutrient || getFluidStack().amount < 1000) {
                    lastUpdate = CalendarTFC.PLAYER_TIME.getTicks();
                    return false;
                }

                totalBoost += totalBulbBoost;

                long growthTicks = (long)((((BlockCropTFC)aboveBlockState.getBlock()).getCrop().getGrowthTicks() / 2.0d) * (1 - totalBoost / 100.0d));

                if (CalendarTFC.PLAYER_TIME.getTicks() - lastUpdate > growthTicks) {
                    lastUpdate = CalendarTFC.PLAYER_TIME.getTicks();
                    ((BlockCropTFC)aboveBlock).grow(this.world,this.pos.up(),aboveBlockState,new Random());
                    fluidHandler.drain(1000,true);
                    return true;
                }
                return false;

            }
        }
    }

    public int getTotalGrowthFromAdjacentLight() {
        for(int i = 1; i < EmergingTechnologyConfig.HYDROPONICS_MODULE.GROWLIGHT.lightBlockRange; ++i) {
            BlockPos pos = this.pos.add(0, i + 2, 0);
            Block aboveBlock = this.world.getBlockState(pos).getBlock();
            if (aboveBlock instanceof Light) {
                TileEntity tileEntity = this.world.getTileEntity(pos);
                if (tileEntity instanceof LightTileEntity) {
                    LightTileEntity lightTileEntity = (LightTileEntity)tileEntity;
                    if (lightTileEntity.energyHandler.getEnergyStored() == 0) return 0;
                    return lightTileEntity.getGrowthProbabilityForBulb();
                }
            } else if (aboveBlock != Blocks.AIR && !(aboveBlock instanceof BlockCropTFC)) {
                return 0;
            }
        }

        return 0;
    }

    @Override
    public void func_145839_a(NBTTagCompound compound) {
        super.func_145839_a(compound);
        lastUpdate = compound.getLong("lastUpdate");
    }

    @Override
    public NBTTagCompound func_189515_b(NBTTagCompound compound) {
        compound.setLong("lastUpdate",lastUpdate);
        return super.func_189515_b(compound);
    }


}
