package wanion.exfusile.compat;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

import ganymedes01.aobd.items.AOBDItem;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nonnull;

public class AOBDCompat extends Compat<AOBDItem>
{
	@Override
	public String getNameFor(@Nonnull AOBDItem item)
	{
		return WordUtils.uncapitalize(item.getOre().name());
	}
}