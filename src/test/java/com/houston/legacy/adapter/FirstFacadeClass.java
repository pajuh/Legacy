package com.houston.legacy.adapter;

import com.houston.legacy.adapter.AdapterySpec.CallInterface;

public class FirstFacadeClass {

	public static CallInterface callInterface = null;

	public void methodNumberOne() {
		callInterface.callMe("methodOne");
	}
	
	public void methodWithParameter(String parameter) {
		callInterface.callMe("methodWithParameter: " + parameter);
	}
	
	public void methodWithParameter(boolean b) {
		callInterface.callMe("methodWithParameter: " + b);
	}
	
	public void methodWithTwoParameters(String parameter1, String parameter2) {
		callInterface.callMe("methodWithTwoParameters: " + parameter1 + ", " + parameter2);
	}
	
	public String methodWithReturnValueAndParameter(String parameter1) {
		return callInterface.callMe("methodWithReturnValueAndParameter: " + parameter1);
	}

}
