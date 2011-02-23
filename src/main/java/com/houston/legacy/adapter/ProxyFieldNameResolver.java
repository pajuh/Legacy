package com.houston.legacy.adapter;

public class ProxyFieldNameResolver {
	public String resolve(Class classToBeProxied) {
		return "proxy" + classToBeProxied.getSimpleName();
	}
}