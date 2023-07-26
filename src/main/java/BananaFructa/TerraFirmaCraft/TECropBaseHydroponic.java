package BananaFructa.TerraFirmaCraft;

import net.dries007.tfc.api.types.ICrop;
import net.dries007.tfc.objects.blocks.agriculture.BlockCropTFC;
import net.dries007.tfc.objects.te.TECropBase;

import java.util.ArrayList;
import java.util.List;

public class TECropBaseHydroponic extends TECropBase {

    @Override
    public long getTicksSinceUpdate() {
        return 0; // stops it from growing
    }

}
