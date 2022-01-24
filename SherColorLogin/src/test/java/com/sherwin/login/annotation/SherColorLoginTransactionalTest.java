package com.sherwin.login.annotation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

//TODO edit/update

/**
 * The primary purpose of this annotation is to provide a meta-annotation encompassing the various Spring annotations
 * which are necessary for running <b><u>transactional</u></b> integration tests in SherColorLogin.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@SpringBootTest
@Transactional
@ActiveProfiles("login-test")
public @interface SherColorLoginTransactionalTest {
}
