package com.houston.legacy.adapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.houston.legacy.adapter.mapper.CustomizableMapper;
import com.houston.legacy.adapter.mapper.ManualMapper;
import com.houston.legacy.adapter.source.parts.MethodNamePart;
import com.houston.legacy.adapter.source.parts.MethodPart;
import com.houston.legacy.adapter.source.parts.ParameterPart;
import com.houston.legacy.adapter.source.parts.ReturnValuePart;
import com.houston.legacy.adapter.source.parts.SourceBlockPart;
import com.houston.legacy.adapter.source.parts.VisibilityValuePart;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;

public class Adaptery {

	private CustomizableMapper customizedMapper = new CustomizableMapper();
	private String adapterClassName;
	private BeforeInterception beforeInterception;
	private AfterInterception afterInterception;

	public <T> T createAdapter(Class<T>  interfaceToImplement, Class<?> classToBeAdapted) {
		List<MethodPart> methodParts = new ArrayList<MethodPart>() {{
			add(new VisibilityValuePart());
			add(new ReturnValuePart());
			add(new MethodNamePart());
			add(new ParameterPart());
			add(new SourceBlockPart(new ArrayList<MethodPart>() {{
				add(new BeforeInterceptionPart(beforeInterception));
				add(new MethodSourceBlockPart());
				add(new CallParameterPart());
				add(new AfterInterceptionPart(afterInterception));
			}}));
			add(new MethodSourceBlockPart());
			add(new CallParameterPart());
		}};
		try {
			ClassPool classPool = ClassPool.getDefault();
			CtClass adapter = classPool.makeClass(createClassName(classToBeAdapted.getSimpleName()));
			adapter.setInterfaces(new CtClass[] {
						classPool.makeInterface(Interceptable.class.getName()),
						classPool.makeInterface(interfaceToImplement.getName())
					});
			adapter.addField(CtField.make(createProxyFieldFor(classToBeAdapted), adapter));
			adapter.addField(CtField.make("private com.houston.legacy.adapter.Interceptions interceptions;", adapter));
			adapter.addMethod(CtMethod.make("public void setInterceptions(com.houston.legacy.adapter.Interceptions interceptions) { this.interceptions = interceptions; }", adapter));
			buildMethodsForAdapter(interfaceToImplement, adapter, methodParts);
			T adapterInstance = (T) adapter.toClass().newInstance();
			((Interceptable) adapterInstance).setInterceptions(new Interceptions(beforeInterception, afterInterception));
			return adapterInstance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String createProxyFieldFor(Class<?> classToBeAdapted) {
		return "private " + classToBeAdapted.getName() + " proxy = new " + classToBeAdapted.getName() + "();";
	}

	private <T> void buildMethodsForAdapter(Class<T> interfaceToImplement, CtClass makeClass, List<MethodPart> methodParts) throws CannotCompileException {
		List<Method> methods = Arrays.asList(interfaceToImplement.getMethods());
		for (Method method : methods) {
			StringBuilder stringBuilder = new StringBuilder();
			for (MethodPart methodPart : methodParts) {
				stringBuilder.append(methodPart.build(method));
				stringBuilder.append(" ");
			}
			makeClass.addMethod(CtMethod.make(stringBuilder.toString(), makeClass));
		}
	}
	
	public class MethodSourceBlockPart implements MethodPart {
		@Override
		public String build(Method method) {
			return decideReturnType(method) + " proxy." + customizedMapper.map(method);
		}

		private String decideReturnType(Method method) {
			if  (method.getReturnType().getName() == "void") {
				return "";
			} else 
				return "return";
		}
	}
	
	private String createClassName(String proxyName) {
		if (adapterClassName == null) {
			return "Adapter" + proxyName;
		} else return adapterClassName;
	}
	
	public Adaptery withManualMapping(ManualMapping manualMapping) {
		customizedMapper.customizeWith(new ManualMapper(manualMapping));
		return this;
	}
	
	public Adaptery withAdpaterName(String adapterClassName) {
		this.adapterClassName = adapterClassName;
		return this;
	}

	public InterceptionPhase withInterceptor() {
		return new InterceptionPhase(this);
	}

	protected void beforeInterception(BeforeInterception beforeInterception) {
		this.beforeInterception = beforeInterception;
	}

	public Adaptery and() {
		return this;
	}

	public void afterInterception(AfterInterception afterInterception) {
		this.afterInterception = afterInterception;
	}
}