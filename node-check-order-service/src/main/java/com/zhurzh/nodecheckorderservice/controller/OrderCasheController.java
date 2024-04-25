package com.zhurzh.nodecheckorderservice.controller;

import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.AppUser;
import com.zhurzh.commonjpa.entity.Order;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class OrderCasheController {
    private OrderDAO orderDAO;
    @NonNull
    private Map<AppUser, Long> orderMap;


    public Order getCurrentOrder(AppUser appUser){
        var id = orderMap.get(appUser);
        return orderDAO.findById(id).orElseThrow();
    }

    public void setOrder(AppUser appUser, Order order){
        orderMap.put(appUser, order.getId());
    }
}
