/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.source.parts;

import java.lang.reflect.Method;

import com.houston.legacy.adapter.AfterInterception;

public class AfterInterceptionPart implements MethodPart {

	private final AfterInterception afterInterception;

	public AfterInterceptionPart(AfterInterception afterInterception) {
		this.afterInterception = afterInterception;
	}

	@Override
	public String build(Method method) {
		if (afterInterception == null) return "";
		return "this.interceptions.afterInterception().after();";
	}
}