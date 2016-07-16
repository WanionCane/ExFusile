package wanion.exfusile;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 1.1. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/1.1/.
 */

import exnihilo.items.ores.ItemOre;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;
import exnihilo.utils.ItemInfo;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.text.WordUtils;
import tconstruct.library.crafting.Smeltery;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

class ExNihiloCompat
{
    final Pattern pattern = Pattern.compile("(i?)(exnihilo|\\.|:|ore|_|nether|ender|broken|crushed|powdered)");
    final Map<ItemInfo, ArrayList<SiftingResult>> sieveRegistry = SieveRegistry.getSiftables();
    final Map<String, SmelteryRecipeWrapper> canMelt = new LinkedHashMap<>();

    ExNihiloCompat()
    {
        searchForThingsThatCanMelt();
        final String aluminium = "aluminium";
        final String aluminum = "aluminum";
        if (canMelt.containsKey(aluminium) && canMelt.containsKey(aluminum))
            canMelt.get(aluminium).setFluid(canMelt.get(aluminum).getFluid());
        final String yellorium = "yellorium";
        if (canMelt.containsKey(yellorium))
            canMelt.get(yellorium).setFluid("aobd" + yellorium);
        registerInSmeltery();
    }

    protected void searchForThingsThatCanMelt()
    {
        for (ItemInfo itemInfoKey : sieveRegistry.keySet()) {
            final List<SiftingResult> results = sieveRegistry.get(itemInfoKey);
            for (SiftingResult result : results) {
                if (!(result.item instanceof ItemOre))
                    continue;
                String materialName = WordUtils.uncapitalize(pattern.matcher(result.item.getUnlocalizedName()).replaceAll(""));
                if (!canMelt.containsKey(materialName))
                    canMelt.put(materialName, new SmelteryRecipeWrapper(materialName, 72));
                canMelt.get(materialName).meltingList.add(new ItemStack(result.item, 1, result.meta));
                if (!canMelt.containsKey(materialName))
                    canMelt.put(materialName, new SmelteryRecipeWrapper(materialName, 72));
                canMelt.get(materialName).meltingList.add(new ItemStack(result.item, 1, result.meta));
            }
        }
    }

    private void registerInSmeltery()
    {
        canMelt.values().stream().filter(SmelteryRecipeWrapper::valid).forEach(fluidWrapper -> {
            for (final ItemStack thisWillMelt : fluidWrapper.meltingList)
                Smeltery.addMelting(thisWillMelt, fluidWrapper.block, fluidWrapper.blockMeta, fluidWrapper.pointOfFusion, fluidWrapper.getFluidStack());
        });
    }
}