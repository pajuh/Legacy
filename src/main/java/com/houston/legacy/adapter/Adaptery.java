/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.houston.legacy.adapter.internal.Interceptable;
import com.houston.legacy.adapter.internal.InterceptionPhase;
import com.houston.legacy.adapter.internal.Interceptions;
import com.houston.legacy.adapter.internal.ProxyFieldNameResolver;
import com.houston.legacy.adapter.mapper.ByNameAsDefaultCustomizableMapper;
import com.houston.legacy.adapter.mapper.CustomizableMethodMapper;
import com.houston.legacy.adapter.mapper.ManualMapper;
import com.houston.legacy.adapter.source.parts.AfterInterceptionPart;
import com.houston.legacy.adapter.source.parts.BeforeInterceptionPart;
import com.houston.legacy.adapter.source.parts.CallParameterPart;
import com.houston.legacy.adapter.source.parts.MethodNamePart;
import com.houston.legacy.adapter.source.parts.MethodPart;
import com.houston.legacy.adapter.source.parts.MethodSourceBlockPart;
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

	private final CustomizableMethodMapper customizableMethodMapper = new ByNameAsDefaultCustomizableMapper();
	private String adapterClassName;
	private BeforeInterception beforeInterception;
	private AfterInterception afterInterception;
	private final ClassPool classPool = ClassPool.getDefault();

	public <T> T createAdapter(Class<T> interfaceToImplement, final Class<?> classToBeAdapted) {
		return createAdapter(interfaceToImplement, new ArrayList<Class<?>>() {{
				add(classToBeAdapted);
		}});
	}

	public <T> T createAdapter(Class<T> interfaceToImplement, List<Class<?>> classesToBeAdapted) {
		try {
			CtClass adapter = classPool.makeClass(createClassName(interfaceToImplement.getSimpleName()));
			
			defineImplementedInterfaces(interfaceToImplement, adapter);
			buildInterceptionFieldAndMethods(adapter);
			buildMethodsAndFields(interfaceToImplement, classesToBeAdapted, adapter);
			
			T adapterInstance = (T) adapter.toClass().newInstance();
			insertInterceptions(adapterInstance);
			return adapterInstance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private <T> void defineImplementedInterfaces(Class<T> interfaceToImplement, CtClass adapter) {
		adapter.setInterfaces(new CtClass[] { classPool.makeInterface(Interceptable.class.getName()), classPool.makeInterface(interfaceToImplement.getName()) });
	}

	private <T> void insertInterceptions(T adapterInstance) {
		((Interceptable) adapterInstance).setInterceptions(new Interceptions(beforeInterception, afterInterception));
	}

	private <T> void buildMethodsAndProxyFields(Class<T> interfaceToImplement, final Class<?> classToBeAdapted, CtClass adapter) throws CannotCompileException {
		List<MethodPart> methodParts = new ArrayList<MethodPart>() {{
				add(new VisibilityValuePart());
				add(new ReturnValuePart());
				add(new MethodNamePart());
				add(new ParameterPart());
				add(new SourceBlockPart(new ArrayList<MethodPart>() {{
						add(new BeforeInterceptionPart(beforeInterception));
						add(new MethodSourceBlockPart(classToBeAdapted, customizableMethodMapper));
						add(new CallParameterPart());
						add(new AfterInterceptionPart(afterInterception));
					}
				}));
			}
		};
		adapter.addField(CtField.make(createProxyFieldFor(classToBeAdapted), adapter));
		buildMethodsForAdapter(interfaceToImplement, adapter, methodParts, classToBeAdapted);
	}

	private String createProxyFieldFor(Class<?> classToBeAdapted) {
		return "private " + classToBeAdapted.getName() + " " + new ProxyFieldNameResolver().resolve(classToBeAdapted) + " = new " + classToBeAdapted.getName() + "();";
	}

	private <T> void buildMethodsForAdapter(Class<T> interfaceToImplement, CtClass makeClass, List<MethodPart> methodParts, Class classToBeAdapted) throws CannotCompileException {
		List<Method> methods = Arrays.asList(interfaceToImplement.getMethods());
		for (Method method : methods) {
			if (customizableMethodMapper.accept(method, classToBeAdapted)) {
				StringBuilder stringBuilder = new StringBuilder();
				for (MethodPart methodPart : methodParts) {
					stringBuilder.append(methodPart.build(method));
					stringBuilder.append(" ");
				}
				makeClass.addMethod(CtMethod.make(stringBuilder.toString(), makeClass));
			}
		}
	}

	private String createClassName(String proxyName) {
		if (adapterClassName == null) {
			return "Adapter" + proxyName;
		} else
			return adapterClassName;
	}

	public Adaptery withManualMapping(ManualMapping manualMapping) {
		customizableMethodMapper.customizeWith(new ManualMapper(manualMapping));
		return this;
	}

	public Adaptery withAdapterName(String adapterClassName) {
		this.adapterClassName = adapterClassName;
		return this;
	}

	public InterceptionPhase withInterceptor() {
		return new InterceptionPhase(this);
	}

	public void beforeInterception(BeforeInterception beforeInterception) {
		this.beforeInterception = beforeInterception;
	}

	public Adaptery and() {
		return this;
	}

	public void afterInterception(AfterInterception afterInterception) {
		this.afterInterception = afterInterception;
	}

	private void buildInterceptionFieldAndMethods(CtClass adapter) throws CannotCompileException {
		adapter.addField(CtField.make("private com.houston.legacy.adapter.internal.Interceptions interceptions;", adapter));
		adapter.addMethod(CtMethod.make("public void setInterceptions(com.houston.legacy.adapter.internal.Interceptions interceptions) { this.interceptions = interceptions; }", adapter));
	}

	private <T> void buildMethodsAndFields(Class<T> interfaceToImplement, List<Class<?>> classesToBeAdapted, CtClass adapter) throws CannotCompileException {
		for (Class<?> classToBeAdapted : classesToBeAdapted) {
			buildMethodsAndProxyFields(interfaceToImplement, classToBeAdapted, adapter);
		}
	}
}
