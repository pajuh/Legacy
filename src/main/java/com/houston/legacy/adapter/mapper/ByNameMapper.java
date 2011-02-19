package com.houston.legacy.adapter.mapper;

import java.lang.reflect.Method;

class ByNameMapper implements MethodMapper {
	
	public String map(Method method) {
		return method.getName();
	}

	@Override
	public boolean accept(Method method) {
		return true;
	}
}