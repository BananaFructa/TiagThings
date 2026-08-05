package BananaFructa.TiagThings.mixin;

import BananaFructa.StgDel.StgDel;
import BananaFructa.TTIEMultiblocks.Utils.IEUtils;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityAssembler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEntityAssembler.CrafterPatternInventory.class)
public class AssemblerTechCheck {

    @Shadow
    @Final
    private TileEntityAssembler tile;

    @Shadow
    public NonNullList<ItemStack> inv;

    @Inject(method = "func_70299_a",at = @At("TAIL"),remap = false,cancellable = true)
    public void onInsert(int slot, ItemStack stack, CallbackInfo ci) {

        if (tile.hasWorld() && !tile.getWorld().isRemote) {
            World world = tile.getWorld();
            BlockPos pos = tile.getPos();
            EntityPlayer player = world.getClosestPlayer(pos.getX(),pos.getY(),pos.getZ(),15,false);
            if (player != null && !StgDel.proxy.IsItemUseAllowedForPlayer(player,inv.get(9))) {
                for (int i = 0;i < 10;i++) {
                    this.inv.set(i, ItemStack.EMPTY);
                    tile.markDirty();
                    IEUtils.notifyClientUpdate(tile.getWorld(),tile.getPos());
                }
            }
        }
    }

}
