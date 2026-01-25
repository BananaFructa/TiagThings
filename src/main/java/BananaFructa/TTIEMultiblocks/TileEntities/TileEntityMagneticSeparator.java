package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_2;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.*;
import BananaFructa.TiagThings.RockTraces;
import BananaFructa.TiagThings.Utils;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityMagneticSeparator extends AnimatedOBJTileEntity<TileEntityMagneticSeparator, SimplifiedMultiblockRecipe> {

    public float inSpeed = 0;
    public float inTorque = 0;

    public static final List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(
                new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/hematite>",32)},
                new FluidStack[]{new FluidStack(FluidsTFC.FRESH_WATER.get(),10)},
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rough_iron_powder>")},
                new FluidStack[0],
                0,20*10,true
                ));
        add(new SimplifiedMultiblockRecipe(
                new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/limonite>",32)},
                new FluidStack[]{new FluidStack(FluidsTFC.FRESH_WATER.get(),10)},
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rough_iron_powder>")},
                new FluidStack[0],
                0,20*10,true
        ));
        add(new SimplifiedMultiblockRecipe(
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:magnetite_powder>",32)},
                new FluidStack[]{new FluidStack(FluidsTFC.FRESH_WATER.get(),10)},
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rough_iron_powder>")},
                new FluidStack[0],
                0,20*10, true
        ));
        ItemStack metalTrace = Utils.itemStackFromCTId("<tiagthings:crushed_rock>",64);
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("trace", RockTraces.IRON.ordinal());
        metalTrace.setTagCompound(tag);
        add(new SimplifiedMultiblockRecipe(
                new ItemStack[]{metalTrace},
                new FluidStack[]{new FluidStack(FluidsTFC.FRESH_WATER.get(),10)},
                new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:rough_iron_powder>")},
                new FluidStack[0],
                0,20*10, true
        ));
    }};

    public TileEntityMagneticSeparator() {
        super(TTIEContent.magneticSeparator, 0, false,recipes,TTIEContent.ttBlockMetalMultiblock_2.getStateFromMeta(TTBlockTypes_MetalMultiblock_2.MAGNETIC_SEPARATOR.getMeta()));
        setAnimation("mag_sep_rotate");
        setAnimationSpeed(0);
    }

    @Override
    public void initPorts() {
        int rotaryIn = registerRotaryStorage();
        registerRotaryPort(2,rotaryIn,PortType.INPUT,EnumFacing.SOUTH);
        int num = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        int out = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(1,num,PortType.INPUT,EnumFacing.SOUTH);
        int tank = registerFluidTank(100);
        registerFluidPort(6,tank,PortType.INPUT,EnumFacing.NORTH);
        registerItemPort(5,out,PortType.OUTPUT,EnumFacing.NORTH);
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote) return;
        if (rotaryEnergies.size() > 0) {
            IRotaryEnergy re = rotaryEnergies.get(0);
            if (re != null) {
                if (re.getRotationSpeed() != inSpeed || inTorque != re.getOutputTorque()) IEUtils.notifyClientUpdate(world, pos);
                if (re.getRotationSpeed() > 0) {
                    inTorque = re.getOutputTorque();
                }
                else {
                    inTorque = 0;
                }
                inSpeed = re.getRotationSpeed();
                setAnimationSpeed(inSpeed/40.0f);
            } else {
                IEUtils.notifyClientUpdate(world, pos);
                inSpeed = 0;
                inTorque = 0;
            }
        }
        super.update();
    }

    /*@Override
    protected int registerItemHandler(int slots, boolean[] canInsert, boolean[] canExtract) {
        STEMMInventoryHandler handler = new STEMMInventoryHandler(slots,this,slotCounter,canInsert,canExtract) {
            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {

                ItemStack is = stack.copy();
                int mod = is.getCount() % 32;
                is.shrink(mod);
                ItemStack returned = super.insertItem(slot, is, simulate);
                return new ItemStack(stack.getItem(),returned.getCount() + mod);
            }
        };
        slotCounter += slots;
        inventoryHandlers.add(handler);
        return inventoryHandlers.size() - 1;
    }*/

    public float progress() {
        if (this.processQueue.isEmpty()) return 0;
        return (float)this.processQueue.get(0).processTick/this.processQueue.get(0).maxTicks;
    }

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        if (rotaryEnergies.size() < 1) return false;
        IRotaryEnergy energy = rotaryEnergies.get(0);
        if (energy.getOutputTorque() > 5 && energy.getOutputRotationSpeed() >= 20 && energy.getOutputRotationSpeed() <= 40) return true;
        return false;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        super.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setFloat("inTorque",inTorque);
        nbtTagCompound.setFloat("inSpeed",inSpeed);
        nbtTagCompound.setString("animationName",currentAnimationName);
        nbtTagCompound.setFloat("animationSpeed",speed);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        inTorque = pkt.getNbtCompound().getFloat("inTorque");
        inSpeed = pkt.getNbtCompound().getFloat("inSpeed");
        currentAnimationName = pkt.getNbtCompound().getString("animationName");
        speed = pkt.getNbtCompound().getFloat("animationSpeed");
    }
}
