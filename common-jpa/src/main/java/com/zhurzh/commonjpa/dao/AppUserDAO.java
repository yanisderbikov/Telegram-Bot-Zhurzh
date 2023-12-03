package com.zhurzh.commonjpa.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.zhurzh.commonjpa.entity.AppUser;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByTelegramUserId(Long id);
    Optional<AppUser> findById(Long id);
    Optional<AppUser> findByTelegramUserName(String tgUserName);
    List<AppUser> findAll();

}
