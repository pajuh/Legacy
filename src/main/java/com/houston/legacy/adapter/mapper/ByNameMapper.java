/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.mapper;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class ByNameMapper implements MethodMapper {
	
	@Override
	public boolean accept(Method method, Class<?> classToBeAdapted) {
		List<Method> asList = Arrays.asList(classToBeAdapted.getMethods());
		for (Method proxyClassMethod : asList) {
			if (proxyClassMethod.getName().equals(method.getName())) return true;
		}
		return false;
	}

	@Override
	public String map(Method method, Class<?> classToBeMappedFor) {
		return method.getName();
	}
}