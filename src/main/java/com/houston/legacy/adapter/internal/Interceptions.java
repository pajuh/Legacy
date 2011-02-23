/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.internal;

import com.houston.legacy.adapter.AfterInterception;
import com.houston.legacy.adapter.BeforeInterception;

public class Interceptions {

	private final BeforeInterception beforeInterception;
	private final AfterInterception afterInterception;

	public Interceptions(BeforeInterception beforeInterception, AfterInterception afterInterception) {
		this.beforeInterception = beforeInterception;
		this.afterInterception = afterInterception;
	}
	
	public BeforeInterception beforeInterception() {
		return this.beforeInterception;
	}
	
	public AfterInterception afterInterception() {
		return this.afterInterception;
	}

}
