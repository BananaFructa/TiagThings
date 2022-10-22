package BananaFructa.TiagThings;

import BananaFructa.RailcraftModifications.RFTileBlockCrusher;
import BananaFructa.TiagThings.Items.FluidLoaderHandler;
import BananaFructa.TiagThings.Proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = TTMain.modId,version = TTMain.version,name = TTMain.name,dependencies = "after:railcraft;after:buildcraftbuilders")
public class TTMain {

    // There are no recipes present for the items as they are handled by craft twaker in the modpack

    public static final String modId = "tiagthings";
    public static final String name = "Tiag Things";
    public static final String version = "1.2.4";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FluidLoaderHandler.addLiquid("cooled_lava_mixture","blocks/cooled_lava_mixture_still","blocks/cooled_lava_mixture_flow", Material.WATER);
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        Coke.registerFuel();
        proxy.init();
        GameRegistry.registerTileEntity(RFTileBlockCrusher.class,new ResourceLocation(modId, RFTileBlockCrusher.class.getSimpleName()));
    }

    @SidedProxy(modId = TTMain.modId,clientSide = "BananaFructa.TiagThings.Proxy.ClientProxy",serverSide = "BananaFructa.TiagThings.Proxy.CommonProxy")
    public static CommonProxy proxy;

}
