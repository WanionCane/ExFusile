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
        final String fzDarkIron = "fzDarkIron";
        if (canMelt.containsKey(fzDarkIron))
            canMelt.get(fzDarkIron).setFluid("fzdarkiron");
        final String deepIron = "deepIron";
        if (canMelt.containsKey(deepIron))
            canMelt.get(deepIron).setFluid("deep.iron.molten");
        final String astralSilver = "astralSilver";
        if (canMelt.containsKey(astralSilver))
            canMelt.get(astralSilver).setFluid("astral.silver.molten");
        final String shadowIron = "shadowIron";
        if (canMelt.containsKey(shadowIron))
            canMelt.get(shadowIron).setFluid("shadow.iron.molten");
        canMelt.values().stream().filter(SmelteryRecipeWrapper::valid).forEach(fluidWrapper -> fluidWrapper.meltingList.forEach(thisWillMelt -> Smeltery.addMelting(thisWillMelt, fluidWrapper.getBlock(), fluidWrapper.getBlockMeta(), fluidWrapper.getPointOfFusion(), fluidWrapper.getFluidStack())));
    }

    protected void searchForThingsThatCanMelt()
    {
        for (final ItemInfo itemInfoKey : sieveRegistry.keySet()) {
            final List<SiftingResult> results = sieveRegistry.get(itemInfoKey);
            for (final SiftingResult result : results) {
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
}