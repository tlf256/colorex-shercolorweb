package com.sherwin.login.service;

import com.sherwin.login.domain.SWUserComments;

public interface SWUserCommentService {
	/***
	 * create a new comment or update an existing comment to the
	 * SW_USER_COMMENTS table. 
	 * @param thisComment - a populated SWUserComments record.
	 * @return boolean true if successfully created/updated, false if not.
	 */
	public boolean createOrUpdateEntry(SWUserComments thisComment);
}
