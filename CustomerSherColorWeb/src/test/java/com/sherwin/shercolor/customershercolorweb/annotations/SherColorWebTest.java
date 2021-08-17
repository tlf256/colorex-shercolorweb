package com.sherwin.shercolor.customershercolorweb.annotations;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * The primary purpose of this annotation is two-fold:
 *
 * <ol>
 *     <li>Provide a single location for specifying the database-specific profile for which to run tests against.</li>
 *     <li>Provide a meta-annotation encompassing the various Spring annotations
 *       which are necessary for running <b><u>transactional</u></b> integration tests in SherColorWeb.</li>
 * </ol>
 *
 * Use this interface at the class level to annotate any <b><u>transactional</u></b> integration tests to automatically pull in configuration,
 * make the test transactional and enable the profile corresponding to the database type under test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@ContextConfiguration(locations = {"classpath:config/spring/shercolorcommon-test.xml"})
@Transactional
@ActiveProfiles("oracle")
public @interface SherColorWebTest {
}
