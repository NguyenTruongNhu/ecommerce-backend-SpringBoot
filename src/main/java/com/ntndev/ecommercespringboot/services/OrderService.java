package com.ntndev.ecommercespringboot.services;

import com.ntndev.ecommercespringboot.dtos.OrderDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.models.Order;

import java.util.List;

public interface OrderService {

    Order createOrder(OrderDTO orderDTO) throws Exception;

    Order getOrder(Long id) throws DataNotFoundException;

    Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);

}
