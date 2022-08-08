package BananaFructa.TiagThings;

import BananaFructa.TiagThings.Proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import tfcflorae.util.agriculture.CropTFCF;

@Mod(modid = TTMain.modId,version = TTMain.version,name = TTMain.name)
public class TTMain {

    // There are no recipes present for the items as they are handled by craft twaker in the modpack

    public static final String modId = "tiagthings";
    public static final String name = "Tiag Things";
    public static final String version = "1.2";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event){
        Coke.registerFuel();
        proxy.init();
    }

    @SidedProxy(modId = TTMain.modId,clientSide = "BananaFructa.TiagThings.Proxy.ClientProxy",serverSide = "BananaFructa.TiagThings.Proxy.CommonProxy")
    public static CommonProxy proxy;

}
