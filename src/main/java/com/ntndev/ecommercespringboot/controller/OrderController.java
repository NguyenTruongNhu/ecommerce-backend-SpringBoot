package com.ntndev.ecommercespringboot.controller;

import com.ntndev.ecommercespringboot.components.LocalizationUtils;
import com.ntndev.ecommercespringboot.dtos.OrderDTO;
import com.ntndev.ecommercespringboot.models.Order;
import com.ntndev.ecommercespringboot.responses.OrderResponse;
import com.ntndev.ecommercespringboot.services.OrderService;
import com.ntndev.ecommercespringboot.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final LocalizationUtils localizationUtils;

    @PostMapping("")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderDTO orderDTO,
                                         BindingResult result) {

        try {
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(e -> e.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Order order = orderService.createOrder(orderDTO);

            // Save order to database
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {

            List<Order> orders = orderService.findByUserId(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long id) {
        try {
            // Get orders from database
           Order existingOrder =  orderService.getOrder(id);
            return ResponseEntity.ok(OrderResponse.fromOrder(existingOrder));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") Long id,
                                         @Valid @RequestBody OrderDTO orderDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }

            Order order = orderService.updateOrder(id, orderDTO);
            // Update order in database
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") Long id) {
        try {
            // Delete order from database
            orderService.deleteOrder(id);
            return ResponseEntity.ok(localizationUtils.getLocalizedMessage(MessageKeys.DELETE_ORDER_SUCCESSFULLY));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
