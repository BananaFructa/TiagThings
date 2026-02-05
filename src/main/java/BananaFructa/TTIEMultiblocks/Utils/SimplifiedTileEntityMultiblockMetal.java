package BananaFructa.TTIEMultiblocks.Utils;

//import BananaFructa.TTIEMultiblocks.TileEntities.TileEntityAE2CompatMultiblock;
import BananaFructa.TTIEMultiblocks.IECopy.BlockTTMultiblock;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import com.sun.jna.platform.win32.WinUser;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import org.lwjgl.Sys;
import pl.pabilo8.immersiveintelligence.api.rotary.CapabilityRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;
import pl.pabilo8.immersiveintelligence.api.rotary.RotaryStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public abstract class SimplifiedTileEntityMultiblockMetal<M extends SimplifiedTileEntityMultiblockMetal<M,R> ,R extends SimplifiedMultiblockRecipe> extends TileEntityMultiblockMetal<M,R> {

    public List<R> recipes;
    public List<Integer> energyPorts = new ArrayList<>(); //

    public List<Integer> redstonePorts = new ArrayList<>();//
    public List<STEMMFluidTank> tanks = new ArrayList<>();//
    public HashMap<Integer,List<PortInfo>> fluidPorts = new LinkedHashMap<>();//

    public List<STEMMInventoryHandler> inventoryHandlers = new ArrayList<>();//
    public HashMap<Integer,List<PortInfo>> itemPorts = new LinkedHashMap<>();//

    public NonNullList<ItemStack> inventory = NonNullList.withSize(20,ItemStack.EMPTY);//

    public List<RotaryStorage> rotaryEnergies = new ArrayList<>();
    public HashMap<Integer,List<PortInfo>> rotaryPorts = new HashMap<>();

    int itemCounter = 0; // used when a recipes finishes to cycle through the different output ports
    int fluidCounter = 0;

    public int slotCounter = 0;

    //EnumFacing face = EnumFacing.NORTH;//

    public SimplifiedTileEntityMultiblockMetal(SimplifiedMultiblockClass instance, int energyStorage, boolean redstoneControl, List<R> recipes) {
        super(instance,instance.size,energyStorage,redstoneControl);
        this.recipes = recipes;
    }

    public abstract void initPorts();

    public boolean canDoRecipe(R recipe) {
        return true;
    }

    public void onRecipeTick(R recipe) {}

    public boolean isCurrentlyDoingRecipe(R recipe) {
        if (isRSDisabled()) return false;
        for (MultiblockProcess<R> process : this.processQueue) {
            if (process.recipe == recipe && process.canProcess(this)) return true;
        }
        return false;
    }

    public MultiblockProcess<R> getProcessForRecipe(R recipe) {
        if (isRSDisabled()) return null;
        for (MultiblockProcess<R> process : this.processQueue) {
            if (process.recipe == recipe && process.canProcess(this)) return process;
        }
        return null;
    }

    public boolean isWorking() {
        if (isRSDisabled()) return false;
        for (MultiblockProcess<R> process : this.processQueue) {
            if (process.canProcess(this)) return true;
        }
        return false;
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 1;
    }

    public void setRecipeList(List<R> recipes) {
        this.recipes = recipes;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setFace(this.facing);
    }

    public void setFace(EnumFacing face) {
       // this.face = face;
        if (!isDummy()) {
            for (Integer port : itemPorts.keySet()) {
                for (int i = 0;i < itemPorts.get(port).size();i++) {
                    PortInfo info = itemPorts.get(port).get(i);
                    if (info.face != null) {
                        info.face = shiftRelativeToNorth(info.face);
                    }
                }
            }
            for (Integer port : fluidPorts.keySet()) {
                for (int i = 0;i < fluidPorts.get(port).size();i++) {
                    PortInfo info = fluidPorts.get(port).get(i);
                    if (info.face != null) {
                        info.face = shiftRelativeToNorth(info.face);
                    }
                }
            }

            for (Integer port : rotaryPorts.keySet()) {
                for (int i = 0;i < rotaryPorts.get(port).size();i++) {
                    PortInfo info = rotaryPorts.get(port).get(i);
                    if (info.face != null) {
                        info.face = shiftRelativeToNorth(info.face);
                    }
                }
            }
        }
    }

    protected int addEnergyPort(int pos) {
        energyPorts.add(pos);
        return energyPorts.size() - 1;
    }

    protected int registerRotaryStorage() {
        rotaryEnergies.add(new RotaryStorage());
        return rotaryEnergies.size() - 1;
    }

    protected void registerRotaryPort(int pos, int rotaryStorage, PortType portType, EnumFacing northRelative) {
        if (rotaryPorts.containsKey(pos)) {
            rotaryPorts.get(pos).add(new PortInfo(portType,northRelative,rotaryStorage,pos));
        } else {
            rotaryPorts.put(pos,new ArrayList<PortInfo>(){{add(new PortInfo(portType,northRelative,rotaryStorage,pos));}});
        }
    }

    protected void addRedstonePorts(int pos) {
        redstonePorts.add(pos);
    }

    protected int registerFluidTank(int capacity) {
        tanks.add(new STEMMFluidTank(capacity,this));
        return tanks.size() - 1;
    }

    protected int registerFluidTank(int capacity,int ihi,int iho) {
        STEMMFluidTank stemmFluidTank = new STEMMFluidTank(capacity,this);
        stemmFluidTank.setFluidIHInput(ihi);
        stemmFluidTank.setFluidOHInput(iho);
        tanks.add(stemmFluidTank);
        inventoryHandlers.get(ihi).setFluidOnly(true);
        inventoryHandlers.get(iho).setFluidOnly(true);
        return tanks.size() - 1;
    }

    protected void registerFluidPort(int pos, int tank, PortType portType,EnumFacing northRelativeFacing) {
        if (fluidPorts.containsKey(pos)) {
            fluidPorts.get(pos).add(new PortInfo(portType, northRelativeFacing, tank,pos));
        } else {
            fluidPorts.put(pos,new ArrayList<PortInfo>(){{add(new PortInfo(portType, northRelativeFacing, tank,pos));}});
        }
        if (tank != -1 && portType == PortType.INPUT) {
            int id = 0;
            for (Integer p : fluidPorts.keySet()) {
                for (int i = 0;i < fluidPorts.get(p).size();i++) {
                    if (fluidPorts.get(p).get(i).type == PortType.INPUT) id++;
                }
            }
            tanks.get(tank).addRecipeFlowCorelation(id-1);
        }
    }

    protected int registerItemHandler(int slots,boolean canInsert[],boolean[] canExtract) { // creates sub set of inventory
        STEMMInventoryHandler handler = new STEMMInventoryHandler(slots,this,slotCounter,canInsert,canExtract);
        slotCounter += slots;
        inventoryHandlers.add(handler);
        //for(int i = 0;i < handler.getSlots();i++) master().inventory.add(ItemStack.EMPTY);
        return inventoryHandlers.size() - 1;
    }

    int corelationOffset = 0;

    protected void registerItemPort(int pos, int handler, PortType type, EnumFacing northRelativeFacing) {
        if (itemPorts.containsKey(pos)) {
            itemPorts.get(pos).add(new PortInfo(type, northRelativeFacing, handler,pos));
        } else {
            itemPorts.put(pos,new ArrayList<PortInfo>(){{add(new PortInfo(type, northRelativeFacing, handler,pos));}});
        }
        if (handler != -1 && type == PortType.INPUT) {
            int id = 0;
            for (Integer p : itemPorts.keySet()) {
                for (int i = 0; i < itemPorts.get(p).size(); i++) {
                    if (itemPorts.get(p).get(i).type == type) id++;
                }
            }
            //inventoryHandlers.get(handler).addRecipeFlowCorelation(id - 1 + corelationOffset);
            int extraNextCorelation = inventoryHandlers.get(handler).getSlots();

            for (int i = 0; i < extraNextCorelation; i++) {
                inventoryHandlers.get(handler).addRecipeFlowCorelation(id - 1 + corelationOffset + i);
            }
            corelationOffset += extraNextCorelation - 1;

        }
    }

    EnumFacing shiftRelativeToNorth(EnumFacing face) {
        return IEUtils.shiftRelativeToNorth(this.facing,mirrored,face);
        /*if (face == EnumFacing.UP || face == EnumFacing.DOWN) return face;
        switch (this.facing) {
            case NORTH:
                if (!mirrored && (face == EnumFacing.WEST || face == EnumFacing.EAST)) return face.getOpposite();
                return face;
            case SOUTH:
                if (!mirrored && (face == EnumFacing.WEST || face == EnumFacing.EAST)) return face;
                return face.getOpposite();
            case EAST:
                if (!mirrored && (face == EnumFacing.WEST || face == EnumFacing.EAST)) return face.rotateY().getOpposite();
                return face.rotateY();
            case WEST:
                if (!mirrored && (face == EnumFacing.WEST || face == EnumFacing.EAST)) return face.rotateY();
                return face.rotateY().getOpposite();
        }
        return face;*/
    }

    private void resetPortCounters() {
        itemCounter = 0;
        fluidCounter = 0;
    }

    public PortInfo getNextItemPort(PortType type) {
        int found = 0;
        for (Integer pos : itemPorts.keySet()) {
            for (PortInfo info : itemPorts.get(pos)) {
                if (info.type == type) {
                    if (found == itemCounter) {
                        itemCounter++;
                        return info;
                    } else found++;
                }
            }
        }
        return null;
    }

    public PortInfo getNextFluidPort(PortType type) {
        int found = 0;
        for (Integer pos : fluidPorts.keySet()) {
            for (PortInfo info : fluidPorts.get(pos)) {
                if (info.type == type) {
                    if (found == fluidCounter) {
                        fluidCounter++;
                        return info;
                    } else found++;
                }
            }
        }
        return null;
    }

    /**
     * @return Returns the concatenated item stacks of all the inventory slots of item handler of all input item ports
     */
    Tuple<ItemStack[],int[]> collectItemInputs() {
        List<ItemStack> stacks = new ArrayList<>();
        List<Integer> slots = new ArrayList<>();
        for (PortInfo info = null;null != (info = getNextItemPort(PortType.INPUT));) {
            IItemHandler handler = inventoryHandlers.get(info.index);
            for(int j = 0;j < handler.getSlots();j++) {
                stacks.add(handler.getStackInSlot(j));
                slots.add(j + ((STEMMInventoryHandler)handler).getOffset());
            }
        }
        return new Tuple<>(stacks.toArray(new ItemStack[0]),slots.stream().mapToInt(i->i).toArray());
    }


    /**
     * @return Returns the concatenated fluid stacks of the fluid stacks of the tanks of all input fluid ports
     */
    Tuple<FluidStack[],int[]> collectFluidInputs() {
        List<FluidStack> stacks = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        for (PortInfo info = null;null != (info = getNextFluidPort(PortType.INPUT));) {
            IFluidTank handler = tanks.get(info.index);
            stacks.add(handler.getFluid());
            indexes.add(info.index);
        }
        return new Tuple<>(stacks.toArray(new FluidStack[0]),indexes.stream().mapToInt(i->i).toArray());
    }

    @Override
    public void update() {
        super.func_73660_a();
        if (world.isRemote) return;
        if (world.getTotalWorldTime() % 20 == 0) {
            IEUtils.notifyClientUpdate(world,pos);
            markDirty();
        }
        if (isDummy()) return;

        if (!isRSDisabled() && (energyStorage.getEnergyStored() > 0 || energyStorage.getMaxEnergyStored() == 0) && this.processQueue.size() < this.getProcessQueueMaxLength()) {

            for (MultiblockProcess<R> process : this.processQueue) {
                onRecipeTick(process.recipe);
            }

            resetPortCounters();

            Tuple<ItemStack[],int[]> itemsAndSlots = collectItemInputs();
            Tuple<FluidStack[],int[]> fluidsAndIndexes = collectFluidInputs();

            resetPortCounters();

            List<R> blackList = new ArrayList<>();

            R recipe;

            do {
                recipe = IEUtils.findRecipe(recipes,fluidsAndIndexes.getFirst(),itemsAndSlots.getFirst(),blackList);
                blackList.add(recipe);
            } while(recipe != null && !canDoRecipe(recipe));

            if (recipe != null && canDoRecipe(recipe)) {
                resetPortCounters();
                if (enoughSpaceForRecipe(recipe)) {
                    System.out.println("HHGHG");
                    MultiblockProcessInMachine<R> process = new MultiblockProcessInMachine<R>(recipe);
                    if (process.recipe.consumeFirst) onProcessFinishNoCheck(process);
                    this.processQueue.add(process);
                } else {
                    System.out.println("WAAA");
                }
                resetPortCounters();
            }

            for (STEMMFluidTank tank : tanks) {
                if (tank.fluidOHInput == -1 || tank.fluidIHInput == -1) continue;
                STEMMInventoryHandler ihi = inventoryHandlers.get(tank.fluidIHInput);
                STEMMInventoryHandler ohi = inventoryHandlers.get(tank.fluidOHInput);
                ItemStack input = ihi.getStackInSlot(0);
                IFluidHandlerItem fh = (IFluidHandlerItem)input.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, (EnumFacing)null);
                if (fh != null && fh.getTankProperties().length != 0) {
                    FluidStack fs = fh.getTankProperties()[0].getContents();
                    if (fs == null) continue;
                    if (tank.fill(fs,false) == fs.amount) {
                        tank.fill(fs,true);
                        fh.drain(fs,true);
                        ihi.setStackInSlot(0,ItemStack.EMPTY);
                        ohi.setStackInSlot(0,fh.getContainer());
                        markDirty();
                        IEUtils.notifyClientUpdate(world,getPos()); // why ??
                    }
                }
            }
        }

        outputFluids();
        outputItems();
        inputRotaryPower();
    }

    protected boolean enoughSpaceForRecipe(R recipe) {
        List<ItemStack> outputs = recipe.getItemOutputs();
        if (outputs != null && !outputs.isEmpty()) {
            for (ItemStack stack : outputs) {
                PortInfo info = getNextItemPort(PortType.OUTPUT);
                if (info == null) return false;
                if (info.index == -1) return true; // no internal inventory associated
                STEMMInventoryHandler handler = inventoryHandlers.get(info.index);
                boolean foundSlot = false;
                for (int s = 0; s < handler.getSlots(); s++) {
                    ItemStack outStack = handler.getStackInSlot(s);
                    if (!outStack.isEmpty()) {
                        if ((ItemHandlerHelper.canItemStacksStack(outStack, stack) && outStack.getCount() + stack.getCount() <= getSlotLimit(handler.getOffset() + s))) {
                            foundSlot = true;
                            break;
                        }
                    } else {
                        foundSlot = true;
                        break;
                    }
                }
                if (!foundSlot) return false;
            }
        }
        List<FluidStack> fluidOutputs = recipe.getFluidOutputs();
        if (fluidOutputs != null && !fluidOutputs.isEmpty()) {
            for (FluidStack fluidOutput : fluidOutputs) {
                PortInfo info = getNextFluidPort(PortType.OUTPUT);
                if (info == null) {
                    return false;
                }
                IFluidTank tank = (IFluidTank) tanks.get(info.index);
                if (((STEMMFluidTank)tank).fillFromRecipe(fluidOutput, false) != fluidOutput.amount) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void outputFluids() {
        // already in master
        for (Integer port : fluidPorts.keySet()) {
            for (PortInfo portInfo : fluidPorts.get(port)) {
                if (portInfo.type == PortType.INPUT) continue;
                IFluidHandler tank = FluidUtil.getFluidHandler(world, getBlockPosForPos(port).offset(portInfo.face), portInfo.face.getOpposite());
                if (this.tanks.get(portInfo.index).getFluidAmount() > 0 && tank != null) {
                    FluidStack out = this.tanks.get(portInfo.index).getFluid();
                    int accepted = tank.fill(out, false);
                    if (accepted == 0) continue;
                    int drained = tank.fill(Utils.copyFluidStackWithAmount(out, Math.min(out.amount, accepted), false), true);
                    this.tanks.get(portInfo.index).drain(drained, true);
                }
            }
        }
    }

    protected void outputItems() {
        for (Integer pos : itemPorts.keySet()) {
            for (PortInfo info : itemPorts.get(pos)) {
                if (info.type == PortType.OUTPUT && info.index > -1) { // means that there is internal storage for output items
                    IItemHandler handler = inventoryHandlers.get(info.index);
                    BlockPos bpos = getBlockPosForPos(pos).offset(info.face);
                    TileEntity te = world.getTileEntity(bpos);
                    if (te != null) {
                        for (int i = 0; i < handler.getSlots(); i++) {
                            if (!handler.getStackInSlot(i).isEmpty()) {
                                ItemStack is = handler.getStackInSlot(i);
                                ItemStack iss = Utils.insertStackIntoInventory(te, is, info.face.getOpposite(), true);
                                if (is != iss) {
                                    iss = Utils.insertStackIntoInventory(te, is, info.face.getOpposite(), false);
                                    if (iss.isEmpty()) handler.extractItem(i, is.getCount(), false);
                                    else handler.extractItem(i, is.getCount() - iss.getCount(), false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected void inputRotaryPower() {
        for (Integer pos : rotaryPorts.keySet()) {
            for (PortInfo info : rotaryPorts.get(pos)) {
                if (info.type == PortType.INPUT && info.index > -1) {
                    BlockPos bpos = getBlockPosForPos(pos).offset(info.face);
                    TileEntity te = world.getTileEntity(bpos);
                    boolean set = false;
                    if (te != null) {
                        if (te.hasCapability(CapabilityRotaryEnergy.ROTARY_ENERGY,info.face.getOpposite())) {
                            IRotaryEnergy re = te.getCapability(CapabilityRotaryEnergy.ROTARY_ENERGY,info.face.getOpposite());
                            if (re != null) {
                                IRotaryEnergy thisRe = rotaryEnergies.get(info.index);
                                thisRe.setTorque(re.getOutputTorque());
                                thisRe.setRotationSpeed(re.getOutputRotationSpeed());
                                set = true;
                            }
                        }
                    }
                    if (!set) {
                        IRotaryEnergy thisRe = rotaryEnergies.get(info.index);
                        thisRe.setTorque(0);
                        thisRe.setRotationSpeed(0);
                    }
                }
            }
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (world.isRemote || master() == null) return false;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (master() == null) return false;
            List<PortInfo> infoList = master().itemPorts.get(this.field_174879_c);
            if (infoList == null) return false;
            for (PortInfo info : infoList) {
                if (info.face == facing)
                    return true;
            }
            return false;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null) {
            if (master() == null) return false;
            List<PortInfo> infoList = master().fluidPorts.get(this.field_174879_c);
            if (infoList == null) return false;
            for (PortInfo info : infoList) {
                if (info.index != -1 && facing == info.face) return true;
            }
            return false;
        } else if (capability == CapabilityRotaryEnergy.ROTARY_ENERGY && facing != null) {
            if (master() == null) return false;
            List<PortInfo> infoList = master().rotaryPorts.get(this.field_174879_c);
            if (infoList == null) return false;
            for (PortInfo info : infoList) {
                if (info.index != -1 && facing == info.face && info.type == PortType.OUTPUT) return true;
            }
            return false;
        } else {
            return super.hasCapability(capability,facing);
        }
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (world.isRemote || master() == null) return null;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            List<PortInfo> infoList = master().itemPorts.get(this.field_174879_c);
            if (infoList == null) return null;
            for (PortInfo info : infoList) {
                if (info.face == facing && info.index > -1) {
                    return (T)master().inventoryHandlers.get(info.index);
                }
            }
            return null;
        }  else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null) {
            List<PortInfo> infoList = master().fluidPorts.get(this.field_174879_c);
            if (infoList == null) return null;
            for (PortInfo info : infoList) {
                if (info.index != -1 && facing == info.face) return (T)new TTMultiblockFluidWrapper(master().tanks.get(info.index),this,info.type);
            }
            return null;
        } else if (capability == CapabilityRotaryEnergy.ROTARY_ENERGY && facing != null) {
            if (master() == null) return null;
            List<PortInfo> infoList = master().rotaryPorts.get(this.field_174879_c);
            for (PortInfo info : infoList) {
                if (info.index != -1 && info.face == facing && info.type == PortType.OUTPUT) {
                    return (T)new RotaryStorageWrapper(master().rotaryEnergies.get(info.index),info.type);
                }
            }
            return null;
        } else {
            return super.getCapability(capability, facing);
        }
    }

    @Override
    protected R readRecipeFromNBT(NBTTagCompound nbtTagCompound) {
        int itemSize = nbtTagCompound.getInteger("inputItems");
        int fluidSize = nbtTagCompound.getInteger("inputFluids");
        ItemStack[] items = new ItemStack[itemSize];
        FluidStack[] fluids = new FluidStack[fluidSize];
        for (int i = 0;i < itemSize;i++) {
            NBTTagCompound tag = nbtTagCompound.getCompoundTag("inputItem-"+i);
            if (tag.hasKey("isNull"))  items[i] = null;
            else items[i] = new ItemStack(nbtTagCompound.getCompoundTag("inputItem-"+i));
        }
        for (int i = 0;i < fluidSize;i++) {
            NBTTagCompound tag = nbtTagCompound.getCompoundTag("inputFluid-"+i);
            if (tag.hasKey("isNull")) fluids[i] = null;
            else fluids[i] = FluidStack.loadFluidStackFromNBT(nbtTagCompound.getCompoundTag("inputFluid-"+i));
        }
        R recipe = IEUtils.findRecipe(recipes,fluids,items,new ArrayList<>());
        return recipe;
    }

    @Override
    public int[] getEnergyPos() {
        if (world.isRemote || master() == null) return new int[0];
        return master().energyPorts.stream().mapToInt(i->i).toArray();
    }

    @Override
    public int[] getRedstonePos() {
        if (world.isRemote || master() == null) return new int[0];
        return master().redstonePorts.stream().mapToInt(i->i).toArray();
    }

    @Nonnull
    @Override
    protected IFluidTank[] getAccessibleFluidTanks(EnumFacing enumFacing) {
        if (world.isRemote || master() == null) return new IFluidTank[0];
        return master().tanks.toArray(new IFluidTank[0]);
    }

    @Override
    public R findRecipeForInsertion(ItemStack itemStack) {
        return (R)IEUtils.findRecipeForItem(itemStack,recipes);
    }

    @Override
    public void doProcessOutput(ItemStack itemStack) {
        ItemStack rest = itemStack;
        PortInfo info = getNextItemPort(PortType.OUTPUT);
        //if (port == -1) return;
        if (!rest.isEmpty()) {
            if (info.index > -1) {
                STEMMInventoryHandler handler = inventoryHandlers.get(info.index);
                for (int i = 0;i < handler.getSlots() && rest != null;i++) {
                    rest = handler.outputItem(i,rest,false);
                }
            }
        }
        if (!rest.isEmpty()) {
            BlockPos pos = getBlockPosForPos(info.pos).offset(info.face);
            TileEntity te = world.getTileEntity(pos);
            if (te != null) {
                rest = Utils.insertStackIntoInventory(te,rest,info.face.getOpposite());
            }
        }
        if (!rest.isEmpty()) {
            BlockPos pos = getBlockPosForPos(info.pos);
            ItemStack stack = rest;
            EnumFacing facing = info.face;
            if (!stack.isEmpty()) {
                EntityItem ei = new EntityItem(world, (double)pos.getX() + 0.5, (double)pos.getY() + (facing != EnumFacing.DOWN ? 0.5 : -0.5), (double)pos.getZ() + 0.5, stack.copy());
                ei.motionY = 0.025000000372529;
                if (facing != null) {
                    ei.motionX = (double)(0.075F * (float)facing.getFrontOffsetX());
                    ei.motionZ = (double)(0.075F * (float)facing.getFrontOffsetZ());
                    ei.motionY = (double)(0.075F * (float)facing.getFrontOffsetY());
                }

                world.spawnEntity(ei);
            }
        }
    }

    @Override
    public void doProcessFluidOutput(FluidStack fluidStack) {
        PortInfo info = getNextFluidPort(PortType.OUTPUT);
        //if (port == -1) return; // could mean that the te is not loaded in yet
        tanks.get(info.index).fillFromRecipe(fluidStack,true);
    }


    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess<R> multiblockProcess) {
        return true;
    }

    @Override
    public int getMaxProcessPerTick() {
        return 1;
    }

    @Override
    public boolean isInWorldProcessingMachine() {
        return false;
    }
    @Override
    public float[] getBlockBounds() {
        return null;
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }

    @Override
    public boolean isStackValid(int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public int getSlotLimit(int i) {
        return 64;
    }

    @Override
    public void doGraphicalUpdates(int i) {

    }

    public void onProcessFinishNoCheck(MultiblockProcess<R> multiblockProcess) {
        resetPortCounters();

        // extracts inputs in order

        SimplifiedMultiblockRecipe recipe = (SimplifiedMultiblockRecipe)multiblockProcess.recipe;

        resetPortCounters();

        for (IngredientStack stack : recipe.getItemInputs()) {
            //STEMMInventoryHandler handler = (STEMMInventoryHandler) inventoryHandlers.get(getNextItemPort(PortType.INPUT).index);
            for (STEMMInventoryHandler handler : inventoryHandlers) { // AAAAAAAAAAAAAAAAAAAAAAAHHHHHHH
                if (stack.stack == null) continue;
                int remaining = stack.stack.getCount();
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack is = handler.getStackInSlot(i);
                    if (stack.stack.getItem() == is.getItem()) {
                        int toShrink = Math.min(remaining, is.getCount());
                        remaining -= toShrink;
                        ItemStack newIs = is.copy();
                        newIs.shrink(toShrink);
                        handler.setStackInSlot(i, newIs);
                        if (remaining == 0) break;
                    }
                }
            }
        }

        for (FluidStack stack : recipe.getFluidInputs()) {
            FluidTank tank = tanks.get(getNextFluidPort(PortType.INPUT).index);
            if (stack == null) continue;
            tank.drain(stack.amount,true);
        }

        resetPortCounters();
    }
    @Override
    public void onProcessFinish(MultiblockProcess<R> multiblockProcess) {
        if (!((SimplifiedMultiblockRecipe)multiblockProcess.recipe).consumeFirst) onProcessFinishNoCheck(multiblockProcess);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt,boolean descPacket) {
        super.writeCustomNBT(nbt,descPacket);

        if (this.offset.length !=3 || isDummy()) return;

        int index = 0;

        //nbt.setInteger("face", face.ordinal());

        nbt.setInteger("rotStorageCount", rotaryEnergies.size());
        for (int i = 0; i < rotaryEnergies.size(); i++) {
            nbt.setTag("rotStorage-" + i, rotaryEnergies.get(i).toNBT());
        }

        nbt.setInteger("tanksCount", tanks.size());
        for (int i = 0; i < tanks.size(); i++) {
            nbt.setInteger("tankCapacity-"+i,tanks.get(i).getCapacity());
            nbt.setTag("tank-" + i, tanks.get(i).writeToNBT(new NBTTagCompound()));
        }

        nbt.setInteger("inventorySize", inventory.size());
        for (ItemStack stack : inventory) {
            nbt.setTag("item-" + index, stack.writeToNBT(new NBTTagCompound()));
            index++;
        }

        nbt.setInteger("energyPortsCount", energyPorts.size());
        for (int i = 0; i < energyPorts.size(); i++) nbt.setInteger("energyPort-" + i, energyPorts.get(i));

        index = 0;
        nbt.setInteger("rotaryPortsCount",rotaryPorts.size());
        for (Integer i : rotaryPorts.keySet()) {
            nbt.setInteger("rotaryPortCode-" + (index++), i);
            nbt.setInteger("rotaryPort-"+i+"-count",rotaryPorts.get(i).size());
            for (int j = 0;j < rotaryPorts.get(i).size();j++) {
                nbt.setTag("rotaryPort-" + i + "-" + j, rotaryPorts.get(i).get(j).writeToNBT(new NBTTagCompound()));
            }
        }

        nbt.setInteger("redstonePortsCount", redstonePorts.size());
        for (int i = 0; i < redstonePorts.size(); i++) nbt.setInteger("redstonePort-" + i, redstonePorts.get(i));

        index = 0;
        nbt.setInteger("fluidPortsCount",fluidPorts.size());
        for (Integer i : fluidPorts.keySet()) {
            nbt.setInteger("fluidPortCode-" + (index++), i);
            nbt.setInteger("fluidPort-"+i+"-count",fluidPorts.get(i).size());
            for (int j = 0;j < fluidPorts.get(i).size();j++) {
                nbt.setTag("fluidPort-" + i + "-" + j, fluidPorts.get(i).get(j).writeToNBT(new NBTTagCompound()));
            }
        }

        index = 0;
        nbt.setInteger("itemPortsCount",itemPorts.size());
        for (Integer i : itemPorts.keySet()) {
            nbt.setInteger("itemPortCode-"+(index++),i);
            nbt.setInteger("itemPort-"+i+"-count",itemPorts.get(i).size());
            for (int j = 0;j < itemPorts.get(i).size();j++) {
                nbt.setTag("itemPort-"+i+"-"+j,itemPorts.get(i).get(j).writeToNBT(new NBTTagCompound()));
            }
        }

        nbt.setInteger("ihCount",inventoryHandlers.size());
        for (int i = 0;i < inventoryHandlers.size();i++) {
            NBTTagCompound handlerNBT = new NBTTagCompound();
            handlerNBT.setBoolean("fluidOnly",inventoryHandlers.get(i).getFluidOnly());
            handlerNBT.setInteger("slots",inventoryHandlers.get(i).getSlots());
            handlerNBT.setInteger("offsetih",inventoryHandlers.get(i).getOffset());
            handlerNBT.setIntArray("canInsert",IEUtils.booleanToInt(inventoryHandlers.get(i).getCanInsert()));
            handlerNBT.setIntArray("canExtract",IEUtils.booleanToInt(inventoryHandlers.get(i).getCanExtract()));
            handlerNBT.setInteger("inputs",inventoryHandlers.get(i).recipeInputCorelation.size());
            for (int j = 0;j <  inventoryHandlers.get(i).recipeInputCorelation.size();j++) {
                handlerNBT.setInteger("crl-"+j,inventoryHandlers.get(i).recipeInputCorelation.get(j));
            }
            nbt.setTag("ih-"+i,handlerNBT);
        }

    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt,descPacket);

        if (this.offset.length !=3 || isDummy()) return;

        energyPorts.clear();
        rotaryPorts.clear();
        rotaryEnergies.clear();
        redstonePorts.clear();
        tanks.clear();
        fluidPorts.clear();
        inventoryHandlers.clear();
        itemPorts.clear();

        int rotCount = nbt.getInteger("rotStorageCount");
        for (int i = 0; i < rotCount; i++) {
            RotaryStorage rs = new RotaryStorage();
            rs.fromNBT(nbt.getCompoundTag("rotStorage-" + i));
            rotaryEnergies.add(rs);
        }

        int tanksCount = nbt.getInteger("tanksCount");
        for (int i = 0; i < tanksCount; i++) tanks.add(new STEMMFluidTank(nbt.getInteger("tankCapacity-"+i),this).readFromNBT(nbt.getCompoundTag("tank-" + i)));


        for (int i = 0; i < inventory.size(); i++) inventory.set(i,new ItemStack(nbt.getCompoundTag("item-" + i)));


        int energyPortsCount = nbt.getInteger("energyPortsCount");
        for (int i = 0; i < energyPortsCount; i++) energyPorts.add(nbt.getInteger("energyPort-" + i));


        int redstonePortsCount = nbt.getInteger("redstonePortsCount");
        for (int i = 0; i < redstonePortsCount; i++) redstonePorts.add(nbt.getInteger("redstonePort-" + i));

        int fluidPortsCount = nbt.getInteger("fluidPortsCount");
        for (int i = 0; i < fluidPortsCount; i++) {
            int code = nbt.getInteger("fluidPortCode-"+i);
            int count = nbt.getInteger("fluidPort-"+code+"-count");
            List<PortInfo> infos = new ArrayList<>();
            for (int j = 0;j < count;j++) infos.add(PortInfo.fromNBT((NBTTagCompound) nbt.getTag("fluidPort-"+code+"-"+j)));
            fluidPorts.put(code,infos);
        }

        int rotaryPortsCount = nbt.getInteger("rotaryPortsCount");
        for (int i = 0; i < rotaryPortsCount; i++) {
            int code = nbt.getInteger("rotaryPortCode-"+i);
            int count = nbt.getInteger("rotaryPort-"+code+"-count");
            List<PortInfo> infos = new ArrayList<>();
            for (int j = 0;j < count;j++) infos.add(PortInfo.fromNBT((NBTTagCompound) nbt.getTag("rotaryPort-"+code+"-"+j)));
            rotaryPorts.put(code,infos);
        }

        int itemPortsCount = nbt.getInteger("itemPortsCount");
        for (int i = 0;i < itemPortsCount;i++) {
            int code = nbt.getInteger("itemPortCode-"+i);
            int count = nbt.getInteger("itemPort-"+code+"-count");
            List<PortInfo> infos = new ArrayList<>();
            for (int j = 0;j < count;j++) infos.add(PortInfo.fromNBT((NBTTagCompound) nbt.getTag("itemPort-"+code+"-"+j)));
            itemPorts.put(code,infos);
        }

        int ihCount = nbt.getInteger("ihCount");
        for (int i = 0;i < ihCount;i++) {
            NBTTagCompound ihNBT = (NBTTagCompound) nbt.getTag("ih-"+i);
            boolean fluidOnly = ihNBT.getBoolean("fluidOnly");
            int slots = ihNBT.getInteger("slots");
            int offset = ihNBT.getInteger("offsetih");
            boolean[] canInsert = IEUtils.intToBoolean(ihNBT.getIntArray("canInsert"));
            boolean[] canExtract = IEUtils.intToBoolean(ihNBT.getIntArray("canExtract"));
            STEMMInventoryHandler handler = new STEMMInventoryHandler(slots,this,offset,canInsert,canExtract);
            int size = ihNBT.getInteger("inputs");
            for (int j = 0;j <  size;j++) {
                handler.addRecipeFlowCorelation(ihNBT.getInteger("crl-"+j));
            }
            handler.setFluidOnly(fluidOnly);
            inventoryHandlers.add(handler);
        }

    }

    /*@Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeCustomNBT(compound,true);
        return new SPacketUpdateTileEntity(getPos(),3,compound);
    }*/

    @Override
    public float getMinProcessDistance(MultiblockProcess<R> multiblockProcess) {
        return 0;
    }

    // Invalidates IE's recipe logic

    @Override
    public int[] getOutputSlots() {
        return null;
    }

    @Override
    public int[] getOutputTanks() {
        return null;
    }

    @Override
    public IFluidTank[] getInternalTanks() {
        return null;
    }

    @Override
    protected boolean canFillTankFrom(int i, EnumFacing enumFacing, FluidStack fluidStack) {
        return true;
    }

    @Override
    protected boolean canDrainTankFrom(int i, EnumFacing enumFacing) {
        return true;
    }

    // =================== AE2 compat


    @Override
    public boolean isDummy() {
        if (offset == null || offset.length != 3) return true;
        return super.isDummy();
    }

    public void disassemble()
    {
        if(formed && !world.isRemote)
        {
            BlockPos startPos = getOrigin();
            BlockPos masterPos = getPos().add(-offset[0], -offset[1], -offset[2]);
            long time = world.getTotalWorldTime();
            for(int yy=0;yy<structureDimensions[0];yy++)
                for(int ll=0;ll<structureDimensions[1];ll++)
                    for(int ww=0;ww<structureDimensions[2];ww++)
                    {
                        int w = mirrored?-ww:ww;
                        BlockPos pos = startPos.offset(facing, ll).offset(facing.rotateY(), w).add(0, yy, 0);
                        ItemStack s = ItemStack.EMPTY;

                        TileEntity te = world.getTileEntity(pos);
                        if(te instanceof TileEntityMultiblockPart)
                        {
                            TileEntityMultiblockPart part = (TileEntityMultiblockPart) te;
                            Vec3i diff = pos.subtract(masterPos);
                            if (part.offset[0]!=diff.getX()||part.offset[1]!=diff.getY()||part.offset[2]!=diff.getZ())
                                continue;
                            else if (time!=part.onlyLocalDissassembly)
                            {
                                s = part.getOriginalBlock();
                                part.formed = false;
                            }
                        } /*else if (te instanceof TileEntityAE2CompatMultiblock) {
                            TileEntityAE2CompatMultiblock part = (TileEntityAE2CompatMultiblock) te;
                            Vec3i diff = pos.subtract(masterPos);
                            if (part.offset[0]!=diff.getX()||part.offset[1]!=diff.getY()||part.offset[2]!=diff.getZ())
                                continue;
                            else if (time!=part.onlyLocalDissassembly)
                            {
                                s = part.getOriginalBlock();
                                part.formed = false;
                            }
                        }*/
                        if(pos.equals(getPos()))
                            s = this.getOriginalBlock();
                        IBlockState state = Utils.getStateFromItemStack(s);
                        if(state!=null)
                        {
                            if(pos.equals(getPos()))
                                world.spawnEntity(new EntityItem(world, pos.getX()+.5,pos.getY()+.5,pos.getZ()+.5, s));
                            else
                                replaceStructureBlock(pos, state, s, yy,ll,ww);
                        }
                    }
        }
    }
    public float progress() {
        if (this.processQueue.isEmpty()) return 0;
        return (float)this.processQueue.get(0).processTick/this.processQueue.get(0).maxTicks;
    }
}
