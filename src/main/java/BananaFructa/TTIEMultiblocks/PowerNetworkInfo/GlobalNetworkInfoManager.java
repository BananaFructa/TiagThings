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
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class GlobalNetworkInfoManager {

    public static int connectionId = 0;

    private static List<Runnable> scheduledTasks = new ArrayList<>();

    public static HashMap<UUID,List<Integer>> registeredNetworks = new HashMap<>();
    public static HashMap<UUID, NetworkData> networkData = new HashMap<>();
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
        for (UUID uuid : registeredNetworks.keySet()) {
            List<Integer> network = registeredNetworks.get(uuid);
            // Unitary changes considered only
            if (ids.size() == network.size() && compareIds(ids,network) == ids.size()) {
                // NETWORK FOUND
                networkData.get(uuid).notifyLoad(consumer,interactor);

                return;
            } else if (ids.size() > network.size() && compareIds(ids,network) == network.size()) {
                // THE NETWORK HAS MERGED
                List<Integer> other = subtractIds(ids,network);
                for (UUID otherSet : registeredNetworks.keySet()) {
                    if (registeredNetworks.get(otherSet).size() == other.size() && compareIds(other,registeredNetworks.get(otherSet)) == other.size() ) {
                        NetworkData data1 = networkData.get(uuid);
                        NetworkData data2 = networkData.get(otherSet);
                        if (data1.getActivityScore() < data2.getActivityScore()) {
                            registeredNetworks.put(otherSet,ids);
                            networkData.get(otherSet).notifyLoad(consumer,interactor);
                            addToCache(ids,uuid);
                            return;
                        }
                    }
                }
                registeredNetworks.put(uuid,ids);
                networkData.get(uuid).notifyLoad(consumer,interactor);
                addToCache(ids,uuid);
            } else if (ids.size() < network.size() && compareIds(ids,network) == ids.size()) {
                // THE NETWORK WAS SPLIT
                registeredNetworks.put(uuid,ids);
                networkData.get(uuid).notifyLoad(consumer,interactor);
                addToCache(ids,uuid);
                return;
            }
        }
        // NEW NETWORK
        if (ids.size() <= 1) return; // One connection does not make a network
        UUID newUuid = UUID.randomUUID();
        registeredNetworks.put(newUuid,ids);
        networkData.put(newUuid,new NetworkData());
        networkData.get(newUuid).notifyLoad(consumer,interactor);
        addToCache(ids,newUuid);
    }

    public static void registerNetworkTransaction(NetworkElement element, BlockPos node, World world, boolean consumer, TileEntity interactor) {
        if (interactor == null) return;
        getNetworkFromUUID(getNetworkFor(element)).registerTransfer(element.getDelta(), element.getLoss(), consumer,interactor);
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
        List<UUID> toRemove = new ArrayList<>();
        for (UUID uuid : registeredNetworks.keySet()) {
            if (registeredNetworks.get(uuid).size() == 1) toRemove.add(uuid);
        }
        // Remove all single port networks, this happens when a network was reduced to only one connection
        for (UUID uuid : toRemove) {
            registeredNetworks.remove(uuid);
            networkData.remove(uuid);
            System.out.println("NETWORK REMOVED " + networkData.size());
        }
        for (Runnable r : scheduledTasks) r.run();
        scheduledTasks.clear();
        for (UUID uuid : networkData.keySet()) {
            networkData.get(uuid).tick();
        }
        if (!networkData.isEmpty()) dirty();
    }

    public static NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("connection_id",connectionId);
        int i = 0;
        tag.setInteger("network_count",registeredNetworks.size());
        for (UUID uuid : registeredNetworks.keySet()) {
            tag.setIntArray("network_fingerprint_"+i,registeredNetworks.get(uuid).stream().mapToInt(Integer::intValue).toArray());
            tag.setTag("network_info_"+i,networkData.get(uuid).toNBT());
            tag.setUniqueId("network_uuid_" + i, uuid);
            i++;
        }
        return tag;
    }

    public static void readNBT(NBTTagCompound tag) {
        if (tag == null) {
            connectionId = 0;
        } else {
            connectionId = tag.getInteger("connection_id");
            registeredNetworks.clear();
            networkData.clear();
            cache.clear();
            int size = tag.getInteger("network_count");
            for (int i = 0;i < size;i++) {
                UUID netId = tag.getUniqueId("network_uuid_"+i);
                List<Integer> fingerprint = Arrays.stream(tag.getIntArray("network_fingerprint_"+i)).boxed().collect(Collectors.toCollection(ArrayList::new));
                //System.out.println("FINGERPRINT " + fingerprint);
                NetworkData data = NetworkData.fromNBT(tag.getCompoundTag("network_info_"+i));
                registeredNetworks.put(netId,fingerprint);
                networkData.put(netId,data);
                //System.out.println(tag.getCompoundTag("network_info_"+i));
                //System.out.println("SIZE: " + data.consumptionHistory.size());
            }
        }
    }

}
