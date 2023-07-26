package BananaFructa.TiagThings.Items;

import BananaFructa.TiagThings.TTMain;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class FluidLoaderHandler {

    public static ArrayList<Fluid> fluids = new ArrayList<>();
    private static ArrayList<Block> fluidBlocks = new ArrayList<>();
    private static ArrayList<Item> blockItems = new ArrayList<>();


    public static Fluid addLiquid(String name, String still, String flowing, Material material,boolean gas,boolean floating) {
        Fluid f = new Fluid(name,new ResourceLocation(TTMain.modId,still),new ResourceLocation(TTMain.modId,flowing));
        f.setGaseous(gas);
        if (floating) f.setDensity(-100);
        FluidRegistry.registerFluid(f);
        FluidRegistry.addBucketForFluid(f);
        fluids.add(f);
        fluidBlocks.add(new BlockFluidClassic(f,material).setRegistryName(name).setUnlocalizedName(name));
        return f;
    }

    public static Fluid addLiquidNoBucket(String name, String still, String flowing, Material material,boolean gas) {
        Fluid f = new Fluid(name,new ResourceLocation(TTMain.modId,still),new ResourceLocation(TTMain.modId,flowing));
        f.setGaseous(gas);
        FluidRegistry.registerFluid(f);
        //FluidRegistry.addBucketForFluid(f);
        fluids.add(f);
        fluidBlocks.add(new BlockFluidClassic(f,material).setRegistryName(name).setUnlocalizedName(name));
        return f;
    }

    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        final IForgeRegistry<Item> registry = event.getRegistry();

        for (Block b : fluidBlocks) {
            Item i = new ItemBlock(b).setRegistryName(((BlockFluidClassic)b).getFluid().getName());
            blockItems.add(i);
            registry.register(i);
        }

    }

    @SubscribeEvent
    public static void onBlockRegister(RegistryEvent.Register<Block> event) {
        for (Block b : fluidBlocks) {
            event.getRegistry().register(b);
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        for (int i = 0; i < fluidBlocks.size();i++) {
            ModelLoader.setCustomModelResourceLocation(blockItems.get(i), 0, new ModelResourceLocation(blockItems.get(i).getRegistryName(),"fluid"));
            ModelLoader.setCustomStateMapper(fluidBlocks.get(i),new DefaultStateMapper());
        }
    }

}
