package com.ntndev.ecommercespringboot.controller;

import com.ntndev.ecommercespringboot.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {

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
            // Save order to database
            return ResponseEntity.ok("Order created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order");
        }
    }


    @GetMapping("/{user_id}")
    public ResponseEntity<?> getOrdersByUserId(@Valid @PathVariable("user_id") Long userId) {
        try {
            // Get orders from database
            return ResponseEntity.ok("Orders found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting orders");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@Valid @PathVariable("id") Long id,
                                         @Valid @RequestBody OrderDTO orderDTO,
                                         BindingResult result) {
        try {
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(e -> e.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            // Update order in database
            return ResponseEntity.ok("Order updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating order");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") Long id) {
        try {
            // Delete order from database
            return ResponseEntity.ok("Order deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting order");
        }
    }

}
