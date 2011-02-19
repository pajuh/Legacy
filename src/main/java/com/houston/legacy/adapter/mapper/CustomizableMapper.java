package com.houston.legacy.adapter.mapper;

import java.lang.reflect.Method;

public class CustomizableMapper implements MethodMapper {

	MethodMapper customizedMapper = new MethodMapper() {
		
		@Override
		public String map(Method method) {
			throw new IllegalStateException("This should not be called");
		}
		
		@Override
		public boolean accept(Method method) {
			return false;
		}
	};
	
	MethodMapper defaultMapper = new ByNameMapper();
	
	@Override
	public String map(Method method) {
		if (customizedMapper.accept(method)) {
			return customizedMapper.map(method);
		}
		return defaultMapper.map(method);
	}

	@Override
	public boolean accept(Method method) {
		return true;
	}

	public void customizeWith(MethodMapper customizedMapper) {
		this.customizedMapper = customizedMapper;
	}
}