package wanion.exfusile;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 1.1. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/1.1/.
 */

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;
import tconstruct.library.crafting.Smeltery;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SmelteryRecipeWrapper
{
    public final List<ItemStack> meltingList = new ArrayList<>();
    private final String name;
    private final int amount;
    private int pointOfFusion;
    private Block block;
    private int blockMeta;
    private Fluid fluid;

    SmelteryRecipeWrapper(@Nonnull String name, final int amount)
    {
        fluid = FluidRegistry.getFluid(name + ".molten");
        if (fluid == null)
            fluid = FluidRegistry.getFluid(name);
        this.amount = amount;
        final List<ItemStack> ores = OreDictionary.getOres("block" + (this.name = WordUtils.capitalize(name)), false);
        if (ores != null && !ores.isEmpty()) {
            final ItemStack blockStack = ores.get(0);
            this.block = blockStack.getItem() instanceof ItemBlock ? ((ItemBlock) blockStack.getItem()).field_150939_a : null;
            this.blockMeta = blockStack.getItemDamage();
            this.pointOfFusion = (int) Math.round(Smeltery.getLiquifyTemperature(this.block, this.blockMeta) * 0.60);
        } else {
            this.block = null;
            this.blockMeta = 0;
            this.pointOfFusion = 0;
        }
    }

    boolean valid()
    {
        return fluid != null && block != null && !meltingList.isEmpty();
    }

    FluidStack getFluidStack()
    {
        return new FluidStack(fluid, amount);
    }

    public int getPointOfFusion()
    {
        if (pointOfFusion == 0) {
            final Configuration configuration = new Configuration(new File("." + File.separatorChar + "config" + File.separatorChar + "ExFusile.cfg"));
            pointOfFusion = configuration.getInt(name + "MeltingPoint", Configuration.CATEGORY_GENERAL, 100, 100, Integer.MAX_VALUE, "Ex Fusile Couldn't found the melting point of " + name + ".\nyou must configure this by yourself.\nsorry for the inconvenient.");
            if (configuration.hasChanged())
                configuration.save();
            return pointOfFusion;
        } else return pointOfFusion;
    }

    public void setPointOfFusion(final int pointOfFusion)
    {
        this.pointOfFusion = pointOfFusion;
    }

    public Block getBlock()
    {
        return block;
    }

    public void setBlock(final Block block)
    {
        this.block = block;
    }

    public int getBlockMeta()
    {
        return blockMeta;
    }

    public void setBlockMeta(final int blockMeta)
    {
        this.blockMeta = blockMeta;
    }

    public Fluid getFluid()
    {
        return fluid;
    }

    void setFluid(@Nonnull final String fluidName)
    {
        final Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid != null)
            this.fluid = fluid;
    }

    void setFluid(Fluid fluid)
    {
        this.fluid = fluid;
    }
}