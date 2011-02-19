package com.houston.legacy.adapter.source.parts;

import java.lang.reflect.Method;


public class ParameterPart implements MethodPart {
	@Override
	public String build(Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		StringBuilder block = new StringBuilder();
		block.append("(");
		for (int i = 0; i < parameterTypes.length; i++) {
			block.append(parameterTypes[i].getName());
			block.append(" param");
			block.append(i);
			if (i < parameterTypes.length-1) block.append(", ");
		}
		block.append(")");
		return block.toString();
	}
}