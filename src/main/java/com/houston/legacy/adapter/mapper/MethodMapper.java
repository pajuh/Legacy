package com.houston.legacy.adapter.mapper;

import java.lang.reflect.Method;

interface MethodMapper {
	public String map(Method method);
	public boolean accept(Method method);
}