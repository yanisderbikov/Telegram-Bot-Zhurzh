package com.zhurzh.app.commonjpa.dao;

import com.zhurzh.app.commonjpa.entity.AppUser;
import com.zhurzh.app.commonjpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDAO extends JpaRepository<Order, Long> {
    List<Order> findByOwner(AppUser appUser);
}
