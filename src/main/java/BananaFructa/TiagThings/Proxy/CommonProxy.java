package BananaFructa.TiagThings.Proxy;

import BananaFructa.BCModifications.RFBlockQuarry;
import BananaFructa.BCModifications.RFTileQuarry;
import BananaFructa.RailcraftModifications.RFBlockRockCrusher;
import BananaFructa.TiagThings.Utils;
import buildcraft.builders.BCBuildersBlocks;
import buildcraft.builders.block.BlockQuarry;
import buildcraft.lib.registry.RegistrationHelper;
import mods.railcraft.common.blocks.ItemBlockEntityDelegate;
import mods.railcraft.common.blocks.RailcraftBlocks;
import mods.railcraft.common.fluids.Fluids;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import org.lwjgl.Sys;
import tfcflorae.objects.items.ItemsTFCF;
import tfcflorae.objects.items.food.ItemFoodTFCF;
import tfcflorae.util.agriculture.CropTFCF;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraftforge.fml.common.eventhandler.EventPriority.LOW;

public class CommonProxy {

    public CommonProxy() {
        MinecraftForge.EVENT_BUS.register(this);

        try {
            Object def = Utils.readDeclaredField(RailcraftBlocks.ROCK_CRUSHER.getClass(),RailcraftBlocks.ROCK_CRUSHER,"def");
            Class<?> blockDef = Class.forName("mods.railcraft.common.blocks.RailcraftBlocks$BlockDef");
            Function f = new Function() {
                @Override
                public Object apply(Object o) {
                    return new ItemBlockEntityDelegate<>((RFBlockRockCrusher) o);
                }
            };
            Constructor constructor = blockDef.getConstructors()[0];
            constructor.setAccessible(true);
            Object o = constructor.newInstance(RailcraftBlocks.ROCK_CRUSHER,RailcraftBlocks.ROCK_CRUSHER,"rock_crusher", RFBlockRockCrusher.class,(Supplier)null,f,(Supplier)null);
            Utils.writeDeclaredField(RailcraftBlocks.ROCK_CRUSHER.getClass(),RailcraftBlocks.ROCK_CRUSHER, "def",o,true);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void preInit() {

        //RegistryConfig.setRegistryConfig(TTMain.modId,)
        RegistrationHelper HELPER = Utils.readDeclaredField(BCBuildersBlocks.class,null,"HELPER");
        List<Block> bcBlocks = Utils.readDeclaredField(RegistrationHelper.class,HELPER,"blocks");
        List<Item> bcItems = Utils.readDeclaredField(RegistrationHelper.class,HELPER,"items");
        Block target = null;
        Item targetI = null;
        for (Block b : bcBlocks) {
            if (b instanceof BlockQuarry) {
                target = b;
                break;
            }
        }
        for (Item i : bcItems) {
            if (i instanceof ItemBlock) {
                if (((ItemBlock)i).getBlock() instanceof BlockQuarry) {
                    targetI = i;
                    break;
                }
            }
        }
        if (target != null) {
            bcBlocks.remove(target);
        }

        if (targetI != null) {
            bcItems.remove(targetI);
        }
        //       enforces short circuit evaluation to bypass the needed config or smth at RegistrationHelper.java line:130 V
        BCBuildersBlocks.quarry = (BlockQuarry) HELPER.addBlockAndItem(new RFBlockQuarry(Material.IRON,"block.quarry"),true);
    }

    public void fixCropItemSupplierTFCFlorae(Class<?> objType, Object obj,ItemFoodTFCF food) {
        Supplier<ItemStack> foodDrop = () -> {
          return new ItemStack(food);
        };
        Utils.writeDeclaredField(objType,obj,"foodDrop",foodDrop,true);
    }

    public void fixCropPartialItemSupplierTFCFlorae(Class<?> objType, Object obj,ItemFoodTFCF food) {
        Supplier<ItemStack> foodDrop = () -> {
            return new ItemStack(food);
        };
        Utils.writeDeclaredField(objType,obj,"foodDropEarly",foodDrop,true);
    }

    public void init() {

        RegistrationHelper HELPER = Utils.readDeclaredField(BCBuildersBlocks.class,null,"HELPER");
        HELPER.registerTile(RFTileQuarry.class,"tile.quarry");

        // Makes it so that railcraft uses tfc fresh water instead of vanilla water
        Utils.writeDeclaredField(Fluids.WATER.getClass(),Fluids.WATER,"tag","fresh_water",true);

        // Creates an identity provider for the crop drop TFC Florae
        // Idk if this is due to me not wanting to add pam's harvestcraft but i really do not want it
        fixCropItemSupplierTFCFlorae(CropTFCF.AMARANTH.getClass(),CropTFCF.AMARANTH,ItemsTFCF.AMARANTH);
        fixCropItemSupplierTFCFlorae(CropTFCF.BUCKWHEAT.getClass(),CropTFCF.BUCKWHEAT,ItemsTFCF.BUCKWHEAT);
        fixCropItemSupplierTFCFlorae(CropTFCF.FONIO.getClass(),CropTFCF.FONIO,ItemsTFCF.FONIO);
        fixCropItemSupplierTFCFlorae(CropTFCF.MILLET.getClass(),CropTFCF.MILLET,ItemsTFCF.MILLET);
        fixCropItemSupplierTFCFlorae(CropTFCF.QUINOA.getClass(),CropTFCF.QUINOA,ItemsTFCF.QUINOA);
        fixCropItemSupplierTFCFlorae(CropTFCF.SPELT.getClass(),CropTFCF.SPELT,ItemsTFCF.SPELT);
        fixCropItemSupplierTFCFlorae(CropTFCF.BLACK_EYED_PEAS.getClass(),CropTFCF.BLACK_EYED_PEAS,ItemsTFCF.BLACK_EYED_PEAS);

        fixCropItemSupplierTFCFlorae(CropTFCF.CAYENNE_PEPPER.getClass(),CropTFCF.CAYENNE_PEPPER,ItemsTFCF.RED_CAYENNE_PEPPER);
        fixCropPartialItemSupplierTFCFlorae(CropTFCF.CAYENNE_PEPPER.getClass(),CropTFCF.CAYENNE_PEPPER,ItemsTFCF.GREEN_CAYENNE_PEPPER);

        fixCropItemSupplierTFCFlorae(CropTFCF.GINGER.getClass(),CropTFCF.GINGER,ItemsTFCF.GINGER);
        fixCropItemSupplierTFCFlorae(CropTFCF.GINSENG.getClass(),CropTFCF.GINSENG,ItemsTFCF.GINSENG);
        fixCropItemSupplierTFCFlorae(CropTFCF.RUTABAGA.getClass(),CropTFCF.RUTABAGA,ItemsTFCF.RUTABAGA);
        fixCropItemSupplierTFCFlorae(CropTFCF.TURNIP.getClass(),CropTFCF.TURNIP,ItemsTFCF.TURNIP);
        fixCropItemSupplierTFCFlorae(CropTFCF.SUGAR_BEET.getClass(),CropTFCF.SUGAR_BEET,ItemsTFCF.SUGAR_BEET);
        fixCropItemSupplierTFCFlorae(CropTFCF.PURPLE_GRAPE.getClass(),CropTFCF.PURPLE_GRAPE,ItemsTFCF.PURPLE_GRAPE);
        fixCropItemSupplierTFCFlorae(CropTFCF.GREEN_GRAPE.getClass(),CropTFCF.GREEN_GRAPE,ItemsTFCF.GREEN_GRAPE);
        fixCropItemSupplierTFCFlorae(CropTFCF.LIQUORICE_ROOT.getClass(),CropTFCF.LIQUORICE_ROOT,ItemsTFCF.LIQUORICE_ROOT);
    }

    @SubscribeEvent(priority = LOW)
    public void onRecipeRegister(RegistryEvent.Register<IRecipe> event) {
        String[] sandwiches = {
                "food/sandwich_slice/amaranth",
                "food/sandwich_slice/buckwheat",
                "food/sandwich_slice/fonio",
                "food/sandwich_slice/millet",
                "food/sandwich_slice/quinoa",
                "food/sandwich_slice/spelt",
                "food/sandwich_slice/wild_rice"
        };
        IForgeRegistryModifiable registry;
        registry = (IForgeRegistryModifiable)event.getRegistry();
        for (String s : sandwiches) {
            IRecipe recipe = (IRecipe)registry.getValue(new ResourceLocation("tfcflorae", s));
            if (recipe != null) {
                registry.remove(recipe.getRegistryName());
            }
        }
    }

}
