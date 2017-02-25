package wanion.exfusile.compat;

/*
 * Created by WanionCane(https://github.com/WanionCane).
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 1.1. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/1.1/.
 */

import net.minecraft.item.Item;

import javax.annotation.Nonnull;
import java.lang.reflect.ParameterizedType;

public abstract class Compat<I extends Item>
{
	private final Class<I> itemClass;

	@SuppressWarnings("unchecked")
	Compat()
	{
		this.itemClass = (Class<I>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	public final Class<I> getItemClass()
	{
		return itemClass;
	}

	public abstract String getNameFor(@Nonnull final I item);
}