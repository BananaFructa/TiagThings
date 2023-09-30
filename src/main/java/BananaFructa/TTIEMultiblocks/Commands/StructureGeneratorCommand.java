package BananaFructa.TTIEMultiblocks.Commands;

import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import BananaFructa.TTIEMultiblocks.Utils.MultiblockDimension;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StructureGeneratorCommand extends CommandBase {

    @Override
    public String getName() {
        return "geniemultiblock";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        BlockPos pos1 = parseBlockPos(sender,args,0,false);
        BlockPos pos2 = parseBlockPos(sender,args,3,false);
        BlockPos origin = parseBlockPos(sender,args,6,false);

        String facen = args[9];
        EnumFacing face = faceFromName(facen);
        if (face == EnumFacing.UP || face == EnumFacing.DOWN) return;

        BlockPos unitH = IEUtils.getUnitVectorForSide(face, MultiblockDimension.HEIGHT);
        BlockPos unitL = IEUtils.getUnitVectorForSide(face, MultiblockDimension.LENGTH);
        BlockPos unitW = IEUtils.getUnitVectorForSide(face, MultiblockDimension.WIDTH);

        int sizeH = distance(hadamard(pos1,unitH),hadamard(pos2,unitH)) + 1;
        int sizeL = distance(hadamard(pos1,unitL),hadamard(pos2,unitL)) + 1;
        int sizeW = distance(hadamard(pos1,unitW),hadamard(pos2,unitW)) + 1;
        int originH = 0,originL = 0,originW = 0;

        System.out.println("SIZE H " + sizeH);
        System.out.println("SIZE L" + sizeL);
        System.out.println("SIZE W" + sizeW);

        ItemStack structure[][][] = new ItemStack[sizeH][sizeL][sizeW];

        for (int h = 0;h < sizeH;h++) {
            for (int l = 0;l < sizeL;l++) {
                for (int w = 0;w < sizeW;w++) {
                    BlockPos current = pos1.add(scalarMultiply(unitH,h)).add(scalarMultiply(unitL,l)).add(scalarMultiply(unitW,-w));
                    if (current.equals(origin)) {
                        originH = h;
                        originL = l;
                        originW = w;
                    }
                    IBlockState state = sender.getEntityWorld().getBlockState(current);
                    structure[h][l][w] = state.getBlock().getItem(sender.getEntityWorld(),current,state);
                    System.out.println(current.getX() + " " + current.getY() + " " + current.getZ() + " " + structure[h][l][w].getItem());
                }
            }
        }

        try {
            saveToFile("structure.tgz",structure,originH,originL,originW);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length > 0 && args.length <= 3) return getTabCompletionCoordinate(args,0,targetPos);
        else if (args.length > 3 && args.length <= 6) return getTabCompletionCoordinate(args,3,targetPos);
        else if (args.length > 6 && args.length <= 9) return getTabCompletionCoordinate(args,6,targetPos);
        else if (args.length == 10) {
            Vec3d lookVec = sender.getCommandSenderEntity().getLookVec();
            EnumFacing face = EnumFacing.getFacingFromVector((float)lookVec.x,(float)lookVec.y,(float)lookVec.z);
            return new ArrayList<String>() {{
                add(face.getOpposite().getName().toLowerCase());
            }};
        }
        else return Collections.<String>emptyList();
    }

    private BlockPos normalize1D(BlockPos pos) {
        return new BlockPos((pos.getX() > 0) ? 1 : -1, pos.getY() > 0 ? 1 : -1, pos.getZ() > 0 ? 1 : -1);
    }

    EnumFacing faceFromName(String s) {
        if (s.equals("east")) return EnumFacing.EAST;
        if (s.equals("west")) return EnumFacing.WEST;
        if (s.equals("north")) return EnumFacing.NORTH;
        if (s.equals("south")) return EnumFacing.SOUTH;
        if (s.equals("up")) return EnumFacing.UP;
        if (s.equals("down")) return EnumFacing.DOWN;
        return null;
    }

    BlockPos hadamard(BlockPos p1, BlockPos p2) {
        return new BlockPos(p1.getX() * p2.getX(),p1.getY() * p2.getY(),p1.getZ() * p2.getZ());
    }

    int distance(BlockPos p1, BlockPos p2) {
        return (int)p1.subtract(p2).getDistance(0,0,0);
    }

    BlockPos scalarMultiply(BlockPos v, int s) {
        return new BlockPos(v.getX() * s,v.getY() * s,v.getZ() * s);
    }

    void saveToFile(String fileName, ItemStack structure[][][],int originH, int originL, int originW) throws IOException {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("sizeH",structure.length);
        nbt.setInteger("sizeL",structure[0].length);
        nbt.setInteger("sizeW",structure[0][0].length);
        nbt.setInteger("originH",originH);
        nbt.setInteger("originL",originL);
        nbt.setInteger("originW",originW);
        for (int h = 0;h < structure.length;h++) {
            for (int l = 0;l < structure[0].length;l++) {
                for (int w = 0;w < structure[0][0].length;w++) {
                    if (structure[h][l][w] == null) continue;
                    nbt.setTag(h + "-" + l + "-" + w,structure[h][l][w].serializeNBT());
                }
            }
        }
        FileOutputStream outputStream = new FileOutputStream(new File(fileName));
        CompressedStreamTools.writeCompressed(nbt,outputStream);
        outputStream.close();
    }


}
