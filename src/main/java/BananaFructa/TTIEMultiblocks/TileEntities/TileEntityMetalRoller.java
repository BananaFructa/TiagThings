package BananaFructa.TTIEMultiblocks.TileEntities;

import BananaFructa.TTIEMultiblocks.TTBlockTypes_MetalMultiblock_3;
import BananaFructa.TTIEMultiblocks.TTIEContent;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.PortType;
import BananaFructa.TTIEMultiblocks.Utils.SimplifiedMultiblockRecipe;
import BananaFructa.TiagThings.Utils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import pl.pabilo8.immersiveintelligence.api.rotary.IRotaryEnergy;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TileEntityMetalRoller extends AnimatedOBJTileEntity<TileEntityMetalRoller, SimplifiedMultiblockRecipe>{

    private static String[] plates = {
            "<tfc:metal/sheet/bismuth>",
            "<tfc:metal/sheet/bismuth_bronze>",
            "<tfc:metal/sheet/black_bronze>",
            "<tfc:metal/sheet/brass>",
            "<tfc:metal/sheet/bronze>",
            "<tfc:metal/sheet/copper>",
            "<tfc:metal/sheet/gold>",
            "<tfc:metal/sheet/lead>",
            "<tfc:metal/sheet/nickel>",
            "<tfc:metal/sheet/rose_gold>",
            "<tfc:metal/sheet/silver>",
            "<tfc:metal/sheet/tin>",
            "<tfc:metal/sheet/zinc>",
            "<tfc:metal/sheet/sterling_silver>",
            "<tfc:metal/sheet/wrought_iron>",
            "<tfc:metal/sheet/pig_iron>",
            "<tfc:metal/sheet/steel>",
            "<tfc:metal/sheet/platinum>",
            //"<tfc:metal/sheet/black_steel>",
            //"<tfc:metal/sheet/blue_steel>",
            //"<tfc:metal/sheet/red_steel>",
            "<tfc:metal/sheet/aluminium>",
            "<tfc:metal/sheet/constantan>",
            "<tfc:metal/sheet/electrum>",
            "<tfc:metal/sheet/titanium>",
            "<tfc:metal/sheet/tungsten>",
            "<tfc:metal/sheet/boron>",
            "<tfc:metal/sheet/duraluminium>",
            "<tfc:metal/sheet/advanced_electronic_alloy>",
            "<tfc:metal/sheet/zirconium>",
            "<tfc:metal/sheet/magnesium>",
            "<tfc:metal/sheet/lithium>",
            "<tfc:metal/sheet/manganese>",
            "<tfc:metal/sheet/beryllium>",
            "<tfc:metal/sheet/mild_steel>",
            "<tfc:metal/sheet/crucible_steel>"
    };

    private static String[] doubles = {
            "<tfc:metal/double_ingot/bismuth>",
            "<tfc:metal/double_ingot/bismuth_bronze>",
            "<tfc:metal/double_ingot/black_bronze>",
            "<tfc:metal/double_ingot/brass>",
            "<tfc:metal/double_ingot/bronze>",
            "<tfc:metal/double_ingot/copper>",
            "<tfc:metal/double_ingot/gold>",
            "<tfc:metal/double_ingot/lead>",
            "<tfc:metal/double_ingot/nickel>",
            "<tfc:metal/double_ingot/rose_gold>",
            "<tfc:metal/double_ingot/silver>",
            "<tfc:metal/double_ingot/tin>",
            "<tfc:metal/double_ingot/zinc>",
            "<tfc:metal/double_ingot/sterling_silver>",
            "<tfc:metal/double_ingot/wrought_iron>",
            "<tfc:metal/double_ingot/pig_iron>",
            "<tfc:metal/double_ingot/steel>",
            "<tfc:metal/double_ingot/platinum>",
            //"<tfc:metal/double_ingot/black_steel>",
            //"<tfc:metal/double_ingot/blue_steel>",
            //"<tfc:metal/double_ingot/red_steel>",
            "<tfc:metal/double_ingot/aluminium>",
            "<tfc:metal/double_ingot/constantan>",
            "<tfc:metal/double_ingot/electrum>",
            "<tfc:metal/double_ingot/titanium>",
            "<tfc:metal/double_ingot/tungsten>",
            "<tfc:metal/double_ingot/boron>",
            "<tfc:metal/double_ingot/duraluminium>",
            "<tfc:metal/double_ingot/advanced_electronic_alloy>",
            "<tfc:metal/double_ingot/zirconium>",
            "<tfc:metal/double_ingot/magnesium>",
            "<tfc:metal/double_ingot/lithium>",
            "<tfc:metal/double_ingot/manganese>",
            "<tfc:metal/double_ingot/beryllium>",
            "<tfc:metal/double_ingot/mild_steel>",
            "<tfc:metal/double_ingot/crucible_steel>"
    };

    public static List<SimplifiedMultiblockRecipe> recipes = new ArrayList<SimplifiedMultiblockRecipe>() {{
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:2>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveintelligence:material_spring:2>")},new FluidStack[0],0,1));
        for (int i = 0;i < plates.length;i++) {
            add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId(doubles[i])},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId(plates[i])},new FluidStack[0],0,1));
        }
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/crucible_steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<railcraft:rail>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<railcraft:rail>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/ingot/copper>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<railcraft:rail:5>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersiveintelligence:material_rod>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveintelligence:material_spring>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<immersiveengineering:material:1>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveintelligence:material_spring:1>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/sheet/wrought_iron>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<minecraft:bucket>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tiagthings:mild_steel_rod>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<immersiveintelligence:material_spring:1>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/sheet/mild_steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<minecraft:bucket>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/sheet/crucible_steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<minecraft:hopper>")},new FluidStack[0],0,1));
        add(new SimplifiedMultiblockRecipe(new ItemStack[]{Utils.itemStackFromCTId("<tfc:metal/sheet/steel>")},new FluidStack[0],new ItemStack[]{Utils.itemStackFromCTId("<minecraft:hopper>")},new FluidStack[0],0,1));
    }};

    public float inSpeed = 0;
    public float inTorque = 0;


    public TileEntityMetalRoller() {
        super(TTIEContent.metalRoller, 0, false,recipes,TTIEContent.ttBlockMetalMultiblock_3.getStateFromMeta(TTBlockTypes_MetalMultiblock_3.METAL_ROLLER.getMeta()));
        setAnimation("running");
        setAnimationSpeed(0);
    }

    @Override
    public void initPorts() {
        int rotaryIn = registerRotaryStorage();
        int itemInput = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        int itemOutput = registerItemHandler(1,new boolean[]{true},new boolean[]{true});
        registerItemPort(1,itemInput,PortType.INPUT,EnumFacing.EAST);
        registerItemPort(1,itemOutput,PortType.OUTPUT,EnumFacing.WEST);
        registerRotaryPort(2,rotaryIn,PortType.INPUT,EnumFacing.WEST);
    }

    @Override
    public void update() {
        super.update();
        if (rotaryEnergies.size() > 0) {
            IRotaryEnergy re = rotaryEnergies.get(0);
            if (re != null) {
                if (re.getRotationSpeed() / 60.0f != speed || inTorque != re.getOutputTorque()) IEUtils.notifyClientUpdate(world, pos);
                if (re.getRotationSpeed() > 0) {
                    speed = re.getRotationSpeed()/60.0f;
                    inTorque = re.getOutputTorque();
                }
                else {
                    speed -= 0.02;
                    inTorque = 0;
                }
                if (speed < 0) speed = 0;
                setAnimationSpeed(speed);
                inSpeed = re.getRotationSpeed();
            } else {
                setAnimationSpeed(0);
                IEUtils.notifyClientUpdate(world, pos);
                inSpeed = 0;
                inTorque = 0;
            }
        }
    }

    @Override
    public boolean canDoRecipe(SimplifiedMultiblockRecipe recipe) {
        if (inventoryHandlers.size() > 0) {
            ItemStack is = inventoryHandlers.get(0).getStackInSlot(0);
            if (is.isEmpty()) return false;
            if (!is.hasTagCompound()) return false;
            NBTTagCompound compound = is.getTagCompound();
            if (!compound.hasKey("roller")) return false;
            NBTTagCompound roller = compound.getCompoundTag("roller");
            if (!roller.hasKey("progress")) return false;
            return roller.getFloat("progress") >= 1;
        }
        return false;
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        this.writeCustomNBT(nbtTagCompound, true);
        nbtTagCompound.setString("animationName",currentAnimationName);
        nbtTagCompound.setFloat("animationSpeed",speed);
        nbtTagCompound.setFloat("inTorque",inTorque);
        nbtTagCompound.setFloat("inSpeed",inSpeed);
        return new SPacketUpdateTileEntity(getPos(),1,nbtTagCompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net,pkt);
        inTorque = pkt.getNbtCompound().getFloat("inTorque");
        inSpeed = pkt.getNbtCompound().getFloat("inSpeed");
    }
}
