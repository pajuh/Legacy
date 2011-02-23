/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.source.parts;

import java.lang.reflect.Method;

import com.houston.legacy.adapter.BeforeInterception;

public class BeforeInterceptionPart implements MethodPart {

	private final BeforeInterception beforeInterception;

	public BeforeInterceptionPart(BeforeInterception beforeInterception) {
		this.beforeInterception = beforeInterception;
	}

	@Override
	public String build(Method method) {
		if (beforeInterception == null) return "";
		return "this.interceptions.beforeInterception().before();";
	}
	
}