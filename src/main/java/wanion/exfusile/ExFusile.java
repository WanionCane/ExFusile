package wanion.exfusile;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 1.1. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/1.1/.
 */

import com.google.common.collect.Lists;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import exnihilo.registries.SieveRegistry;
import exnihilo.registries.helpers.SiftingResult;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tconstruct.library.crafting.Smeltery;
import wanion.exfusile.compat.AOBDCompat;
import wanion.exfusile.compat.Compat;
import wanion.exfusile.compat.ExAstrisCompat;
import wanion.exfusile.compat.ExNihiloCompat;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Mod(modid = "ExFusile", name = "Ex Fusile", version = "1.4", dependencies = "required-after:exnihilo;required-after:TConstruct;after:*")
public class ExFusile
{
    @Mod.EventHandler
    public void postInit(final FMLPostInitializationEvent event)
    {
        final Map<String, SmelteryRecipeWrapper> canMeltMap = new LinkedHashMap<>();
        searchForThingsThatCanMelt(canMeltMap);
        fixSomeThings(canMeltMap);
        canMeltMap.values().stream().filter(SmelteryRecipeWrapper::valid).forEach(fluidWrapper -> fluidWrapper.meltingList.forEach(thisWillMelt -> Smeltery.addMelting(thisWillMelt, fluidWrapper.getBlock(), fluidWrapper.getBlockMeta(), fluidWrapper.getPointOfFusion(), fluidWrapper.getFluidStack())));
    }

    private static void searchForThingsThatCanMelt(@Nonnull final Map<String, SmelteryRecipeWrapper> canMeltMap)
    {
        final List<Compat<? extends Item>> compatList = Lists.newArrayList(new ExNihiloCompat());
        if (Loader.isModLoaded("aobd"))
            compatList.add(new AOBDCompat());
        if (Loader.isModLoaded("exastris"))
            compatList.add(new ExAstrisCompat());
        final Map<Class<? extends Item>, Compat<? extends Item>> compatMap = getCompatMap(compatList);
        final Method getNameMethod;
        try {
            getNameMethod = Compat.class.getMethod("getNameFor", Item.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Couldn't find the Method!");
        }
        for (final List<SiftingResult> results : SieveRegistry.getSiftables().values()) {
            for (final SiftingResult result : results) {
                final Item item = result.item;
                final Compat<? extends Item> compat = item != null ? compatMap.get(item.getClass()) : null;
                if (item == null || compat == null)
                    continue;
                final String materialName;
                try {
                    materialName = (String) getNameMethod.invoke(compat, item);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    continue;
                }
                if (materialName == null)
                    continue;
                if (!canMeltMap.containsKey(materialName))
                    canMeltMap.put(materialName, new SmelteryRecipeWrapper(materialName, 72));
                canMeltMap.get(materialName).meltingList.add(new ItemStack(result.item, 1, result.meta));
            }
        }
    }

    private static void fixSomeThings(@Nonnull final Map<String, SmelteryRecipeWrapper> canMeltMap)
    {
        final String aluminium = "aluminium";
        final String aluminum = "aluminum";
        if (canMeltMap.containsKey(aluminium) && canMeltMap.containsKey(aluminum))
            canMeltMap.get(aluminium).setFluid(canMeltMap.get(aluminum).getFluid());
        final String yellorium = "yellorium";
        if (canMeltMap.containsKey(yellorium))
            canMeltMap.get(yellorium).setFluid("aobd" + yellorium);
        final String fzDarkIron = "fzDarkIron";
        if (canMeltMap.containsKey(fzDarkIron))
            canMeltMap.get(fzDarkIron).setFluid("fzdarkiron");
        final String deepIron = "deepIron";
        if (canMeltMap.containsKey(deepIron))
            canMeltMap.get(deepIron).setFluid("deep.iron.molten");
        final String astralSilver = "astralSilver";
        if (canMeltMap.containsKey(astralSilver))
            canMeltMap.get(astralSilver).setFluid("astral.silver.molten");
        final String shadowIron = "shadowIron";
        if (canMeltMap.containsKey(shadowIron))
            canMeltMap.get(shadowIron).setFluid("shadow.iron.molten");
        if (Loader.isModLoaded("exastris"))
            ExAstrisCompat.fixMeltingList(canMeltMap);
    }

    private static Map<Class<? extends Item>, Compat<? extends Item>> getCompatMap(@Nonnull final List<Compat<? extends Item>> compatList)
    {
        return compatList.stream().collect(Collectors.toMap(Compat::getItemClass, Function.identity()));
    }
}