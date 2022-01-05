package com.sherwin.login.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import com.sherwin.login.domain.SWUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.sherwin.login.domain.SWUserComments;



@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class SWUserCommentServiceImplTest {

    //TODO need to add more tests!

	@Autowired
	SWUserCommentService target;

	@Test
	public void testCreate() {
		assertNotNull(target);
	}

	@Test
	public void testPositiveCreate() {
		SWUserComments thisComment = new SWUserComments();

		thisComment.setComments("test comment message");
		thisComment.setCreatedBy("system");
		thisComment.setLoginID("bxp874");
		thisComment.setCreatedDate(new Date());
		assertTrue(target.createOrUpdateEntry(thisComment));

	}
}
