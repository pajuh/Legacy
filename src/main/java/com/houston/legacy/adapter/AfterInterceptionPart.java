/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter;

import java.lang.reflect.Method;

import com.houston.legacy.adapter.source.parts.MethodPart;

public class AfterInterceptionPart implements MethodPart {

	private final AfterInterception afterInterception;

	public AfterInterceptionPart(AfterInterception afterInterception) {
		this.afterInterception = afterInterception;
	}

	@Override
	public String build(Method method) {
		if (afterInterception == null) return "";
		String string = "this.interceptions.afterInterception().after();";
		return string;
	}
}