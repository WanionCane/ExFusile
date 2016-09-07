package wanion.exfusile;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 1.1. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/1.1/.
 */

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;

@Mod(modid = "ExFusile", name = "Ex Fusile", version = "1.1", dependencies = "required-after:exnihilo;required-after:TConstruct")
public class ExFusile
{
    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event)
    {
        if (!Loader.isModLoaded("aobd"))
            new ExNihiloCompat();
        else
            new AOBDCompat();
    }
}