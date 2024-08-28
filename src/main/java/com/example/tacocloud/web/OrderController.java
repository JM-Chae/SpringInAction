package com.example.tacocloud.web;

import com.example.tacocloud.Order;
import com.example.tacocloud.data.OrderRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController
  {
    private OrderRepository orderRepo;
    public OrderController(OrderRepository orderRepo)
      {
        this.orderRepo = orderRepo;
      }

    @GetMapping("/current")
    public String orderForm()
      {
//        model.addAttribute("order", new Order());
        return "orderForm";
      }

    @PostMapping
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus)
      {
        if (errors.hasErrors())
          {
            return "orderForm";
          }
//        log.info("Order submitted: " + order);
        orderRepo.save(order);
        sessionStatus.setComplete();
        return "redirect:/";
      }
  }
