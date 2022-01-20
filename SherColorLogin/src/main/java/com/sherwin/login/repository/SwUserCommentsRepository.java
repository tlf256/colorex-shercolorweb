package com.sherwin.login.repository;

import com.sherwin.login.domain.SWUserComments;
import com.sherwin.login.domain.SWUserCommentsPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SwUserCommentsRepository extends JpaRepository<SWUserComments, SWUserCommentsPK> {
}
