package com.junshock.jpatest.controller;

import com.junshock.jpatest.domain.Member;
import com.junshock.jpatest.domain.item.Item;
import com.junshock.jpatest.domain.order.Order;
import com.junshock.jpatest.repository.OrderSearch;
import com.junshock.jpatest.service.ItemService;
import com.junshock.jpatest.service.MemberService;
import com.junshock.jpatest.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);

        return "order/orderList";
    }

    @PostMapping("/orders/{oderId}/cancle")
    public String cancelOrder(@PathVariable("oderId") Long orderId){
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
