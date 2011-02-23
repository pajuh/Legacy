package com.houston.legacy.adapter;

import com.houston.legacy.adapter.AdapterySpec.CallInterface;

public class SecondFacadeClass {

	public static CallInterface callInterface = null;

	public void methodNumberTwo() {
		callInterface.callMe("methodTwo");
	}
}
