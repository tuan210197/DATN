package shupship.repo.impl;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import shupship.common.Const;
import shupship.domain.dto.UserInfoDTO;
import shupship.repo.BasicLoginRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Slf4j
public class BasicLoginRepoCustomImpl implements BasicLoginRepoCustom {

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserInfoDTO getActiveUserByEmail(String email) {
        StringBuilder strQuery = new StringBuilder();
        strQuery.append("select au.UID as uid, au.IS_ACTIVE as isActive, au.IS_DELETED as isDeleted, bl.EMAIL as email, ");
        strQuery.append("au.AVATAR as avatar, au.BIRTHDAY as birthday, au.FULL_NAME as fullName, au.GENDER as gender, ");
        strQuery.append("au.MOBILE as mobile, au.NAME as name ");
        strQuery.append("from APP_USER au join BASIC_LOGIN bl on au.UID = bl.USER_UID ");
        strQuery.append("where au.IS_ACTIVE = 1 and au.IS_DELETED = 0 and bl.IS_VERIFIED = 1 and bl.email = :email");
        Query query = em.createNativeQuery(strQuery.toString(), Const.ResultSetMapping.USER_INFO_DTO);
        query.setParameter("email", email);
        List<UserInfoDTO> userInfoDTOS = query.getResultList();
        return CollectionUtils.isEmpty(userInfoDTOS) ? null : userInfoDTOS.get(0);
    }



}
