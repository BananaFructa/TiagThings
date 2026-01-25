package BananaFructa.TTIEMultiblocks.Utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;

import javax.sound.sampled.Port;

public class PortInfo {
    public PortType type;
    EnumFacing originalFace;
    public EnumFacing face;
    public int index = -1;
    public int pos = 0;

    public PortInfo(PortType type,EnumFacing face,int index,int pos) {
        this.type = type;
        this.originalFace = face;
        this.face = face;
        this.index = index;
        this.pos = pos;
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("type",type.ordinal());
        compound.setInteger("face",originalFace.ordinal());
        compound.setInteger("index",index);
        compound.setInteger("pos",pos);
        return compound;
    }

    public static PortInfo fromNBT(NBTTagCompound compound) {
        return new PortInfo(PortType.values()[compound.getInteger("type")],EnumFacing.values()[compound.getInteger("face")],compound.getInteger("index"),compound.getInteger("pos"));
    }

}
