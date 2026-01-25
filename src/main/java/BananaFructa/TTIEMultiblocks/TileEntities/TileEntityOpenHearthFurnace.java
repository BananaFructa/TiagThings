package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.Particles.SmokeParticle;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.*;
import BananaFructa.TiagThings.Utils;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

// I do not like anything about this code
public class TileEntityOpenHearthFurnace extends SimplifiedTileEntityMultiblockMetal<TileEntityOpenHearthFurnace, SimplifiedMultiblockRecipe> {

    public static int idleTimeToMildSteel = 20*30;
    public static int idleTimeStart = -20*10;
    public int idleTime = idleTimeStart;

    public static final List<SimplifiedMultiblockRecipe> sitRecipes = new ArrayList<SimplifiedMultiblockRecipe>(){{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),1)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidRegistry.getFluid("mild_steel"),1)},0,0));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(FluidRegistry.getFluid("crucible_steel"),1)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),1)},0,0));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),1)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),1)},0,0));
    }};

    public static final List<SimplifiedMultiblockRecipe> centralisedRecipes = new ArrayList<SimplifiedMultiblockRecipe>(){{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),1)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),1)},0,0));
    }};

    public static final List<SimplifiedMultiblockRecipe> solidBurningRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/coke>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 60,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:petcoke>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 60,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<minecraft:coal:1>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 120,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/bituminous_coal>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 240,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/lignite>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 120,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/jet>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 40,true));
    }};

    public static final List<SimplifiedMultiblockRecipe> liquidBurningRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:diesel>"),10)},new ItemStack[0],new FluidStack[0],0,4,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:gasoline>"),10)},new ItemStack[0],new FluidStack[0],0,4,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:biodiesel>"),10)},new ItemStack[0],new FluidStack[0],0,4,true));
    }};

    public static final List<SimplifiedMultiblockRecipe> burningRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
       addAll(solidBurningRecipes);
       addAll(liquidBurningRecipes);
    }};

    public static final List<SimplifiedMultiblockRecipe> chargingRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/ingot/steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),100)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/double_ingot/steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),200)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/sheet/steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),200)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/double_sheet/steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),400)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/dust/steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),100)},0,20,true));

        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/ingot/pig_iron>")},new FluidStack[0],new ItemStack[0],new FluidStack[] { new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),100)} ,0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/double_ingot/pig_iron>")},new FluidStack[0],new ItemStack[0],new FluidStack[] {new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),200)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/sheet/pig_iron>")},new FluidStack[0],new ItemStack[0],new FluidStack[] {new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),200)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/double_sheet/pig_iron>")},new FluidStack[0],new ItemStack[0],new FluidStack[] {new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),400)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/dust/pig_iron>")},new FluidStack[0],new ItemStack[0],new FluidStack[] {new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),100)},0,20,true));

        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<<tfc:metal/ingot/crucible_steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidRegistry.getFluid("crucible_steel"),100)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/double_ingot/crucible_steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidRegistry.getFluid("crucible_steel"),200)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/sheet/crucible_steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidRegistry.getFluid("crucible_steel"),200)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/double_sheet/crucible_steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidRegistry.getFluid("crucible_steel"),400)},0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/dust/crucible_steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidRegistry.getFluid("crucible_steel"),100)},0,20,true));
    }};

    public static final List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        addAll(burningRecipes);
        addAll(chargingRecipes);
    }};

    int temperature = 0;
    public boolean clientActiveBurning = false;
    float clientBurnPosX,clientBurnPosY,clientBurnPosZ;

    boolean wasBurning = false;

    public TileEntityOpenHearthFurnace() {
        super(TTIEContent.openHearthFurnace, 0, false,recipes);
    }

    STEMMFluidTank dummyTank = new STEMMFluidTank(0,this);

    @Override
    public void initPorts() {

        int fuelItemHandler = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(90,fuelItemHandler,PortType.INPUT,EnumFacing.EAST);

        int fuelTankHandler = registerFluidTank(1000);
        registerFluidPort(120,fuelTankHandler,PortType.INPUT,EnumFacing.EAST); // fluid in 0

        int moltenBath = registerFluidTank(30000);
        registerFluidPort(589,moltenBath,PortType.OUTPUT,EnumFacing.NORTH); // fluid out 0
        registerFluidPort(745,moltenBath,PortType.INPUT,EnumFacing.NORTH); // fluid in 1

        int metalItemHandler = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(743,metalItemHandler,PortType.INPUT,EnumFacing.NORTH);



    }

    public static final Fluid liquidSteel = FluidsTFC.getFluidFromMetal(Metal.STEEL);
    public static final Fluid liquidPigIron = FluidsTFC.getFluidFromMetal(Metal.PIG_IRON);
    public static final Fluid liquidCrucibleSteel = FluidRegistry.getFluid("crucible_steel");

    public boolean isSteel(Fluid f) {
        return f == liquidSteel || f == liquidCrucibleSteel || f == liquidPigIron;
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote && clientActiveBurning) {
            for (int i = 0; i < 5; i++) {
                Minecraft.getMinecraft().effectRenderer.addEffect(new SmokeParticle(world, clientBurnPosX + 0.5 + (world.rand.nextFloat() - 0.5) * 0.3, clientBurnPosY + 1 + (world.rand.nextFloat() - 0.5) * 0.3, clientBurnPosZ + 0.5 + (world.rand.nextFloat() - 0.5) * 0.3, world.rand.nextFloat() * 0.05, world.rand.nextFloat() * 0.1 + 0.2, world.rand.nextFloat() * 0.05, (int) (10 * 20 * (0.5 + world.rand.nextInt(2))), 2 + world.rand.nextFloat() * 2));
            }
            //for (int i = 0;i < 5;i++) world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,true,clientBurnPosX +0.5 + (world.rand.nextFloat() - 0.5)*0.3,clientBurnPosY+1+ (world.rand.nextFloat() - 0.5)*0.3,clientBurnPosZ+0.5+ (world.rand.nextFloat() - 0.5)*0.3,0,world.rand.nextFloat() * 0.5,0);
        }
        if (world.isRemote || isDummy()) return;
        boolean isBurning = isBurning();
        if ((temperature != 0 && temperature != 1750) || (isBurning ^ wasBurning))
            world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 2);
        wasBurning = isBurning;
        if (isBurning) temperature++;
        else temperature--;
        temperature = Math.max(Math.min(temperature, 1750), 0);
        if (temperature == 1750 && tanks.get(1).getFluidAmount() > 0 && isSteel(tanks.get(1).getFluid().getFluid())) {
            idleTime++;
            if (idleTime == idleTimeToMildSteel) {
                if (tanks.get(1).getFluid().getFluid() == liquidSteel) {
                    tanks.get(1).setFluid(new FluidStack(FluidRegistry.getFluid("mild_steel"), tanks.get(1).getFluidAmount()));
                } else if (tanks.get(1).getFluid().getFluid() == liquidCrucibleSteel || tanks.get(1).getFluid().getFluid() == liquidPigIron) {
                    tanks.get(1).setFluid(new FluidStack(liquidSteel, tanks.get(1).getFluidAmount()));
                }
                idleTime = idleTimeStart;
            }
        } else if (!(tanks.get(1).getFluidAmount() > 0 && isSteel(tanks.get(1).getFluid().getFluid()))) {
            idleTime = idleTimeStart;
        }
    }

    @Override
    protected void outputFluids() {
        /*if (temperature == 1750)*/ super.outputFluids();
    }

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        if (!super.canDoRecipe(recipe)) return false;
        if (burningRecipes.contains(recipe)) {
            for (SimplifiedMultiblockRecipe r : burningRecipes) {
                if (isCurrentlyDoingRecipe(r)) return false;
            }
        }
        if (chargingRecipes.contains(recipe)) {
            for (SimplifiedMultiblockRecipe r : chargingRecipes) {
                if (isCurrentlyDoingRecipe(r)) return false;
            }
        }
        if (chargingRecipes.contains(recipe) && (temperature < 1750)) return false;
        return true;
    }

    @Override
    public void onProcessFinish(MultiblockProcess<SimplifiedMultiblockRecipe> multiblockProcess) {
        if (chargingRecipes.contains(multiblockProcess.recipe)) idleTime = idleTimeStart;
        super.onProcessFinish(multiblockProcess);
    }

    public boolean isBurning() {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : burningRecipes) if(isCurrentlyDoingRecipe(coalBurnRecipe)) return true;
        return false;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setBoolean("active",isBurning());
        BlockPos smokePos = getBlockPosForPos(2561);
        nbtTagCompound.setFloat("smokePosx",smokePos.getX());
        nbtTagCompound.setFloat("smokePosy",smokePos.getY());
        nbtTagCompound.setFloat("smokePosz",smokePos.getZ());
        nbtTagCompound.setInteger("temperature",temperature);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        clientActiveBurning = pkt.getNbtCompound().getBoolean("active");
        clientBurnPosX = pkt.getNbtCompound().getFloat("smokePosx");
        clientBurnPosY = pkt.getNbtCompound().getFloat("smokePosy");
        clientBurnPosZ = pkt.getNbtCompound().getFloat("smokePosz");
        temperature = pkt.getNbtCompound().getInteger("temperature");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("temperature",temperature);
        nbt.setInteger("idleT",idleTime);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        temperature = nbt.getInteger("temperature");
        idleTime = nbt.getInteger("idleT");
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 5;
    }

    @Override
    public int getMaxProcessPerTick() {
        return 5;
    }

    public float getRemainingFuel() {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : solidBurningRecipes) {
            MultiblockProcess process = getProcessForRecipe(coalBurnRecipe);
            if (process == null) continue;
            return 1-(float)process.processTick/process.maxTicks;
        }
        for (SimplifiedMultiblockRecipe coalBurnRecipe : liquidBurningRecipes) {
            MultiblockProcess process = getProcessForRecipe(coalBurnRecipe);
            if (process == null) continue;
            return 1;
        }
        return 0.0f;
    }

    public float getChargingProcess() {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : chargingRecipes) {
            MultiblockProcess process = getProcessForRecipe(coalBurnRecipe);
            if (process == null) continue;
            return (float)process.processTick/process.maxTicks;
        }
        return 0.0f;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (world.isRemote || master() == null) return null;
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && facing != null) {
            List<PortInfo> infoList = master().fluidPorts.get(this.field_174879_c);
            if (infoList == null) return null;
            for (PortInfo info : infoList) {
                if (info.index == 1 && facing == info.face) {
                    boolean charged = master().tanks.get(1).getFluidAmount() != 0;
                    if (charged) {
                        return (T) new TTMultiblockFluidWrapper(master().tanks.get(info.index), this, info.type);
                    } else {
                        return (T) new TTMultiblockFluidWrapper(master().dummyTank,this ,info.type); // no fluid input if it is not charged
                    }
                } else {
                    return (T) new TTMultiblockFluidWrapper(master().tanks.get(info.index), this, info.type);
                }
            }
            return null;
        } else return super.getCapability(capability,facing);
    }
}
