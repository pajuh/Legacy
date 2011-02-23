/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.source.parts;

import java.lang.reflect.Method;

public class CallParameterPart implements MethodPart {

	@Override
	public String build(Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		String block = "(";
		for (int i = 0; i < parameterTypes.length; i++) {
			block = block + "param" + i;
			if (i < parameterTypes.length - 1)
				block = block + ", ";
		}
		block = block + ");";
		return block;
	}
}