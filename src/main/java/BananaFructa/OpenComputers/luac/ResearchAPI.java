package BananaFructa.OpenComputers.luac;

import BananaFructa.OpenComputers.ResearchManager;
import BananaFructa.OpenComputers.ResearchTechnologies;
import li.cil.oc.api.machine.Machine;
import li.cil.oc.api.machine.MachineHost;
import li.cil.oc.api.network.EnvironmentHost;
import li.cil.oc.server.machine.luac.NativeLuaAPI;
import li.cil.oc.server.machine.luac.NativeLuaArchitecture;
import li.cil.repack.com.naef.jnlua.LuaState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import scala.runtime.AbstractFunction1;

import javax.swing.text.html.parser.Entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResearchAPI extends NativeLuaAPI {
    public ResearchAPI(NativeLuaArchitecture owner) {
        super(owner);
    }

    final class Test extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            state.pushString(ResearchManager.getUUIDForResult(ResearchTechnologies.values()[(int)state.checkInteger(1)]));
            return 1;
        }
    }

    final class BeginResearch extends AbstractFunction1<LuaState, Object> implements Serializable {

        Machine parent;

        public BeginResearch(Machine machine) {
            this.parent = machine;
        }

        @Override
        public Integer apply(LuaState state) {
            ResearchManager.addResearchState(state,parent);
            /*if (waitingId == null) {
                throw new RuntimeException("Only one researching instance can exist per computer ! (Nice try)");
            }
            state.pushString(waitingId.toString());*/
            return 1;
        }
    }

    final class EndResearch extends AbstractFunction1<LuaState, Object> implements Serializable {

        Machine parent;

        public EndResearch(Machine machine) {
            this.parent = machine;
        }

        @Override
        public Integer apply(LuaState state) {
            int tier = ResearchManager.getTier(Thread.currentThread());
            int memory = ResearchManager.removeResearchState(state,parent);
            if (memory == -1) state.pushString("no_token");
            else if (memory == -2) state.pushString("not_registered");
            else state.pushString(ResearchManager.computeResearchCost(tier,memory));
            return 1;
        }
    }

    final class CheckoutToken extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            String token = state.checkString(1);
            UUID uuid = UUID.fromString(token);
            state.pushInteger(ResearchManager.getValue(uuid));
            return 1;
        }
    }

    final class MergeToken extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            String token1 = state.checkString(1);
            String token2 = state.checkString(2);
            UUID uuid1 = UUID.fromString(token1);
            UUID uuid2 = UUID.fromString(token2);
            ResearchManager.mergeTokens(uuid1,uuid2);
            return 1;
        }
    }

    final class GetCost extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            int r = (int)state.checkInteger(1);
            if (r < 0 || r >= ResearchTechnologies.values().length) state.pushInteger(-1);
            else state.pushInteger(ResearchManager.getCost(ResearchTechnologies.values()[r]));
            return 1;
        }
    }

    final class GetId extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            String id = state.checkString(1);
            state.pushInteger(ResearchManager.getResearchId(UUID.fromString(id)));
            return 1;
        }
    }

    final class AcquireResearch extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            int r = (int)state.checkInteger(1);
            if (r < 0 || r >= ResearchTechnologies.values().length) state.pushString("not_enough");
            else {
                ResearchTechnologies tech = ResearchTechnologies.values()[r];
                String token = state.checkString(2);
                long researchedCount = state.checkInteger(3);
                List<UUID> researched = new ArrayList<>();
                for (int i = 0; i < researchedCount; i++) {
                    researched.add(UUID.fromString(state.checkString(4 + i)));
                }
                UUID uuid = UUID.fromString(token);
                state.pushString(ResearchManager.purchaseResearch(tech, uuid, researched));
            }
            return 1;
        }
    }

    final class GetDescription extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            int r = (int)state.checkInteger(1);
            if (r < 0 || r >= ResearchTechnologies.values().length) state.pushInteger(-1);
            else state.pushString(ResearchManager.getDescription(ResearchTechnologies.values()[r]));
            return 1;
        }
    }

    final class GetName extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            int r = (int)state.checkInteger(1);
            if (r < 0 || r >= ResearchTechnologies.values().length) state.pushInteger(-1);
            else state.pushString(ResearchManager.getName(ResearchTechnologies.values()[r]));
            return 1;
        }
    }

    final class MeetsRequirements extends AbstractFunction1<LuaState, Object> implements Serializable {
        @Override
        public Integer apply(LuaState state) {
            int r = (int)state.checkInteger(1);
            if (r < 0 || r >= ResearchTechnologies.values().length) state.pushBoolean(false);
            else {
                ResearchTechnologies tech = ResearchTechnologies.values()[r];
                long researchedCount = state.checkInteger(2);
                List<UUID> researched = new ArrayList<>();
                for (int i = 0; i < researchedCount; i++) {
                    researched.add(UUID.fromString(state.checkString(3 + i)));
                }
                state.pushBoolean(ResearchManager.meetsRequirements(tech, researched));
            }
            return 1;
        }
    }

    final class GiveReport extends AbstractFunction1<LuaState, Object> implements Serializable {

        Machine parent;
        public int rangeCheck = 10;

        public GiveReport(Machine parent) {
            this.parent = parent;
        }

        @Override
        public Integer apply(LuaState state) {
            int r = (int)state.checkInteger(1);
            if (r < 0 || r >= ResearchTechnologies.values().length) state.pushBoolean(false);
            else {
                ResearchTechnologies tech = ResearchTechnologies.values()[r];
                long researchedCount = state.checkInteger(2);
                List<UUID> researched = new ArrayList<>();
                for (int i = 0; i < researchedCount; i++) {
                    researched.add(UUID.fromString(state.checkString(3 + i)));
                }
                if(ResearchManager.meetsRequirements(tech, researched)) {
                    MachineHost host = parent.host();
                    if (host instanceof EnvironmentHost) {
                        EnvironmentHost eHost = (EnvironmentHost) host;
                        World world = eHost.world();
                        double x = eHost.xPosition();
                        double y = eHost.yPosition();
                        double z = eHost.zPosition();
                        AxisAlignedBB box = new AxisAlignedBB(
                            x - rangeCheck, y - rangeCheck, z - rangeCheck,
                            x + rangeCheck, y + rangeCheck, z + rangeCheck
                        );
                        List<EntityPlayer> nearby = world.getEntitiesWithinAABB(EntityPlayer.class,box);
                        if (!nearby.isEmpty()) {
                            ItemStack stack = new ItemStack(tech.rpProvider.get());
                            NBTTagCompound tagCompound = new NBTTagCompound();
                            tagCompound.setInteger("stage", tech.stageCode);
                            stack.setTagCompound(tagCompound);
                            nearby.get(0).addItemStackToInventory(stack);
                        }
                    }
                }
            }
            return 1;
        }
    }

    @Override
    public void initialize() {
        this.lua().newTable();

        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new Test());
        this.lua().setField(-2, "test");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new BeginResearch(this.machine()));
        this.lua().setField(-2, "begin");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new EndResearch(this.machine()));
        this.lua().setField(-2, "finish");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new CheckoutToken());
        this.lua().setField(-2, "check");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new MergeToken());
        this.lua().setField(-2, "merge");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new GetCost());
        this.lua().setField(-2, "costOf");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new AcquireResearch());
        this.lua().setField(-2, "acquire");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new GetDescription());
        this.lua().setField(-2, "descriptionOf");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new GetName());
        this.lua().setField(-2, "nameOf");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new MeetsRequirements());
        this.lua().setField(-2, "meetsRequirements");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new GetId());
        this.lua().setField(-2, "idOf");
        li.cil.oc.util.ExtendedLuaState.extendLuaState(this.lua()).pushScalaFunction(new GiveReport(this.machine()));
        this.lua().setField(-2, "give");



        this.lua().setGlobal("research");
    }
}
