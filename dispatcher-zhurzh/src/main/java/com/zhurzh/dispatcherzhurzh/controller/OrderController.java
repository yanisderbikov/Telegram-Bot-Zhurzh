package com.zhurzh.dispatcherzhurzh.controller;

import com.zhurzh.commonjpa.entity.Order;
import com.zhurzh.dispatcherzhurzh.model.ViewOrder;
import com.zhurzh.dispatcherzhurzh.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/orders")
    public String showOrderList(Model model) {
        List<ViewOrder> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "orderList";
    }

    @GetMapping("/orders/finished")
    public String showOrderFinished(Model model) {
        List<ViewOrder> orders = orderService.getAllOrderFinished();
        model.addAttribute("orders", orders);
        return "orderList";
    }
    @PostMapping("/updateStatus")
    public ResponseEntity<String> updateStatus(@RequestBody Map<String, String> data) {
        String orderId = data.get("orderId");
        String newStatus = data.get("status");

        // Обновление статуса в вашем сервисе
        orderService.updateStatus(orderId, newStatus);

        return ResponseEntity.ok("Status updated successfully");
    }
}
