/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.internal;

import com.houston.legacy.adapter.Adaptery;
import com.houston.legacy.adapter.AfterInterception;
import com.houston.legacy.adapter.BeforeInterception;

public class InterceptionPhase {

	private final Adaptery adaptery;

	public InterceptionPhase(Adaptery adaptery) {
		this.adaptery = adaptery;
	}

	public Adaptery before(BeforeInterception beforeInterception) {
		adaptery.beforeInterception(beforeInterception);
		return adaptery;
	}

	public Adaptery after(AfterInterception afterInterception) {
		adaptery.afterInterception(afterInterception);
		return adaptery;
	}

}
