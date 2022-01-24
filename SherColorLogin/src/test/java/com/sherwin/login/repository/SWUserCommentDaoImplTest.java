package com.sherwin.login.repository;

import com.sherwin.login.annotation.SherColorLoginTransactionalTest;
import com.sherwin.login.domain.SWUserComments;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;



@RunWith(SpringJUnit4ClassRunner.class)
@SherColorLoginTransactionalTest
public class SWUserCommentDaoImplTest {

    //TODO need to add more tests!

	@Autowired
	private SwUserCommentsRepository swUserCommentsRepository;

	@Test
	public void testCreate() {
		assertNotNull(swUserCommentsRepository);
	}

	@Test
	public void testPositiveCreate() {
		SWUserComments thisComment = new SWUserComments();

		try {
			thisComment.setComments("test comment message");
			thisComment.setCreatedBy("system");
			thisComment.setLoginID("bxp874");
			thisComment.setCreatedDate(new Date());
		SWUserComments thatComment = swUserCommentsRepository.save(thisComment);
		System.out.println("Update successful.  ID is " + thatComment.getId());
		assertTrue(true);
		} catch (Exception ex) {
			//fail the test.
			assertTrue(false);
			System.out.println("Update failed. " + ex.getMessage());
		}


	}

	@Test
	public void testPositiveUpdate() {
		SWUserComments thisComment = new SWUserComments();

		try {
			//set the ID - should update?
			thisComment.setId(840);
			thisComment.setComments("test comment message");
			thisComment.setCreatedBy("system");
			thisComment.setLoginID("bxp874");
			thisComment.setCreatedDate(new Date());
		SWUserComments thatComment = swUserCommentsRepository.save(thisComment);
		System.out.println("Update successful.  ID is " + thatComment.getId());
		assertTrue(true);
		} catch (Exception ex) {
			//fail the test.
			assertTrue(false);
			System.out.println("Update failed. " + ex.getMessage());
		}


	}
}
