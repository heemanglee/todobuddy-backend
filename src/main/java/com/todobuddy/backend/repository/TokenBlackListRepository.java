package com.todobuddy.backend.repository;

import com.todobuddy.backend.entity.TokenBlackList;
import org.springframework.data.repository.CrudRepository;

public interface TokenBlackListRepository extends CrudRepository<TokenBlackList, String> {

}
