package org.vbossica.springbox.jmx;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.*;

/**
 * @author vladimir
 */
public class PropertyCanonicalNameResolverTest {

	private PropertyCanonicalNameResolver resolver;

	@Before
	public void init() throws Exception {
		resolver = new PropertyCanonicalNameResolver();
		resolver.setLocation(new ClassPathResource("jmxnames.properties"));
		resolver.afterPropertiesSet();
	}

	@Test
	public void should_replace_defined_name() throws Exception {
		assertEquals("com.dummy.services:name=ExtraServiceMBean", resolver.resolve("org.example.services:name=SimpleServiceMBean"));
	}

	@Test
	public void should_return_null() throws Exception {
		assertNull(resolver.resolve("org.example.services:name=ComplexServiceMBean"));
	}

}
