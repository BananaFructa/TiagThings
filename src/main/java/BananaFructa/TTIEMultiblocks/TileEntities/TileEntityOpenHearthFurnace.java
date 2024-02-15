package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.Utils;
import net.dries007.tfc.api.types.Metal;
import net.dries007.tfc.objects.fluids.FluidsTFC;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityOpenHearthFurnace extends SimplifiedTileEntityMultiblockMetal<TileEntityOpenHearthFurnace, SimplifiedMultiblockRecipe> {

    public static int idleTimeToMildSteel = 20*30;
    public static int idleTimeStart = -20*10;
    public int idleTime = idleTimeStart;

    public static final List<SimplifiedMultiblockRecipe> sitRecipes = new ArrayList<SimplifiedMultiblockRecipe>(){{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),1)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidRegistry.getFluid("mild_steel"),1)},0,0));
    }};

    public static final List<SimplifiedMultiblockRecipe> centralisedRecipes = new ArrayList<SimplifiedMultiblockRecipe>(){{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),1)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),1)},0,0));
    }};

    public static final List<SimplifiedMultiblockRecipe> burningRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
       add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/coke>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 60));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:diesel>"),5)},new ItemStack[0],new FluidStack[0],0,2));
    }};
    static SimplifiedMultiblockRecipe pigIronIntake;
    public static final List<SimplifiedMultiblockRecipe> transferRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{null, new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),10)},new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),10)},0,1));
        add(pigIronIntake =  new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{null, new FluidStack(FluidsTFC.getFluidFromMetal(Metal.PIG_IRON),10)},new ItemStack[0],new FluidStack[0],0,1));
    }};

    public static final List<SimplifiedMultiblockRecipe> solidTransferRecipe = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/ingot/steel>")},new FluidStack[0],new ItemStack[0],new FluidStack[]{ new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),100)},0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:metal/ingot/pig_iron>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,1));
    }};

    public static final List<SimplifiedMultiblockRecipe> conversionRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[0],new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),2)},0,1)); // standard conversion recipes
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[0],new ItemStack[0],new FluidStack[]{new FluidStack(FluidsTFC.getFluidFromMetal(Metal.STEEL),1)},0,1)); // this is to make sure that nothing is left
    }};

    public static final List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        addAll(burningRecipes);
        addAll(transferRecipes);
        addAll(conversionRecipes);
        addAll(solidTransferRecipe);
    }};

    int temperature = 0;
    boolean clientActiveBurning = false;
    float clientBurnPosX,clientBurnPosY,clientBurnPosZ;

    boolean wasBurning = false;

    public int pigIronMb = 0;

    public TileEntityOpenHearthFurnace() {
        super(TTIEContent.openHearthFurnace, 0, false,recipes);
    }

    @Override
    public void initPorts() {

        int fuelItemHandler = registerItemHandler(1,new boolean[]{true},new boolean[]{false});
        registerItemPort(90,fuelItemHandler,PortType.INPUT,EnumFacing.EAST);

        int fuelTankHandler = registerFluidTank(1000);
        registerFluidPort(120,fuelTankHandler,PortType.INPUT,EnumFacing.EAST); // fluid in 0

        int steelBath = registerFluidTank(30000);
        int moltenMetalIn = registerFluidTank(10);
        registerFluidPort(589,steelBath,PortType.OUTPUT,EnumFacing.NORTH); // fluid out 0
        registerFluidPort(745,moltenMetalIn,PortType.INPUT,EnumFacing.NORTH); // fluid in 1

        int metalItemHandler = registerItemHandler(1,new boolean[]{true},new boolean[]{false});
        registerItemPort(743,metalItemHandler,PortType.INPUT,EnumFacing.NORTH);



    }

    public static final Fluid liquidSteel = FluidsTFC.getFluidFromMetal(Metal.STEEL);

    @Override
    public void update() {
        super.update();
        if (world.isRemote && clientActiveBurning) {
            for (int i = 0;i < 5;i++) world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,true,clientBurnPosX +0.5 + (world.rand.nextFloat() - 0.5)*0.3,clientBurnPosY+1+ (world.rand.nextFloat() - 0.5)*0.3,clientBurnPosZ+0.5+ (world.rand.nextFloat() - 0.5)*0.3,0,world.rand.nextFloat() * 0.5,0);
        }
        if (world.isRemote || isDummy()) return;
        boolean isBurning = isBurning();
        if ((temperature != 0 && temperature != 1750) || (isBurning ^ wasBurning)) world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
        wasBurning = isBurning;
        if (isBurning) temperature++;
        else temperature--;
        temperature = Math.max(Math.min(temperature,1750),0);
        if (temperature == 1750 && tanks.get(1).getFluidAmount() > 0 && tanks.get(1).getFluid().getFluid() == liquidSteel) {
            idleTime++;
            if (idleTime == idleTimeToMildSteel) {
                tanks.get(1).setFluid(new FluidStack(FluidRegistry.getFluid("mild_steel"),tanks.get(1).getFluidAmount()));
                idleTime = idleTimeStart;
            }
        } else if (!(tanks.get(1).getFluidAmount() > 0 && tanks.get(1).getFluid().getFluid() == liquidSteel)) {
            idleTime = idleTimeStart;
        }
    }

    @Override
    protected void outputFluids() {
        if (temperature == 1750) super.outputFluids();
    }

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        if (burningRecipes.contains(recipe)) {
            for (SimplifiedMultiblockRecipe r : burningRecipes) {
                if (isCurrentlyDoingRecipe(r)) return false;
            }
        }
        if (transferRecipes.contains(recipe)) {
            for (SimplifiedMultiblockRecipe r : transferRecipes) {
                if (isCurrentlyDoingRecipe(r)) return false;
            }
        }
        if (conversionRecipes.contains(recipe)) {
            for (SimplifiedMultiblockRecipe r : conversionRecipes) {
                if (isCurrentlyDoingRecipe(r)) return false;
            }
        }
        if (solidTransferRecipe.contains(recipe)) {
            for (SimplifiedMultiblockRecipe r : solidTransferRecipe) {
                if (isCurrentlyDoingRecipe(r)) return false;
            }
        }
        //if (solidTransferRecipe.contains(recipe) && temperature != 1750) return false;
        /*else*/ if (recipe == solidTransferRecipe.get(1) && pigIronMb > 30000-100) return false;
        if (conversionRecipes.get(0) == recipe && (temperature != 1750 || pigIronMb < 2 || tanks.get(1).getFluidAmount() == 0)) return false;
        if (conversionRecipes.get(1) == recipe && (temperature != 1750 || pigIronMb < 1 || tanks.get(1).getFluidAmount() == 0)) return false;
        if (recipe == pigIronIntake) {
            if (pigIronMb > 30000-1) return false;
        }
        return super.canDoRecipe(recipe);
    }

    @Override
    public void onProcessFinish(MultiblockProcess<SimplifiedMultiblockRecipe> multiblockProcess) {
        if (multiblockProcess.recipe == pigIronIntake) {
            idleTime = idleTimeStart;
            pigIronMb+=10;
            world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
            markDirty();
        }
        if (multiblockProcess.recipe == conversionRecipes.get(1)) {
            idleTime = idleTimeStart;
            pigIronMb--;
            world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
            markDirty();
        }
        if (multiblockProcess.recipe == conversionRecipes.get(0)) {
            idleTime = idleTimeStart;
            pigIronMb-=2;
            world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
            markDirty();
        }
        if (multiblockProcess.recipe == solidTransferRecipe.get(1)) {
            idleTime = idleTimeStart;
            pigIronMb += 100;
            world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
            markDirty();
        }
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
        nbtTagCompound.setInteger("pigIronMb",pigIronMb);
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
        pigIronMb = pkt.getNbtCompound().getInteger("pigIronMb");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("temperature",temperature);
        nbt.setInteger("pigIronMb",pigIronMb);
        nbt.setInteger("idleT",idleTime);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        temperature = nbt.getInteger("temperature");
        pigIronMb = nbt.getInteger("pigIronMb");
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
}
