package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.Particles.SmokeParticle;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.*;
import BananaFructa.TiagThings.TTMain;
import BananaFructa.TiagThings.Utils;
import nc.init.NCFluids;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TileEntityCokeOvenBattery extends SimplifiedTileEntityMultiblockMetal<TileEntityCokeOvenBattery, SimplifiedMultiblockRecipe> {

    public static String[] logs = {
            "<tfc:wood/log/acacia>",
            "<tfc:wood/log/ash>" ,
            "<tfc:wood/log/aspen>" ,
            "<tfc:wood/log/birch>" ,
            "<tfc:wood/log/blackwood>",
            "<tfc:wood/log/chestnut>" ,
            "<tfc:wood/log/douglas_fir>" ,
            "<tfc:wood/log/hickory>" ,
            "<tfc:wood/log/kapok>" ,
            "<tfc:wood/log/maple>" ,
            "<tfc:wood/log/oak>" ,
            "<tfc:wood/log/palm>" ,
            "<tfc:wood/log/pine>",
            "<tfc:wood/log/rosewood>" ,
            "<tfc:wood/log/sequoia>" ,
            "<tfc:wood/log/spruce>" ,
            "<tfc:wood/log/sycamore>" ,
            "<tfc:wood/log/white_cedar>" ,
            "<tfc:wood/log/willow>",
            "<tfc:wood/log/african_padauk>",
            "<tfc:wood/log/angelim>",
            "<tfc:wood/log/box>",
            "<tfc:wood/log/brazilwood>",
            "<tfc:wood/log/cocobolo>",
            "<tfc:wood/log/ebony>",
            "<tfc:wood/log/fever>",
            "<tfc:wood/log/fruitwood>",
            "<tfc:wood/log/greenheart>",
            "<tfc:wood/log/holly>",
            "<tfc:wood/log/iroko>",
            "<tfc:wood/log/ironwood>",
            "<tfc:wood/log/kauri>",
            "<tfc:wood/log/limba>",
            "<tfc:wood/log/logwood>",
            "<tfc:wood/log/mahoe>",
            "<tfc:wood/log/marblewood>",
            "<tfc:wood/log/messmate>",
            "<tfc:wood/log/mountain_ash>",
            "<tfc:wood/log/purpleheart>",
            "<tfc:wood/log/rubber_fig>",
            "<tfc:wood/log/teak>",
            "<tfc:wood/log/wenge>",
            "<tfc:wood/log/yellow_meranti>",
            "<tfc:wood/log/zebrawood>",
            "<tfc:wood/log/hemlock>",
            "<tfc:wood/log/nordmann_fir>",
            "<tfc:wood/log/norway_spruce>",
            "<tfc:wood/log/redwood>",
            "<tfc:wood/log/baobab>",
            "<tfc:wood/log/eucalyptus>",
            "<tfc:wood/log/hawthorn>",
            "<tfc:wood/log/maclura>",
            "<tfc:wood/log/mahogany>",
            "<tfc:wood/log/pink_ivory>",
            "<tfc:wood/log/red_cedar>",
            "<tfc:wood/log/rowan>",
            "<tfc:wood/log/syzygium>",
            "<tfc:wood/log/yew>",
            "<tfc:wood/log/jacaranda>",
            "<tfc:wood/log/juniper>",
            "<tfc:wood/log/ipe>",
            "<tfc:wood/log/pink_cherry>",
            "<tfc:wood/log/white_cherry>",
            "<tfc:wood/log/sweetgum>",
            "<tfc:wood/log/larch>",
            "<tfc:wood/log/alder>",
            "<tfc:wood/log/beech>",
            "<tfc:wood/log/black_walnut>",
            "<tfc:wood/log/butternut>",
            "<tfc:wood/log/cypress>",
            "<tfc:wood/log/european_oak>",
            "<tfc:wood/log/ginkgo>",
            "<tfc:wood/log/hazel>",
            "<tfc:wood/log/hornbeam>",
            "<tfc:wood/log/locust>",
            "<tfc:wood/log/poplar>",
            "<tfc:wood/log/red_elm>",
            "<tfc:wood/log/walnut>",
            "<tfc:wood/log/white_elm>",
            "<tfc:wood/log/whitebeam>",
            "<tfc:wood/log/hevea>",
            "<tfcflorae:wood/log/argyle_eucalyptus>",
            "<tfcflorae:wood/log/rainbow_eucalyptus>",
            "<tfcflorae:wood/log/snow_gum_eucalyptus>",
            "<tfcflorae:wood/log/joshua_tree>"
    };

    public static List<SimplifiedMultiblockRecipe> coalBurnRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<minecraft:coal:1>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,50*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/coke>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,50*10,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/jet>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,333,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/lignite>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,50*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:ore/bituminous_coal>"),ItemStack.EMPTY},new FluidStack[0],new ItemStack[0],new FluidStack[0],0,50*40,true));
    }};

    public static List<SimplifiedMultiblockRecipe> liquidBurnRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:diesel>"),20)},new ItemStack[0],new FluidStack[0],0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:gasoline>"),20)},new ItemStack[0],new FluidStack[0],0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:butane>"),20)},new ItemStack[0],new FluidStack[0],0,20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[0],new FluidStack[]{new FluidStack(Utils.fluidFromCTId("<liquid:biodiesel>"),20)},new ItemStack[0],new FluidStack[0],0,20,true));
    }};

    public static List<SimplifiedMultiblockRecipe> burnRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        addAll(coalBurnRecipes);
        addAll(liquidBurnRecipes);
    }};

    public static List<SimplifiedMultiblockRecipe> cokeRecipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:ore/jet>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/coke>",3)},new FluidStack[0],0,12*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:ore/lignite>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/coke>",5)},new FluidStack[0],0,12*20,true));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId("<tfc:ore/bituminous_coal>")},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<tfc:powder/coke>",8)},new FluidStack[0],0,12*20,true));
        for (String log : logs) {
            add(new SimplifiedMultiblockRecipe(new ItemStack[]{ItemStack.EMPTY,Utils.itemStackFromCTId(log)},new FluidStack[0], new ItemStack[]{Utils.itemStackFromCTId("<minecraft:coal:1>",2)},new FluidStack[0],0,12*20,true));
        }
    }};

    int temperature = 0;

    boolean clientActiveBurning = false;
    float clientBurnPosX,clientBurnPosY,clientBurnPosZ;
    boolean wasBurning = false;

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        addAll(burnRecipes);
        addAll(cokeRecipes);
    }};
    public TileEntityCokeOvenBattery() {
        super(TTIEContent.cokeOvenBattery, 0, false,recipes);
    }

    @Override
    public void update() {
        super.update();
        if (world.isRemote && clientActiveBurning) {
            for (int i = 0;i < 5;i++) Minecraft.getMinecraft().effectRenderer.addEffect(new SmokeParticle(world,clientBurnPosX + (world.rand.nextFloat() - 0.5)*0.3,clientBurnPosY+ (world.rand.nextFloat() - 0.5)*0.3+1,clientBurnPosZ+ (world.rand.nextFloat() - 0.5)*0.3,world.rand.nextFloat() * 0.05,world.rand.nextFloat() * 0.1 +0.2,world.rand.nextFloat() * 0.05,(int)(10*20*(0.5+world.rand.nextInt(2))),2+world.rand.nextFloat()*2));
        }
        if (world.isRemote || isDummy()) return;
        boolean isBurning = isBurning();
        if ((temperature != 0 && temperature != 1000) || (isBurning ^ wasBurning)) world.notifyBlockUpdate(getPos(),world.getBlockState(getPos()),world.getBlockState(getPos()),2);
        wasBurning = isBurning;
        if (isBurning) {
            if (!isRSDisabled()) temperature++;
        }
        else temperature--;
        temperature = Math.max(Math.min(temperature,1000),0);
    }

    @Override
    public void initPorts() {
        int solidFuelIn = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(1326,solidFuelIn,PortType.INPUT,EnumFacing.SOUTH);
        int fluidFuelIn = registerFluidTank(1000);
        registerFluidPort(1325,fluidFuelIn,PortType.INPUT,EnumFacing.SOUTH);
        int itemsIn = registerItemHandler(1,new boolean[]{true}, new boolean[]{true});
        registerItemPort(1266,itemsIn,PortType.INPUT,EnumFacing.NORTH);
        registerItemPort(156,-1,PortType.OUTPUT,EnumFacing.NORTH);
    }
    public boolean isBurning() {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : burnRecipes) if(isCurrentlyDoingRecipe(coalBurnRecipe)) return true;
        return false;
    }

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        if (isBurning() && burnRecipes.contains(recipe)) return false;
        if (cokeRecipes.contains(recipe)) {
            if (temperature < 1000) return false;
            for (SimplifiedMultiblockRecipe cokeRecipe : cokeRecipes) {
                if (isCurrentlyDoingRecipe(cokeRecipe)) return false;
            }
        }
        return super.canDoRecipe(recipe);
    }

    // Would coking be the verb ?
    public boolean isCoking() {
        for (SimplifiedMultiblockRecipe cokeRecipe : cokeRecipes) if(isCurrentlyDoingRecipe(cokeRecipe)) return true;
        return false;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setBoolean("active",isBurning());
        BlockPos smokePos = getBlockPosForPos(2865);
        nbtTagCompound.setFloat("smokePosx",smokePos.getX() + 0.5f);
        nbtTagCompound.setFloat("smokePosy",smokePos.getY() + 0.5f);
        nbtTagCompound.setFloat("smokePosz",smokePos.getZ() + 0.5f);
        nbtTagCompound.setInteger("temperature",temperature);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        readCustomNBT(pkt.getNbtCompound(),true);
        clientActiveBurning = pkt.getNbtCompound().getBoolean("active");
        clientBurnPosX = pkt.getNbtCompound().getFloat("smokePosx");
        clientBurnPosY = pkt.getNbtCompound().getFloat("smokePosy");
        clientBurnPosZ = pkt.getNbtCompound().getFloat("smokePosz");
        temperature = pkt.getNbtCompound().getInteger("temperature");
    }

    public int getTemperature() {
        return temperature;
    }

    public int getProcessQueueMaxLength() {
        return 2;
    }

    @Override
    public int getMaxProcessPerTick() {
        return 2;
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
                rest = blusunrize.immersiveengineering.common.util.Utils.insertStackIntoInventory(te,rest,info.face.getOpposite());
            }
        }
        if (!rest.isEmpty()) {
            BlockPos pos = getBlockPosForPos(info.pos + world.rand.nextInt(12));
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

    public float getRemainingFuel() {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : coalBurnRecipes) {
            MultiblockProcess process = getProcessForRecipe(coalBurnRecipe);
            if (process == null) continue;
            return 1-(float)process.processTick/process.maxTicks;
        }
        return 0.0f;
    }

    public float getCokeProcess() {
        for (SimplifiedMultiblockRecipe coalBurnRecipe : cokeRecipes) {
            MultiblockProcess process = getProcessForRecipe(coalBurnRecipe);
            if (process == null) continue;
            return (float)process.processTick/process.maxTicks;
        }
        return 0.0f;
    }

}
