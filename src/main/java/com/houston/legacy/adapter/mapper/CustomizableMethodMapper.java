package com.houston.legacy.adapter.mapper;

public interface CustomizableMethodMapper extends MethodMapper {

	public void customizeWith(MethodMapper customizedMapper);

}
