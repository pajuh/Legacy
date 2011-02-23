/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.mapper;

import java.lang.reflect.Method;

public interface MethodMapper {
	public boolean accept(Method method, Class<?> classToBeAdapted);
	public String map(Method method, Class<?> classToBeMappedFor);
}