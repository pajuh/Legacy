package com.houston.legacy.adapter.source.parts;

import java.lang.reflect.Method;

import com.houston.legacy.adapter.ProxyFieldNameResolver;
import com.houston.legacy.adapter.mapper.MethodMapper;

public class MethodSourceBlockPart implements MethodPart {

	private final Class<?> classToBeMappedFor;
	private String proxyFieldName;
	private final MethodMapper methodMapper;

	public MethodSourceBlockPart(Class<?> classToBeMappedFor, MethodMapper methodMapper) {
		this.classToBeMappedFor = classToBeMappedFor;
		this.methodMapper = methodMapper;
		this.proxyFieldName = new ProxyFieldNameResolver().resolve(classToBeMappedFor);
	}

	@Override
	public String build(Method method) {
		return decideReturnType(method) + " " + proxyFieldName + "." + methodMapper.map(method, classToBeMappedFor);
	}

	private String decideReturnType(Method method) {
		if (method.getReturnType().getName() == "void") {
			return "";
		} else
			return "return";
	}
}