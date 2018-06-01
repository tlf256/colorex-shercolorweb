package com.sherwin.login.dao;

import com.sherwin.login.domain.SWUserComments;

public interface SWUserCommentDao {
	/***
	 * create a new comment or update an existing comment to the
	 * SW_USER_COMMENTS table. 
	 * @param thisComment - a populated SWUserComments record.
	 * @return boolean true if successfully created/updated, false if not.
	 * @throws Exception 
	 */
	public SWUserComments createOrUpdateEntry(SWUserComments thisComment) throws Exception;
}
