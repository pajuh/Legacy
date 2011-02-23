/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter.mapper;

import java.lang.reflect.Method;

public class ByNameAsDefaultCustomizableMapper implements CustomizableMethodMapper {

	MethodMapper customizedMapper = new MethodMapper() {
		
		@Override
		public String map(Method method, Class<?> classToBeMappedFor) {
			throw new IllegalStateException("This should not be called");
		}
		
		@Override
		public boolean accept(Method method, Class<?> classToBeAdapted) {
			return false;
		}
	};
	
	MethodMapper defaultMapper = new ByNameMapper();
	
	@Override
	public String map(Method method, Class<?> classToBeMappedFor) {
		if (customizedMapper.accept(method, classToBeMappedFor)) {
			return customizedMapper.map(method, classToBeMappedFor);
		}
		return defaultMapper.map(method, classToBeMappedFor);
	}

	@Override
	public boolean accept(Method method, Class<?> classToBeAdapted) {
		return (defaultMapper.accept(method, classToBeAdapted) || customizedMapper.accept(method, classToBeAdapted));
	}

	public void customizeWith(MethodMapper customizedMapper) {
		this.customizedMapper = customizedMapper;
	}
}