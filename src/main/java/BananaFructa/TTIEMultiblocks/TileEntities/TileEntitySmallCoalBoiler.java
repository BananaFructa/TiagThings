package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import mods.railcraft.common.fluids.RailcraftFluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntitySmallCoalBoiler extends SimplifiedTileEntityMultiblockMetal<TileEntitySmallCoalBoiler, SimplifiedMultiblockRecipe> {
    public static final SimplifiedMultiblockRecipe coalBurnRecipe1 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/bituminous_coal>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 60 * 4,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe2 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/lignite>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 60 * 2,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe3 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/jet>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 40,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe4 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/coke>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 60,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe5 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<minecraft:coal:1>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 60 * 2,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe6 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:petcoke>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,20 * 60,true);

    public static final SimplifiedMultiblockRecipe waterBurnRecipe = new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:fresh_water>"),200)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:low_quality_steam>"), 400)},0,20);
    public static final SimplifiedMultiblockRecipe waterBurnRecipe2 = new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.treatedWater,200)},new ItemStack[0],new FluidStack[]{new FluidStack(RailcraftFluids.STEAM.getBlock().getFluid(), 400)},0,20);
    public static final SimplifiedMultiblockRecipe waterBurnRecipe3 = new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:oil>"),75)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:heated_oil>"),75)},0,3);
    public static final SimplifiedMultiblockRecipe waterBurnRecipe4 = new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:sour_oil>"),75)},new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:heated_sour_oil>"),75)},0,3);
    public static List<SimplifiedMultiblockRecipe> burnRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(coalBurnRecipe1);
        add(coalBurnRecipe2);
        add(coalBurnRecipe3);
        add(coalBurnRecipe4);
        add(coalBurnRecipe5);
        add(coalBurnRecipe6);
    }};

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(coalBurnRecipe1);
        add(coalBurnRecipe2);
        add(coalBurnRecipe3);
        add(coalBurnRecipe4);
        add(coalBurnRecipe5);
        add(coalBurnRecipe6);
        add(waterBurnRecipe);
        add(waterBurnRecipe2);
        add(waterBurnRecipe3);
        add(waterBurnRecipe4);
    }};


    int temperature = 0;

    boolean clientActiveBurning = false;
    boolean wasBurning = false;

    @Override
    public void update() {
        super.update();
        //if (world.isRemote && clientActiveBurning) {
        //    for (int i = 0;i < 10;i++) world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,true,clientBurnPosX + world.rand.nextFloat() - 0.5,clientBurnPosY+ world.rand.nextFloat() - 0.5,clientBurnPosZ+ world.rand.nextFloat() - 0.5,0,world.rand.nextFloat() * 2,0);
        //}
        if (world.isRemote || isDummy()) return;
        boolean isBurning = isBurningCoal();
        if ((temperature != 0 && temperature != 600) || (isBurning ^ wasBurning)) world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
        wasBurning = isBurning;
        if (isBurning) temperature++;
        else temperature--;
        temperature = Math.max(Math.min(temperature,600),0);
    }

    public TileEntitySmallCoalBoiler() {
        super(TTIEContent.smallCoalBoiler, 0, false,recipes);
    }

    @Override
    public void initPorts() {
        int itemIn = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(22,itemIn,PortType.INPUT,EnumFacing.NORTH);
        int bucketIn = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        int bucketOut = registerItemHandler(1,new boolean[]{true},new boolean[]{true});

        int outputTank = registerFluidTank(20000);
        registerFluidPort(31,outputTank,PortType.OUTPUT,EnumFacing.UP);
        int inputTank = registerFluidTank(20000,bucketIn,bucketOut);
        registerFluidPort(13,inputTank,PortType.INPUT,EnumFacing.SOUTH);



    }

    @Override
    public int getProcessQueueMaxLength() {
        return 2;
    }

    @Override
    public int getMaxProcessPerTick() {
        return 2;
    }

    public int getTemperature(){
        return temperature;
    }

    public boolean isBurningCoal() {
        boolean burning = false;
        for (SimplifiedMultiblockRecipe coalBurnRecipe : burnRecipes) burning |= isCurrentlyDoingRecipe(coalBurnRecipe);
        return burning;
    }

    public float getRemainingFuel() {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : burnRecipes) {
            MultiblockProcess process = getProcessForRecipe(coalBurnRecipe);
            if (process == null) continue;
            return 1-(float)process.processTick/process.maxTicks;
        }
        return 0.0f;
    }

    public float getBoilProcess() {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : new SimplifiedMultiblockRecipe[]{waterBurnRecipe,waterBurnRecipe2,waterBurnRecipe3}) {
            MultiblockProcess process = getProcessForRecipe(coalBurnRecipe);
            if (process == null) continue;
            return (float)process.processTick/process.maxTicks;
        }
        return 0.0f;
    }

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : burnRecipes) {
            if (recipe == coalBurnRecipe && isCurrentlyDoingRecipe(coalBurnRecipe)) return false;
        }
        if (recipe == waterBurnRecipe && isCurrentlyDoingRecipe(waterBurnRecipe)) return false;
        if (recipe == waterBurnRecipe && temperature < 600) return false;
        if (recipe == waterBurnRecipe2 && isCurrentlyDoingRecipe(waterBurnRecipe2)) return false;
        if (recipe == waterBurnRecipe2 && temperature < 600) return false;
        if (recipe == waterBurnRecipe3 && isCurrentlyDoingRecipe(waterBurnRecipe3)) return false;
        if (recipe == waterBurnRecipe3 && temperature < 600) return false;
        if (recipe == waterBurnRecipe4 && isCurrentlyDoingRecipe(waterBurnRecipe4)) return false;
        if (recipe == waterBurnRecipe4 && temperature < 600) return false;
        return super.canDoRecipe(recipe);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setInteger("temperature",temperature);
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket) {
        super.readCustomNBT(nbt, descPacket);
        temperature = nbt.getInteger("temperature");
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setBoolean("active",isBurningCoal());
        nbtTagCompound.setInteger("temperature",temperature);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        clientActiveBurning = pkt.getNbtCompound().getBoolean("active");
        temperature = pkt.getNbtCompound().getInteger("temperature");
    }
}
