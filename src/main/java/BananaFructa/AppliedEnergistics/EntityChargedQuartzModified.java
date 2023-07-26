package BananaFructa.AppliedEnergistics;

import appeng.api.AEApi;
import appeng.api.definitions.IMaterials;
import appeng.client.EffectType;
import appeng.core.AEConfig;
import appeng.core.AppEng;
import appeng.core.features.AEFeature;
import appeng.entity.AEBaseEntityItem;
import appeng.helpers.Reflected;
import appeng.util.Platform;
import nc.init.NCItems;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityChargedQuartzModified extends AEBaseEntityItem {
    private int delay = 0;
    private int transformTime = 0;

    @Reflected
    public EntityChargedQuartzModified( final World w )
    {
        super( w );
    }

    public EntityChargedQuartzModified( final World w, final double x, final double y, final double z, final ItemStack is )
    {
        super( w, x, y, z, is );
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if( this.isDead || !AEConfig.instance().isFeatureEnabled( AEFeature.IN_WORLD_FLUIX ) )
        {
            return;
        }

        if( Platform.isClient() && this.delay > 30 && AEConfig.instance().isEnableEffects() )
        {
            AppEng.proxy.spawnEffect( EffectType.Lightning, this.world, this.posX, this.posY, this.posZ, null );
            this.delay = 0;
        }

        this.delay++;

        final int j = MathHelper.floor( this.posX );
        final int i = MathHelper.floor( ( this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY ) / 2.0D );
        final int k = MathHelper.floor( this.posZ );

        IBlockState state = this.world.getBlockState( new BlockPos( j, i, k ) );
        final Material mat = state.getMaterial();

        if( Platform.isServer() && mat.isLiquid() )
        {
            this.transformTime++;
            if( this.transformTime > 60 )
            {
                if( !this.transform() )
                {
                    this.transformTime = 0;
                }
            }
        }
        else
        {
            this.transformTime = 0;
        }
    }

    private boolean transform()
    {
        final ItemStack item = this.getItem();
        final IMaterials materials = AEApi.instance().definitions().materials();

        if( materials.certusQuartzCrystalCharged().isSameAs( item ) )
        {
            final AxisAlignedBB region = new AxisAlignedBB( this.posX - 1, this.posY - 1, this.posZ - 1, this.posX + 1, this.posY + 1, this.posZ + 1 );
            final List<Entity> l = this.getCheckedEntitiesWithinAABBExcludingEntity( region );

            EntityItem californium = null;
            EntityItem netherQuartz = null;

            for( final Entity e : l )
            {
                if( e instanceof EntityItem && !e.isDead )
                {
                    final ItemStack other = ( (EntityItem) e ).getItem();
                    if( !other.isEmpty() )
                    {
                        if( ItemStack.areItemsEqual( other, new ItemStack( NCItems.californium ) ) )
                        {
                            //if (other.getMetadata() == 15)
                            californium = (EntityItem) e;
                        }

                        if( ItemStack.areItemsEqual( other, new ItemStack( Items.QUARTZ ) ) )
                        {
                            netherQuartz = (EntityItem) e;
                        }
                    }
                }
            }

            if( californium != null && netherQuartz != null )
            {
                this.getItem().grow( -1 );
                californium.getItem().grow( -1 );
                netherQuartz.getItem().grow( -1 );

                if( this.getItem().getCount() <= 0 )
                {
                    this.setDead();
                }

                if( californium.getItem().getCount() <= 0 )
                {
                    californium.setDead();
                }

                if( netherQuartz.getItem().getCount() <= 0 )
                {
                    netherQuartz.setDead();
                }

                materials.fluixCrystal().maybeStack( 2 ).ifPresent( is ->
                {
                    final EntityItem entity = new EntityItem( this.world, this.posX, this.posY, this.posZ, is );

                    this.world.spawnEntity( entity );
                } );

                return true;
            }
        }

        return false;
    }
}
