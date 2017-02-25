package wanion.exfusile.compat;
/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import ExAstris.Item.ItemOre;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;
import wanion.exfusile.SmelteryRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExAstrisCompat extends Compat<ItemOre>
{
	@Override
	public String getNameFor(@Nonnull ItemOre item)
	{
		return WordUtils.uncapitalize(item.Name);
	}

	public static void fixMeltingList(@Nonnull final Map<String, SmelteryRecipeWrapper> canMeltMap)
	{
		canMeltMap.values().forEach(smelteryRecipeWrapper -> {
			final List<ItemStack> newStacksToMelt = new ArrayList<>();
			smelteryRecipeWrapper.meltingList.forEach(itemStack -> {
				if (itemStack != null && itemStack.getItem() instanceof ItemOre) {
					newStacksToMelt.add(new ItemStack(itemStack.getItem(), 1, 1));
					newStacksToMelt.add(new ItemStack(itemStack.getItem(), 1, 2));
				}
			});
			if (!newStacksToMelt.isEmpty())
				smelteryRecipeWrapper.meltingList.addAll(newStacksToMelt);
		});
	}
}