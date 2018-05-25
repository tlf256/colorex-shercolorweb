package com.sherwin.shercolor.common.domain;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.beans.IntrospectionException;
import org.junit.Test;
import com.sherwin.shercolor.common.util.JavaBeanTester;

public class CustWebLoginTransformTest {

	private CustWebLoginTransform target;

	@Test
	public void testCreate() {
		target = new CustWebLoginTransform();
		assertNotNull(target);
	}

	@Test
	public void testBeanProperties() {
		try {
			JavaBeanTester.test(CustWebLoginTransform.class);
		} catch (IntrospectionException e) {
			fail();
		}
	}

}
