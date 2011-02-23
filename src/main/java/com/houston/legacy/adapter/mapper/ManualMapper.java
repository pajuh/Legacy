/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.mapper;

import java.lang.reflect.Method;
import java.util.Map;

public class ManualMapper implements MethodMapper {

	private final Map<String, String> manualMapping;

	public ManualMapper(Map<String, String> manualMapping) {
		this.manualMapping = manualMapping;
	}

	@Override
	public boolean accept(Method method, Class<?> classToBeAdapted) {
		return manualMapping.containsKey(method.getName());
	}

	@Override
	public String map(Method method, Class<?> classToBeMappedFor) {
		return manualMapping.get(method.getName());
	}
}