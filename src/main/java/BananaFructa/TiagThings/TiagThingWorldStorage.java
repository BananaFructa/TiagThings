package BananaFructa.TiagThings;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.HashMap;

public class TiagThingWorldStorage extends WorldSavedData {

    public static final String dataName = TTMain.modId + "_WORLD_STORAGE";

    public TiagThingWorldStorage(String name) {
        super(name);
    }

    HashMap<ChunkPos, SRBlockPos> reserverHotWaterChunk = new HashMap<>();

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

    public void addReserver(ChunkPos cpos,BlockPos bpos) {
        reserverHotWaterChunk.put(cpos,new SRBlockPos(bpos));
        markDirty();
    }

    public BlockPos getReserver(ChunkPos cpos) {
        SRBlockPos srbpos = reserverHotWaterChunk.get(cpos);
        if (srbpos == null) return null;
        return srbpos.toBlockPos();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {

        Gson gson = new Gson();

        try {
            reserverHotWaterChunk = gson.fromJson(nbt.getString("hotWaterReservers"),new TypeToken<HashMap<ChunkPos,SRBlockPos>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        Gson gson = new Gson();

        try {
            compound.setString("hotWaterReservers", gson.toJson(reserverHotWaterChunk));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compound;
    }
}
