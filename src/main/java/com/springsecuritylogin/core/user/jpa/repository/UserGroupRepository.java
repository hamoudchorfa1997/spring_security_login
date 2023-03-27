package com.springsecuritylogin.core.user.jpa.repository;

import com.springsecuritylogin.core.user.jpa.data.Group;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends PagingAndSortingRepository<Group, Long> {
    Group findByCode(String code);
}

