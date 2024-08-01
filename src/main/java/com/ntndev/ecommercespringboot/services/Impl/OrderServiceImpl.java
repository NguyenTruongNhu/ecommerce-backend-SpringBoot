package com.ntndev.ecommercespringboot.services.Impl;

import com.ntndev.ecommercespringboot.dtos.OrderDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.models.Order;
import com.ntndev.ecommercespringboot.models.OrderStatus;
import com.ntndev.ecommercespringboot.models.User;
import com.ntndev.ecommercespringboot.repositories.OrderRepository;
import com.ntndev.ecommercespringboot.repositories.UserRepository;
import com.ntndev.ecommercespringboot.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    // Tạo mới một Order từ OrderDTO
    @Override
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        // Lấy thông tin User từ repository theo ID
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        // Tạo một đối tượng Order mới
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus(OrderStatus.PENDING);

        // Kiểm tra và đặt ngày giao hàng
        LocalDate shippingDate = orderDTO.getShippingDate() == null ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new Exception("Date must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);

        // Lưu đối tượng Order mới vào repository và trả về kết quả
        orderRepository.save(order);
        return order;
    }

    // Lấy một Order theo ID
    @Override
    public Order getOrder(Long id) throws DataNotFoundException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));
    }

    // Cập nhật một Order theo ID và dữ liệu từ OrderDTO
    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO) throws DataNotFoundException {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        User existingUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        // Cập nhật các thuộc tính của Order
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);

        // Lưu lại Order đã cập nhật vào repository và trả về kết quả
        return orderRepository.save(order);
    }

    // Xóa một Order theo ID
    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    // Tìm danh sách Order theo User ID
    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
