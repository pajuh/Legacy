package com.houston.legacy.adapter.source.parts;

import java.lang.reflect.Method;


public class ReturnValuePart implements MethodPart {
	@Override
	public String build(Method method) {
		return method.getReturnType().getName();
	}
}