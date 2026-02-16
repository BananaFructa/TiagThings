package BananaFructa.TiagThings;

import BananaFructa.TTIEMultiblocks.PowerNetworkInfo.GlobalNetworkInfoManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import nc.multiblock.qComputer.QuantumGate;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.lwjgl.Sys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TiagThingWorldStorage extends WorldSavedData {

    public static final String dataName = TTMain.modId + "_WORLD_STORAGE";

    public TiagThingWorldStorage(String name) {
        super(name);
    }

    // The restricted fluids are only on earth so wea don't need to account for dimension
    HashMap<String,HashMap<ChunkPos,SRBlockPos>> restrictedFluidPumps = new HashMap<>();

    public TiagThingWorldStorage() {
        super(dataName);
    }

    public static TiagThingWorldStorage get(World world) {
        MapStorage storage = world.getMapStorage();
        TiagThingWorldStorage farmingWorldStorage = (TiagThingWorldStorage)storage.getOrLoadData(TiagThingWorldStorage.class,dataName);
        if (farmingWorldStorage == null) {
            farmingWorldStorage = new TiagThingWorldStorage();
            storage.setData(dataName,farmingWorldStorage);
        }
        return farmingWorldStorage;
    }

    public void addReserver(String fluid, ChunkPos cpos,BlockPos bpos) {
        if (!restrictedFluidPumps.containsKey(fluid)) restrictedFluidPumps.put(fluid,new HashMap<>());
        restrictedFluidPumps.get(fluid).put(cpos,new SRBlockPos(bpos));
        markDirty();
    }

    public BlockPos getReserver(String fluid, ChunkPos cpos) {
        if (!restrictedFluidPumps.containsKey(fluid)) return null;
        SRBlockPos srbpos = restrictedFluidPumps.get(fluid).get(cpos);
        if (srbpos == null) return null;
        return srbpos.toBlockPos();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        try {
            int fluidCount = nbt.getInteger("fluidSize");
            for (int i =0;i < fluidCount;i++) {
                String key = nbt.getString("fluid-"+i);
                HashMap<ChunkPos,SRBlockPos> value = nbtToHashMap((NBTTagCompound) nbt.getTag("hm-"+i));
                restrictedFluidPumps.put(key,value);
            }
            GlobalNetworkInfoManager.readNBT(nbt.getCompoundTag("power_network_info"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {

        try {
            compound.setInteger("fluidSize",restrictedFluidPumps.keySet().size());
            int i = 0;
            for (String s : restrictedFluidPumps.keySet()) {
                compound.setString("fluid-"+i,s);
                compound.setTag("hm-"+i,hashMapToNBT(restrictedFluidPumps.get(s)));
                i++;
            }
            compound.setTag("power_network_info", GlobalNetworkInfoManager.toNBT());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compound;
    }

    private NBTTagCompound hashMapToNBT(HashMap<ChunkPos,SRBlockPos> hm) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("size",hm.size());
        int i = 0;
        for (ChunkPos key : hm.keySet()) {
            nbt.setTag("key"+i,key.toNbt());
            nbt.setTag("value"+i,hm.get(key).toNbt());
            i++;
        }
        return nbt;
    }

    private HashMap<ChunkPos,SRBlockPos> nbtToHashMap(NBTTagCompound nbt) {
        HashMap<ChunkPos,SRBlockPos> hm = new HashMap<>();
        int size = nbt.getInteger("size");
        for (int i = 0;i < size;i++){
            ChunkPos key = ChunkPos.fromNBT((NBTTagCompound) nbt.getTag("key"+i));
            SRBlockPos value = SRBlockPos.fromNBT((NBTTagCompound) nbt.getTag("value"+i));
            hm.put(key,value);
        }
        return hm;
    }
}
