package com.houston.legacy.adapter;

public class Interceptions {

	private final BeforeInterception beforeInterception;
	private final AfterInterception afterInterception;

	public Interceptions(BeforeInterception beforeInterception, AfterInterception afterInterception) {
		this.beforeInterception = beforeInterception;
		this.afterInterception = afterInterception;
	}
	
	public BeforeInterception beforeInterception() {
		return this.beforeInterception;
	}
	
	public AfterInterception afterInterception() {
		return this.afterInterception;
	}

}
