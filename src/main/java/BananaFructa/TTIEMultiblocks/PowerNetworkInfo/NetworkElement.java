package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import net.minecraft.tileentity.TileEntity;

public interface NetworkElement {

    public int getId();
    public int getDelta();
    public int getLoss();
    public TileEntity getInteractor();

}
