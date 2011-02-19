package com.houston.legacy.adapter.mapper;

import java.lang.reflect.Method;
import java.util.Map;

public class ManualMapper<T> implements MethodMapper {

	private final Map<String, String> manualMapping;

	public ManualMapper(Map<String, String> manualMapping) {
		this.manualMapping = manualMapping;
	}

	@Override
	public String map(Method method) {
		return manualMapping.get(method.getName());
	}

	@Override
	public boolean accept(Method method) {
		return manualMapping.containsKey(method.getName());
	}
}