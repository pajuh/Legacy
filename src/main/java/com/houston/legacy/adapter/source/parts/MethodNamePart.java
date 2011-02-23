/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.source.parts;

import java.lang.reflect.Method;


public class MethodNamePart implements MethodPart {
	@Override
	public String build(Method method) {
		return method.getName();
	}
}