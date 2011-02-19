package com.houston.legacy.adapter;

import java.lang.reflect.Method;

import com.houston.legacy.adapter.source.parts.MethodPart;


class CallParameterPart implements MethodPart {

	@Override
	public String build(Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		String block = "(";
		for (int i = 0; i < parameterTypes.length; i++) {
			block = block + "param" + i;
			if (i < parameterTypes.length-1) block = block + ", ";
		}
		block = block + ");";
		return block;
	}
}