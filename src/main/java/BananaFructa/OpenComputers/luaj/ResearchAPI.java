package BananaFructa.OpenComputers.luaj;

import BananaFructa.OpenComputers.ResearchManager;
import li.cil.oc.server.machine.luaj.LuaJAPI;
import li.cil.oc.server.machine.luaj.LuaJLuaArchitecture;
import li.cil.oc.util.ScalaClosure;
import li.cil.repack.org.luaj.vm2.*;
import scala.runtime.AbstractFunction1;

import java.io.Serializable;
import java.util.UUID;

public class ResearchAPI extends LuaJAPI {
    public ResearchAPI(LuaJLuaArchitecture owner) {
        super(owner);
    }

    /*final class Test extends AbstractFunction1<Varargs,Varargs> implements Serializable {
        @Override
        public Varargs apply(Varargs args) {
            String address = args.checkjstring(1);
            System.out.println("ADD: " + address);
            String method = args.checkjstring(2);
            return LuaValue.varargsOf(new LuaValue[]{LuaString.valueOf("WAZZUP")});
        }
    }

    final class RequestToken extends AbstractFunction1<Varargs,Varargs> implements Serializable {
        @Override
        public Varargs apply(Varargs args) {
            return LuaValue.varargsOf(new LuaValue[]{LuaString.valueOf(BananaFructa.TiagThings.Research.ResearchManager.allocateNewToken().toString())});
        }
    }

    final class ExpandToken extends AbstractFunction1<Varargs,Varargs> implements Serializable {
        @Override
        public Varargs apply(Varargs args) {
            String id = args.checkjstring(1);
            return LuaValue.varargsOf(new LuaValue[]{LuaNumber.valueOf(BananaFructa.TiagThings.Research.ResearchManager.expandSequence(UUID.fromString(id)))});
        }
    }

    final class ValidateToken extends AbstractFunction1<Varargs,Varargs> implements Serializable {
        @Override
        public Varargs apply(Varargs args) {
            String id = args.checkjstring(1);
            int val = args.checkint(2);
            return LuaValue.varargsOf(new LuaValue[]{LuaBoolean.valueOf(BananaFructa.TiagThings.Research.ResearchManager.validate(UUID.fromString(id),val))});
        }
    }

    final class CheckToken extends AbstractFunction1<Varargs,Varargs> implements Serializable {
        @Override
        public Varargs apply(Varargs args) {
            String id = args.checkjstring(1);
            return LuaValue.varargsOf(new LuaValue[]{LuaNumber.valueOf(BananaFructa.TiagThings.Research.ResearchManager.checkValue(UUID.fromString(id)))});
        }
    }

    final class Research extends AbstractFunction1<Varargs,Varargs> implements Serializable {
        private final int[] delays = {
               3000,
               2000,
               1000
        };
        @Override
        public Varargs apply(Varargs args) {
            int tier = ResearchManager.getTier(Thread.currentThread());
            tier = Math.min(2,Math.max(0,tier));
            System.out.println("RESEARCH WITH TIER " + tier);
            try {
                Thread.sleep(delays[tier]);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return LuaValue.varargsOf(new LuaValue[]{});
        }
    }*/


    @Override
    public void initialize() {
        LuaTable component = LuaValue.tableOf();
        /*component.set("test", ScalaClosure.wrapVarArgClosure(new Test()));
        component.set("requestToken", ScalaClosure.wrapVarArgClosure(new RequestToken()));
        component.set("expandToken", ScalaClosure.wrapVarArgClosure(new ExpandToken()));
        component.set("validateToken", ScalaClosure.wrapVarArgClosure(new ValidateToken()));
        component.set("checkToken", ScalaClosure.wrapVarArgClosure(new CheckToken()));
        component.set("research", ScalaClosure.wrapVarArgClosure(new Research()));*/

        this.lua().set("research",component);
    }
}
