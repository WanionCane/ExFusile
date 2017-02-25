package wanion.exfusile.compat;
/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import exnihilo.items.ores.ItemOre;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class ExNihiloCompat extends Compat<ItemOre>
{
	private static final Pattern pattern = Pattern.compile("(i?)(exnihilo|\\.|:|ore|_|nether|ender|broken|crushed|powdered)");

	@Override
	public String getNameFor(@Nonnull ItemOre item)
	{
		return WordUtils.uncapitalize(pattern.matcher(item.getUnlocalizedName()).replaceAll(""));
	}
}