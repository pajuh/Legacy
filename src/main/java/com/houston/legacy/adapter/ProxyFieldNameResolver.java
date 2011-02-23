package com.houston.legacy.adapter;

class ProxyFieldNameResolver {
	public String resolve(Class classToBeProxied) {
		return "proxy" + classToBeProxied.getSimpleName();
	}
}