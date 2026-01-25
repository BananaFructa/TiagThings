package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.Particles.SmokeParticle;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedTileEntityMultiblockMetal;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import blusunrize.immersiveengineering.common.IEContent;
import com.eerussianguy.firmalife.registry.ItemsFL;
import mods.railcraft.common.fluids.RailcraftFluids;
import net.dries007.tfc.api.types.Ore;
import net.dries007.tfc.objects.Powder;
import net.dries007.tfc.objects.ToolMaterialsTFC;
import net.dries007.tfc.objects.items.ItemGem;
import net.dries007.tfc.objects.items.ItemPowder;
import net.dries007.tfc.objects.items.ItemsTFC;
import net.dries007.tfc.objects.items.metal.ItemOreTFC;
import net.dries007.tfc.types.DefaultMetals;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import tfcflorae.objects.GemTFCF;
import tfcflorae.objects.PowderTFCF;
import tfcflorae.util.fuel.FuelsTFCF;
import net.minecraft.item.Item;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityCoalBoiler extends SimplifiedTileEntityMultiblockMetal<TileEntityCoalBoiler, SimplifiedMultiblockRecipe> {


    public static final SimplifiedMultiblockRecipe coalBurnRecipe1 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:bituminous_coal_powder>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],2048,20 * 60 * 4,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe2 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:lignite_powder>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],2048,20 * 60 * 2,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe3 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:jet_powder>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],2048,20 * 40,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe4 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:coke_powder>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],2048,20 * 60,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe5 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/charcoal>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],2048,20 * 60 * 2,true);
    public static final SimplifiedMultiblockRecipe coalBurnRecipe6 = new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:petcoke_powder>")},new FluidStack[0],new ItemStack[0],new FluidStack[0],2048,20 * 60,true);

    public static final SimplifiedMultiblockRecipe waterBurnRecipe = new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(TTMain.treatedWater,100)},new ItemStack[0],new FluidStack[]{new FluidStack(RailcraftFluids.STEAM.getBlock().getFluid(), 200)},0,2);

    public static List<SimplifiedMultiblockRecipe> burnRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(coalBurnRecipe1);
        add(coalBurnRecipe2);
        add(coalBurnRecipe3);
        add(coalBurnRecipe4);
        add(coalBurnRecipe5);
        add(coalBurnRecipe6);
    }};

    int temperature = 0;
    public int redstoneLevel = 0;
    boolean clientActiveBurning = false;
    float clientBurnPosX,clientBurnPosY,clientBurnPosZ;

    boolean wasBurning = false;

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(coalBurnRecipe1);
        add(coalBurnRecipe2);
        add(coalBurnRecipe3);
        add(coalBurnRecipe4);
        add(coalBurnRecipe5);
        add(waterBurnRecipe);
    }};

    public TileEntityCoalBoiler() {
        super(TTIEContent.coalBoilerMultiblock, 16000, true,recipes);
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote && clientActiveBurning) {
            for (int i = 0;i < 1;i++) {
                //world.spawnParticle(EnumParticleTypes.SMOKE_LARGE,true,clientBurnPosX + world.rand.nextFloat() - 0.5,clientBurnPosY+ world.rand.nextFloat() - 0.5,clientBurnPosZ+ world.rand.nextFloat() - 0.5,0,world.rand.nextFloat() * 2,0);
                Minecraft.getMinecraft().effectRenderer.addEffect(new SmokeParticle(world,clientBurnPosX + world.rand.nextFloat() - 0.5,clientBurnPosY+ world.rand.nextFloat() - 0.5,clientBurnPosZ+ world.rand.nextFloat() - 0.5,world.rand.nextFloat() * 0.05,world.rand.nextFloat() * 0.1 +0.2,world.rand.nextFloat() * 0.05,(int)(10*20*(0.5+world.rand.nextInt(2))),2+world.rand.nextFloat()*2));
            }
        }
        if (world.isRemote || isDummy()) return;
        redstoneLevel = getRedstoneInLevel();
        boolean isBurning = isBurningCoal();
        if ((temperature != 0 && temperature != 600) || (isBurning ^ wasBurning)) world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
        wasBurning = isBurning;
        if (isBurning) {
            if (!isRSDisabled()) temperature++;
        }
        else temperature--;
        temperature = Math.max(Math.min(temperature,600),0);
    }

    @Override
    public void initPorts() {
        int inputItem = registerItemHandler(1, new boolean[]{true}, new boolean[]{false});
        int inputFluid = registerFluidTank(1000);
        int outputFluid = registerFluidTank(1000);
        addEnergyPort(479);
        addRedstonePorts(76);
        registerItemPort(56, inputItem,PortType.INPUT, EnumFacing.EAST);
        registerFluidPort(75, inputFluid, PortType.INPUT, EnumFacing.NORTH);
        registerFluidPort(366, outputFluid, PortType.OUTPUT, EnumFacing.SOUTH);
    }

    @Override
    public void onRecipeTick(SimplifiedMultiblockRecipe recipe) {
        super.onRecipeTick(recipe);
    }

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : burnRecipes) {
            if (recipe == coalBurnRecipe && isProcessInQueue(coalBurnRecipe)) return false;
        }
        if (recipe == waterBurnRecipe && isProcessInQueue(waterBurnRecipe)) return false;
        if (recipe == waterBurnRecipe && temperature < 600) return false;
        return super.canDoRecipe(recipe);
    }

    public boolean isBurningCoal() {
        boolean burning = false;
        for (SimplifiedMultiblockRecipe coalBurnRecipe : burnRecipes) burning |= isCurrentlyDoingRecipe(coalBurnRecipe);
        return burning;
    }

    @Override
    public int getProcessQueueMaxLength() {
        return 2;
    }

    @Override
    public int getMaxProcessPerTick() {
        return 2;
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
        BlockPos smokePos = getBlockPosForPos(2483).add(getBlockPosForPos(2482)).add(getBlockPosForPos(2490)).add(getBlockPosForPos(2491));
        nbtTagCompound.setFloat("smokePosx",smokePos.getX() / 4.0f + 0.5f);
        nbtTagCompound.setFloat("smokePosy",smokePos.getY() / 4.0f + 0.5f);
        nbtTagCompound.setFloat("smokePosz",smokePos.getZ() / 4.0f + 0.5f);
        nbtTagCompound.setInteger("temperature",temperature);
        nbtTagCompound.setInteger("redstoneControl",redstoneLevel);
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
        redstoneLevel = pkt.getNbtCompound().getInteger("redstoneControl");
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public boolean isRSDisabled() {
        if ((world.getTotalWorldTime() % 15 >= redstoneLevel && !redstoneControlInverted) || (world.getTotalWorldTime() % 15 < redstoneLevel && redstoneControlInverted)) return false;
        return true;
    }

    public int getRedstoneInLevel() {
        int[] rsPositions = getRedstonePos();
        if(rsPositions==null || rsPositions.length<1)
            return (redstoneControlInverted ? 15 : 0);
        for(int rsPos : rsPositions)
        {
            TileEntityCoalBoiler tile = this.getTileForPos(rsPos);
            if(tile!=null)
            {
                return  (redstoneControlInverted ? 15-world.isBlockIndirectlyGettingPowered(tile.getPos()) : world.isBlockIndirectlyGettingPowered(tile.getPos()));
            }
        }
        return (redstoneControlInverted ? 15 : 0);
    }

    @Override
    public boolean isCurrentlyDoingRecipe(SimplifiedMultiblockRecipe recipe) {
        if ((!redstoneControlInverted && redstoneLevel == 15) || (redstoneControlInverted && redstoneLevel == 0)) return false;
        for (MultiblockProcess<SimplifiedMultiblockRecipe> process : this.processQueue) {
            if (process.recipe == recipe && process.canProcess(this)) return true;
        }
        return false;
    }

    public boolean isProcessInQueue(SimplifiedMultiblockRecipe recipe) {
        for (MultiblockProcess<SimplifiedMultiblockRecipe> process : this.processQueue) {
            if (process.recipe == recipe) return true;
        }
        return false;
    }

    @Override // TODO: redstone inversion for boilers
    public boolean hammerUseSide(EnumFacing side, EntityPlayer player, float hitX, float hitY, float hitZ) {
        return false;
    }
}
