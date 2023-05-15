package com.sherwin.shercolor.customershercolorweb.annotation;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * The primary purpose of this annotation is to provide a meta-annotation encompassing the various Spring annotations
 * which are necessary for running <b><u>transactional</u></b> integration tests in SherColorWeb.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Transactional
@SpringBootTest
public @interface SherColorWebTransactionalTest {
}
