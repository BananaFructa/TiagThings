package BananaFructa.TTIEMultiblocks.PowerNetworkInfo;

import BananaFructa.TiagThings.Netowrk.CMessageUpdatePowerInfo;
import BananaFructa.TiagThings.Netowrk.TTPacketHandler;
import BananaFructa.TiagThings.TTMain;
import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;

@Mod.EventBusSubscriber
public class GlobalNetworkInfoManager {

    public static int connectionId = 0;

    private static List<Runnable> scheduledTasks = new ArrayList<>();

    public static HashMap<UUID,List<Integer>> registeredNetworks = new HashMap<>();
    public static HashMap<UUID, NetworkData> networkData = new HashMap<>();
    public static List<UUID> inactiveNetwork = new ArrayList<>();
    public static HashMap<Integer,UUID> cache = new HashMap<>();

    // <Player UUID, Network UUID>
    public static HashMap<UUID,UUID> playerUpdateSubscribers = new HashMap<>();

    public static void dirty() {
        TTMain.INSTANCE.worldStorage.markDirty();
    }

    public static int getNewId() {
        dirty();
        return connectionId++;
    }

    public static NetworkData getNetworkFromUUID(UUID networkUuid) {
        return networkData.get(networkUuid);
    }

    public static UUID getNetworkFor(NetworkElement node) {
        if (node instanceof NetworkElement) {
            int id = ((NetworkElement) node).getId();
            for (UUID uuid : registeredNetworks.keySet()) {
                if (registeredNetworks.get(uuid).contains(id)) return uuid;
            }
        }
        return null;
    }

    public static void addNetworkSubscriber(UUID player, UUID network) {
        playerUpdateSubscribers.remove(player);
        playerUpdateSubscribers.put(player,network);
    }

    public static void removeNetworkSubscriber(UUID player) {
        playerUpdateSubscribers.remove(player);
    }

    public static void notifyLoad(NetworkElement element, BlockPos node, World world, boolean consumer, TileEntity interactor) {
        if (interactor == null) return;
        if (cache.containsKey(element.getId())) {
            networkData.get(cache.get(element.getId())).notifyLoad(consumer,interactor);
            return;
        }
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(element.getId());
        Set<ImmersiveNetHandler.AbstractConnection> cons = ImmersiveNetHandler.INSTANCE.getIndirectEnergyConnections(node,world,true);
        for (ImmersiveNetHandler.AbstractConnection con : cons) {
            IImmersiveConnectable connectable = ApiUtils.toIIC(con.end,world);
            if (connectable instanceof NetworkElement && connectable != element) {
                ids.add(((NetworkElement) connectable).getId());
            }
        }
        //System.out.println(ids);
        //System.out.println("DING");
        for (UUID uuid : registeredNetworks.keySet()) {
            List<Integer> network = registeredNetworks.get(uuid);
            // Unitary changes considered only
            if (ids.size() == network.size() && compareIds(ids,network) == ids.size()) {
                //System.out.println("NETWORK FOUND");

                networkData.get(uuid).notifyLoad(consumer,interactor);
                inactiveNetwork.remove(uuid);

                return;
            } else if (ids.size() > network.size() && compareIds(ids,network) == network.size()) {
                //System.out.println("NETWORK MERGED");
                // THE NETWORK HAS EXTENDED
                List<Integer> other = subtractIds(ids,network);
                for (UUID otherSet : registeredNetworks.keySet()) {
                    if (registeredNetworks.get(otherSet).size() == other.size() && compareIds(other,registeredNetworks.get(otherSet)) == other.size() ) {
                        NetworkData data1 = networkData.get(uuid);
                        NetworkData data2 = networkData.get(otherSet);
                        if (data1.getActivityScore() < data2.getActivityScore()) {
                            //System.out.println("NETWORK CHOOSE OTHER");
                            registeredNetworks.put(otherSet,ids);
                            networkData.get(otherSet).notifyLoad(consumer,interactor);
                            inactiveNetwork.remove(otherSet);
                            addToCache(ids,uuid);
                            return;
                        }
                    }
                }
                //System.out.println("NETWORK CHOOSE FIRST");
                registeredNetworks.put(uuid,ids);
                networkData.get(uuid).notifyLoad(consumer,interactor);
                inactiveNetwork.remove(uuid);
                addToCache(ids,uuid);
            } else if (ids.size() < network.size() && compareIds(ids,network) == ids.size()) {
                //System.out.println("NETWORK SPLIT");
                // THE NETWORK WAS SPLIT
                registeredNetworks.put(uuid,ids);
                networkData.get(uuid).notifyLoad(consumer,interactor);
                inactiveNetwork.remove(uuid);
                addToCache(ids,uuid);
                return;
            }
        }
        //System.out.println("NEW NETWORK");
        UUID newUuid = UUID.randomUUID();
        registeredNetworks.put(newUuid,ids);
        inactiveNetwork.remove(newUuid);
        networkData.put(newUuid,new NetworkData());
        networkData.get(newUuid).notifyLoad(consumer,interactor);
        addToCache(ids,newUuid);
    }

    public static void registerNetworkTransaction(NetworkElement element, BlockPos node, World world, int delta, boolean consumer, TileEntity interactor) {
        if (interactor == null) return;
        getNetworkFromUUID(getNetworkFor(element)).registerTransfer(delta,consumer,interactor);
    }

    public static void addToCache(List<Integer> ids, UUID network) {
        for (Integer i : ids) cache.put(i,network);
    }

    public static int compareIds(List<Integer> first, List<Integer> second) {
        int match = 0;
        for (Integer integer : first) {
            if (second.contains(integer)) match++; // TODO: pretty sure there is a better way to do this but im too lazy rn
        }
        return match;
    }

    public static List<Integer> subtractIds(List<Integer> first, List<Integer> second) {
        List<Integer> dif = new ArrayList<>();
        for (Integer i : first) {
            if (!second.contains(i)) dif.add(i);
        }
        return dif;
    }

    public static void scheduleTask(Runnable task) {
        scheduledTasks.add(task);
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        List<UUID> toBeRemoved = new ArrayList<>();
        for (UUID playerUuid : playerUpdateSubscribers.keySet()) {
            EntityPlayerMP playerMP = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(playerUuid);
            if (playerMP == null) toBeRemoved.add(playerUuid);
            else {
                NetworkData data = networkData.get(playerUpdateSubscribers.get(playerUuid));
                if (data != null) {
                    TTPacketHandler.wrapper.sendTo(new CMessageUpdatePowerInfo(data.getUpdateDelta()), playerMP);
                }
            }
        }
        cache.clear();
        for (UUID uuid : inactiveNetwork) {
            registeredNetworks.remove(uuid);
            networkData.remove(uuid);
            System.out.println("NETWORK REMOVED " + networkData.size() + " " +inactiveNetwork.size());
        }
        for (Runnable r : scheduledTasks) r.run();
        scheduledTasks.clear();
        for (UUID uuid : networkData.keySet()) {
            networkData.get(uuid).tick();
        }
        inactiveNetwork.clear();
        inactiveNetwork.addAll(registeredNetworks.keySet());
    }

    public static NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("connection_id",connectionId);
        return tag;
    }

    public static void readNBT(NBTTagCompound tag) {
        if (tag == null) {
            connectionId = 0;
        } else {
            connectionId = tag.getInteger("connection_id");
        }
    }

}
