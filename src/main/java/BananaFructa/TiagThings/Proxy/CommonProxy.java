package BananaFructa.TiagThings.Proxy;

import mods.railcraft.common.fluids.Fluids;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tfcflorae.objects.items.ItemsTFCF;
import tfcflorae.objects.items.food.ItemFoodTFCF;
import tfcflorae.util.agriculture.CropTFCF;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommonProxy {

    public CommonProxy() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static <T> T readDeclaredField(Class<?> targetType, Object target, String name) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            return (T) f.get(target);
        } catch (Exception err) {
            err.printStackTrace();
            return null;
        }
    }

    public static void writeDeclaredField(Class<?> targetType, Object target, String name, Object value,boolean final_) {
        try {
            Field f = targetType.getDeclaredField(name);
            f.setAccessible(true);
            if (final_) {
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            }
            f.set(target,value);

        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void fixCropItemSupplierTFCFlorae(Class<?> objType, Object obj,ItemFoodTFCF food) {
        Supplier<ItemStack> foodDrop = () -> {
          return new ItemStack(food);
        };
        writeDeclaredField(objType,obj,"foodDrop",foodDrop,true);
    }

    public void fixCropPartialItemSupplierTFCFlorae(Class<?> objType, Object obj,ItemFoodTFCF food) {
        Supplier<ItemStack> foodDrop = () -> {
            return new ItemStack(food);
        };
        writeDeclaredField(objType,obj,"foodDropEarly",foodDrop,true);
    }

    public void init() {
        // Makes it so that railcraft uses tfc fresh water instead of vanilla water
        writeDeclaredField(Fluids.WATER.getClass(),Fluids.WATER,"tag","fresh_water",true);

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

}
