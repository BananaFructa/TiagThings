package BananaFructa.OpenComputers;

import BananaFructa.TiagThings.TTMain;
import li.cil.oc.api.machine.Machine;
import li.cil.repack.com.naef.jnlua.LuaState;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class ResearchManager {

    private static final Map<Thread,Integer> threadTier = new IdentityHashMap<>();
    private static final Map<LuaState,Double> researchingStates = new IdentityHashMap<>();
    private static final Map<LuaState,Integer> researchedWithMemory = new IdentityHashMap<>();
    public static final Map<UUID,Long> tokens = new HashMap<>(); // Id - value
    public static final Map<UUID,ResearchTechnologies> results = new HashMap<>();
    public static final double ticksToResearch = 0.045;

    public static void cleanup() {
        synchronized (threadTier) {
            int b = threadTier.size();
            List<Thread> toRemove = new ArrayList<>();
            for (Thread t : threadTier.keySet()) {
                if (!t.isAlive()) toRemove.add(t);
            }
            for (Thread t: toRemove) threadTier.remove(t);
            //System.out.println("CLEANED UP : " + (b - threadTier.size()) + " threads");
        }
    }

    public static void addThread(Thread t, int tier) {
        synchronized (threadTier) {
            threadTier.put(t, tier);
        }
    }

    public static void removeThread(Thread t) {
        synchronized (threadTier) {
            threadTier.remove(t);
        }
    }

    public static int getTier(Thread t) {
        synchronized (threadTier) {
            if (!threadTier.containsKey(t)) return -1;
            return threadTier.get(t);
        }
    }

    public static int getResearchId(UUID researchUUID) {
        if (!results.containsKey(researchUUID)) return -1;
        return results.get(researchUUID).ordinal();
    }

    public static void addResearchState(LuaState state, Machine who) {
        researchingStates.put(state,who.cpuTime());
        researchedWithMemory.put(state,state.getTotalMemory());
    }

    public static int removeResearchState(LuaState state, Machine who) {
        if (!researchingStates.containsKey(state)) return -2;
        if (who.cpuTime() - researchingStates.get(state) < ticksToResearch) return -1;
        researchingStates.remove(state);
        int v = researchedWithMemory.getOrDefault(state,-1);
        researchedWithMemory.remove(state);
        return v;
    }

    public static boolean isResearching(LuaState state) {
        return researchingStates.containsKey(state);
    }

    public static String computeResearchCost(int tier, int memory) {
        long base = (long)Math.pow(10,tier*2);
        long amount = base * (memory/300000);
        UUID token = UUID.randomUUID();
        tokens.put(token,amount);
        TTMain.INSTANCE.worldStorage.markDirty();
        return token.toString();
    }

    public static long getValue(UUID uuid) {
        if (!tokens.containsKey(uuid)) return 0;
        return tokens.get(uuid);
    }

    public static void mergeTokens(UUID uuid1, UUID uuid2) {
        if (!tokens.containsKey(uuid1) || !tokens.containsKey(uuid2)) return;
        long v1 = tokens.get(uuid1);
        long v2 = tokens.get(uuid2);
        tokens.put(uuid1,v1 + v2);
        tokens.remove(uuid2);
        TTMain.INSTANCE.worldStorage.markDirty();
    }

    public static String getDescription(ResearchTechnologies r) {
        String id = r.desc;
        if (id == null) return "No description.";
        else return I18n.format(id);
    }

    public static String getName(ResearchTechnologies r) {
        return I18n.format(r.name);
    }

    public static long getCost(ResearchTechnologies r) {
        return r.cost;
    }

    public static boolean meetsRequirements(ResearchTechnologies tech, List<UUID> researched) {
        List<ResearchTechnologies> researchedTechs = researched.stream().map(e->results.get(e)).collect(Collectors.toList());
        ResearchTechnologies[] requiredTechnologies = tech.requirements;
        return Arrays.stream(requiredTechnologies).allMatch(e->researchedTechs.contains(e)); // Add this
    }

    public static String getUUIDForResult(ResearchTechnologies tech) {
        UUID result = UUID.randomUUID();
        results.put(result,tech);
        TTMain.INSTANCE.worldStorage.markDirty();
        return result.toString();
    }

    public static String purchaseResearch(ResearchTechnologies tech, UUID uuid, List<UUID> researched) {
        long value = getValue(uuid);
        if (value >= getCost(tech) && meetsRequirements(tech, researched)) {
            tokens.put(uuid,value - getCost(tech));
            UUID result = UUID.randomUUID();
            results.put(result,tech);
            TTMain.INSTANCE.worldStorage.markDirty();
            return result.toString();
        } else return "not_enough";
    }

    private static int counter = 100;
    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent event) {
        counter--;
        if (counter <= 0) {
            counter = 100;
            new Thread(()->{
                cleanup();
            }).start();
        }
    }

    public static NBTTagCompound toNBT() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("token_count",tokens.size());
        int i = 0;
        for (UUID key : tokens.keySet()) {
            tag.setUniqueId("token_key_"+i,key);
            tag.setLong("token_value_"+i,tokens.get(key));
            i++;
        }
        tag.setInteger("results_count",results.size());
        i = 0;
        for (UUID key : results.keySet()) {
            tag.setUniqueId("results_key_"+i,key);
            tag.setInteger("results_value_"+i,results.get(key).ordinal());
            i++;
        }
        return tag;
    }

    public static void readNBT(NBTTagCompound tag) {
        tokens.clear();
        results.clear();
        if (!tag.hasKey("token_count")) return;
        int tokenCount = tag.getInteger("token_count");
        for (int i = 0;i < tokenCount;i++) {
            UUID key = tag.getUniqueId("token_key_"+i);
            long v = tag.getLong("token_value_"+i);
            tokens.put(key,v);
        }
        int resultCount = tag.getInteger("results_count");
        for (int i = 0;i < resultCount;i++) {
            UUID key = tag.getUniqueId("results_key_"+i);
            ResearchTechnologies v = ResearchTechnologies.values()[tag.getInteger("results_value_"+i)];
            results.put(key,v);
        }
    }

}
