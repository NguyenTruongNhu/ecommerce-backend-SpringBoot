package com.ntndev.ecommercespringboot.controller;

import com.ntndev.ecommercespringboot.dtos.OrderDTO;
import com.ntndev.ecommercespringboot.dtos.OrderDetailDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailControler {
    @PostMapping("")
    public ResponseEntity<?> createOrderDetail(@Valid @RequestBody OrderDetailDTO orderDetailDTO ,
                                         BindingResult result) {

        try {
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(e -> e.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            // Save order to database
            return ResponseEntity.ok("Order detail created successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating order detail");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@Valid @PathVariable("id") Long id) {
        try {
            // Get orders from database
            return ResponseEntity.ok("Order detail found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting orders");
        }
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@Valid @PathVariable("orderId") Long orderId) {
        try {
            // Get orders from database
            return ResponseEntity.ok("Order details found");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error getting orders details");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(@Valid @PathVariable("id") Long id,
                                         @Valid @RequestBody OrderDetailDTO newOrderDetailData,
                                         BindingResult result) {
        try {
            if (result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(e -> e.getDefaultMessage())
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            // Update order in database
            return ResponseEntity.ok("Order detail updated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error updating order detail");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable("id") Long id) {
        try {
            // Delete order from database
            return ResponseEntity.ok("Order detail deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error deleting order detail");
        }
    }

}
