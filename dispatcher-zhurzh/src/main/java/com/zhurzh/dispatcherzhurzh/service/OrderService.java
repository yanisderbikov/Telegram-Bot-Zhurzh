package com.zhurzh.dispatcherzhurzh.service;

import com.zhurzh.commonjpa.dao.AppUserDAO;
import com.zhurzh.commonjpa.dao.OrderDAO;
import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.commonnodeservice.service.impl.CommandsManager;
import com.zhurzh.dispatcherzhurzh.model.ViewOrder;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
@Log4j
@AllArgsConstructor
public class OrderService {
    private AppUserDAO appUserDAO;
    private CommandsManager cm;
    private OrderDAO orderDAO;

    public List<ViewOrder> getAllOrders() {
        var res =  orderDAO.findAll()
                .stream()
                .peek(e -> e.getOwner().setTelegramUserName(e.getOwner() == null ? "-" : e.getOwner().getTelegramUserName()))
                .sorted(new Comparator<Order>() {
                    @Override
                    public int compare(Order o1, Order o2) {
                        if (o1.getDeadLine() == null && o2.getDeadLine() == null) return 0;
                        if (o1.getDeadLine() == null && o2.getDeadLine() != null) return 1;
                        if (o1.getDeadLine() != null && o2.getDeadLine() == null) return -1;
                        return o1.getDeadLine().compareTo(o2.getDeadLine());
                    }
                })
                .map(ViewOrder::new)
                .toList();
        return res;
    }

    public List<ViewOrder> getAllOrderFinished(){
        return getAllOrders().stream()
                .filter(Order::getIsFinished)
                .toList();
    }

    public String convertToString(List<ViewOrder> list){
        StringBuilder builder = new StringBuilder("ЗАКАЗЫ");
        for (var order : list){
            builder.append("\n\n").append("Dead Line : ").append(order.getDeadLine());
            builder.append("\n").append("Owner : ").append(order.getOwner().getTelegramUserName());
            builder.append(order);
            builder.append("\nFILES: ").append(order.getArtReference());
        }
        return builder.toString();
    }


    public void updateStatus(String orderId, String newStatus) {
        log.debug(String.format("SAVED : orderID : %s, newStatus : %s", orderId, newStatus));
    }
}
