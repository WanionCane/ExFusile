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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.text.WordUtils;
import tconstruct.library.crafting.Smeltery;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

class SmelteryRecipeWrapper
{
    final int pointOfFusion;
    final Block block;
    final int blockMeta;
    final List<ItemStack> meltingList = new ArrayList<>();
    private final int amount;
    private Fluid fluid;

    SmelteryRecipeWrapper(@Nonnull final String name, final int amount)
    {
        Fluid fluid = FluidRegistry.getFluid(name + ".molten");
        if (fluid == null)
            fluid = FluidRegistry.getFluid(name);
        this.fluid = fluid;
        this.amount = amount;
        List<ItemStack> ores = OreDictionary.getOres("block" + WordUtils.capitalize(name), false);
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

    void setFluid(@Nonnull final String fluidName)
    {
        final Fluid fluid = FluidRegistry.getFluid(fluidName);
        if (fluid != null)
            this.fluid = fluid;
    }

    Fluid getFluid()
    {
        return fluid;
    }

    void setFluid(Fluid fluid)
    {
        this.fluid = fluid;
    }

    boolean valid()
    {
        return fluid != null && block != null && !meltingList.isEmpty();
    }

    FluidStack getFluidStack()
    {
        return new FluidStack(fluid, amount);
    }
}