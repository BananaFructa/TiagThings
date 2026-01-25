package BananaFructa.EmergingTechnologies;

import BananaFructa.TerraFirmaCraft.TECropBaseHydroponic;
import BananaFructa.TiagThings.Utils;
import io.moonman.emergingtechnology.helpers.PlantHelper;
import io.moonman.emergingtechnology.helpers.StackHelper;
import io.moonman.emergingtechnology.helpers.machines.HarvesterHelper;
import io.moonman.emergingtechnology.machines.harvester.Harvester;
import io.moonman.emergingtechnology.machines.harvester.HarvesterTileEntity;
import net.dries007.tfc.api.capability.player.CapabilityPlayerData;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropSimple;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.items.ItemSeedsTFC;
import net.dries007.tfc.objects.te.TECropBase;
import net.dries007.tfc.util.agriculture.Crop;
import net.dries007.tfc.util.skills.SimpleSkill;
import net.dries007.tfc.util.skills.SkillType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemHandlerHelper;
import org.lwjgl.Sys;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;

public class HarvesterTileEntityModified extends HarvesterTileEntity {

    public boolean harvestPartial = false;

    static Method sufficientEnergy = Utils.getDeclaredMethod(HarvesterTileEntity.class,"sufficientEnergy");
    static Method outputsFull = Utils.getDeclaredMethod(HarvesterTileEntity.class,"outputsFull");
    static Method setEnergy = Utils.getDeclaredMethod(HarvesterTileEntity.class,"setEnergy",int.class);
    static Method animateHarvest = Utils.getDeclaredMethod(HarvesterTileEntity.class,"animateHarvest");
    static Method getTargetBlockState = Utils.getDeclaredMethod(HarvesterTileEntity.class,"getTargetBlockState",EnumFacing.class);
    static Method getTarget = Utils.getDeclaredMethod(HarvesterTileEntity.class,"getTarget",EnumFacing.class);
    static Method getFacing = Utils.getDeclaredMethod(HarvesterTileEntity.class,"getFacing");
    static Method useEnergy = Utils.getDeclaredMethod(HarvesterTileEntity.class,"useEnergy");
    static Method pullItems = Utils.getDeclaredMethod(HarvesterTileEntity.class,"pullItems",EnumFacing.class);

    static Method tryInsertItemStackIntoOutput = Utils.getDeclaredMethod(HarvesterTileEntity.class,"tryInsertItemStackIntoOutput",ItemStack.class);

    public void cycle() {
        try {
            setEnergy.invoke(this, this.getEnergy());
            EnumFacing[] var1 = EnumFacing.HORIZONTALS;
            int var2 = var1.length;

            for (int var3 = 0; var3 < var2; ++var3) {
                EnumFacing facing = var1[var3];
                this.tryPlant(facing);
                if (this.canHarvest(facing)) {
                    Harvester.rotateFacing(facing, this.world, this.pos);
                    animateHarvest.invoke(this);
                }
            }


            this.doEnergyTransferProcess();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean canHarvest(EnumFacing facing) {
        try {
            if (!(boolean)sufficientEnergy.invoke(this)) {
                return false;
            } else if ((boolean)outputsFull.invoke(this)) {
                return false;
            } else {
                IBlockState state = this.getWorld().getBlockState(this.getPos().offset(facing));
                Block b = state.getBlock();
                if (b instanceof BlockCropTFC) {
                    BlockCropTFC cropTFC = (BlockCropTFC) b;
                    if (harvestPartial && cropTFC instanceof BlockCropSimple) {
                        BlockCropSimple simple = (BlockCropSimple) cropTFC;
                        ItemStack foodDrop = simple.getCrop().getFoodDrop((Integer)state.getValue(simple.getStageProperty()));
                        return !foodDrop.isEmpty();
                    }
                    return cropTFC.getCrop().getMaxStage() == (Integer) this.getWorld().getBlockState(this.getPos().offset(facing)).getValue(cropTFC.getStageProperty());
                }
                return false;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void tryPlant(EnumFacing facing) {
        try {
            if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
                IBlockState state = (IBlockState)getTargetBlockState.invoke(this,facing);
                if (!HarvesterHelper.isInteractableCrop(state.getBlock())) {
                    ItemStack inputStack = this.getInputStack();
                    if (!StackHelper.isItemStackEmpty(inputStack)) {
                        if (state != null) {
                            if (state.getBlock() == Blocks.AIR) {
                                BlockPos soilTarget = ((BlockPos)getTarget.invoke(this,facing)).add(0, -1, 0);
                                if (PlantHelper.isValidSoil(this.getWorld(), soilTarget)) {
                                    IBlockState blockStateToPlace = PlantHelper.getBlockStateFromItemStackForPlanting(inputStack, this.getWorld(), (BlockPos)getTarget.invoke(this,facing));
                                    if (blockStateToPlace != null) {
                                        this.world.setBlockState((BlockPos)getTarget.invoke(this,facing), blockStateToPlace, 3);
                                        // all this shabang for just this one line
                                        this.world.setTileEntity((BlockPos)getTarget.invoke(this,facing),new TECropBaseHydroponic());
                                        this.itemHandler.extractItem(0, 1, false);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void doHarvest() {
        try {
            EnumFacing facing = (EnumFacing) getFacing.invoke(this);
            if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
                if (this.canHarvest(facing)) {
                    if (getTargetBlockState.invoke(this,facing) != null) {
                        if ((boolean)sufficientEnergy.invoke(this)) {
                            IBlockState state = (IBlockState)getTargetBlockState.invoke(this,facing);
                            Block b = state.getBlock();
                            if (b instanceof BlockCropSimple && (boolean)Utils.readDeclaredField(BlockCropSimple.class,b,"isPickable")) {
                                BlockCropSimple cropSimple = (BlockCropSimple)b;
                                ItemStack foodDrop = cropSimple.getCrop().getFoodDrop((Integer)state.getValue(cropSimple.getStageProperty()));
                                if (!world.isRemote && !foodDrop.isEmpty()) {
                                    BlockPos pos = (BlockPos) getTarget.invoke(this, facing);
                                    world.setBlockState(pos, state.withProperty(cropSimple.getStageProperty(), (Integer)state.getValue(cropSimple.getStageProperty()) - 3));
                                    ItemStack itemStack = (ItemStack) tryInsertItemStackIntoOutput.invoke(this,foodDrop);
                                    if (itemStack != null) world.spawnEntity(new EntityItem(world,pos.getX(),pos.getY(),pos.getZ(),itemStack));
                                }
                            } else {
                                this.world.destroyBlock((BlockPos) getTarget.invoke(this, facing), true);
                                pullItems.invoke(this, facing);
                            }

                            useEnergy.invoke(this);
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void togglePartial() {
        harvestPartial ^= true;
        markDirty();
    }

    public void func_145839_a(NBTTagCompound compound) {
        super.func_145839_a(compound);
        harvestPartial = compound.getBoolean("partial");
    }

    public NBTTagCompound func_189515_b(NBTTagCompound compound) {
        compound.setBoolean("partial",harvestPartial);
        return super.func_189515_b(compound);
    }

}
