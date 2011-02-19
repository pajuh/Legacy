package com.houston.legacy.adapter;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import jdave.Specification;
import jdave.junit4.JDaveRunner;

import org.jmock.Expectations;
import org.junit.runner.RunWith;

import com.houston.legacy.adapter.Adaptery;
import com.houston.legacy.adapter.AfterInterception;
import com.houston.legacy.adapter.BeforeInterception;
import com.houston.legacy.adapter.ManualMapping;

@RunWith(JDaveRunner.class)
public class AdapterySpec extends Specification<Adaptery>{

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
			
			assertEquals("returnValue", adapter.methodWithReturnValueAndParameter("foobar"));
			assertEquals("AdapterClassToBeAdapted", adapter.getClass().getName());
		}
		
		public void shouldCreateAdapterByGivenName() {
			AdaptedInterface adapter = adaptery.withAdpaterName("AnotherAdapterName").createAdapter(AdaptedInterface.class, ClassToBeAdapted.class);
			assertEquals("AnotherAdapterName", adapter.getClass().getName());
		}
	}
	
	public class GivenOneDifferentiatingMapping {
		
		public void shouldMapAsManuallyAskedTo() {
			AdaptedInterface adapter = adaptery.withAdpaterName("ManualMappingAdapter").withManualMapping(new ManualMapping() {{
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

			AdaptedInterface adapter = adaptery.withAdpaterName("BeforeInterceptorAdapter").
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
}
