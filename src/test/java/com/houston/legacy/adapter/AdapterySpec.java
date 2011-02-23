/**
 * @author pasi.honkanen@houston-inc.com
 */
package com.houston.legacy.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

@RunWith(JDaveRunner.class)
public class AdapterySpec extends Specification<Adaptery>{

	private Adaptery adaptery;
	private CallInterface callInterface;
	
	public void create() {
		adaptery = new Adaptery();
		callInterface = mock(CallInterface.class);
		ClassToBeAdapted.callInterface = callInterface;
	}
	
	public class GivenDefaults {		
		public void shouldCreateAdapterWithSignatureMatchMapping() {
			AdaptedInterface adapter = adaptery.createAdapter(AdaptedInterface.class, ClassToBeAdapted.class);
			checking(new Expectations() {{
				oneOf(callInterface).callMe("methodOne");
				oneOf(callInterface).callMe("methodTwo");
				oneOf(callInterface).callMe("methodWithParameter: givenParameter");
				oneOf(callInterface).callMe("methodWithParameter: true");
				oneOf(callInterface).callMe("methodWithTwoParameters: first, second");
				oneOf(callInterface).callMe("methodWithReturnValueAndParameter: foobar"); will(returnValue("returnValue"));
			}});
			
			adapter.methodNumberOne();
			adapter.methodNumberTwo();
			adapter.methodWithParameter("givenParameter");
			adapter.methodWithTwoParameters("first", "second");
			adapter.methodWithParameter(true);
			
			specify(adapter.methodWithReturnValueAndParameter("foobar"), should.equal("returnValue"));
			specify(adapter.getClass().getName(), should.equal("AdapterAdaptedInterface"));
		}
		
		public void shouldCreateAdapterForTwoClasses() {
			List<Class<?>> classesToBeAdapted = new ArrayList<Class<?>>() {{
				add(FirstFacadeClass.class);
				add(SecondFacadeClass.class);
			}};
			
			FirstFacadeClass.callInterface = callInterface;
			SecondFacadeClass.callInterface = callInterface;
			
			AdaptedInterface adapter = adaptery.withAdapterName("TwoClassAdapter").createAdapter(AdaptedInterface.class, classesToBeAdapted);
			
			checking(new Expectations() {{
				oneOf(callInterface).callMe("methodOne");
				oneOf(callInterface).callMe("methodTwo");
			}});
			
			adapter.methodNumberOne();
			adapter.methodNumberTwo();
		}
		
		public void shouldCreateAdapterByGivenName() {
			AdaptedInterface adapter = adaptery.withAdapterName("AnotherAdapterName").createAdapter(AdaptedInterface.class, ClassToBeAdapted.class);
			specify(adapter.getClass().getName(), should.equal("AnotherAdapterName"));
		}
	}
	
	public class GivenOneDifferentiatingMapping {		
		public void shouldMapAsManuallyAskedTo() {
			AdaptedInterface adapter = adaptery.withAdapterName("ManualMappingAdapter").withManualMapping(new ManualMapping() {{
				put("methodNumberOne", "methodNumberTwo");
			}}).createAdapter(AdaptedInterface.class, ClassToBeAdapted.class);
			
			checking(new Expectations() {{
				exactly(2).of(callInterface).callMe("methodTwo");
			}});
			
			adapter.methodNumberOne();
			adapter.methodNumberTwo();
		}
	}
	
	public class GivenInterceptors {
		public void shouldCallInterceptorsBeforeAndAfterMappedMethods() {
			final Logger logger = mock(Logger.class);

			AdaptedInterface adapter = adaptery.withAdapterName("BeforeInterceptorAdapter").
				withInterceptor().before(new BeforeInterception() {
					public void before() {
						logger.log(Level.INFO, "I was called before proxy");
					}
				}).and().
				withInterceptor().after(new AfterInterception() {
					public void after() {
						logger.log(Level.INFO, "I was called after proxy");
					}
				}).createAdapter(AdaptedInterface.class, ClassToBeAdapted.class);
			
			checking(new Expectations() {{
				oneOf(callInterface).callMe("methodOne");
				oneOf(logger).log(Level.INFO, "I was called before proxy");
				oneOf(logger).log(Level.INFO, "I was called after proxy");
			}});
			
			adapter.methodNumberOne();
		}
	}
	
	public interface AdaptedInterface {
		public void methodNumberOne();
		public String methodWithReturnValueAndParameter(String string);
		public void methodWithTwoParameters(String string, String string2);
		public void methodNumberTwo();
		public void methodWithParameter(String message);
		public void methodWithParameter(boolean b);
	}
	
	public interface CallInterface {
		public String callMe(String caller); 
	}
}
