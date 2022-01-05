package com.sherwin.login.dao;

import com.sherwin.login.domain.SWUser;
import com.sherwin.login.domain.SWUserPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SwUserRepository extends JpaRepository<SWUser, SWUserPK> {

    @Modifying
    @Query("update SWUser u set u.activeUser = 'N' where u.loginID = :loginID")
    int disableActiveUser(String loginID);

    SWUser findByLoginID(String loginID);

    @Query("select loginID from SWUser where lower(loginID) = lower(:loginID)")
    String getLoginID(String loginID);
}
