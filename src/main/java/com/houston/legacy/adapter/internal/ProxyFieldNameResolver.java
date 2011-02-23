package com.houston.legacy.adapter.internal;

public class ProxyFieldNameResolver {
	public String resolve(Class<?> classToBeProxied) {
		return "proxy" + classToBeProxied.getSimpleName();
	}
}