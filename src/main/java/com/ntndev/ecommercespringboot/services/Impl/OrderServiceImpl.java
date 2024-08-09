package com.ntndev.ecommercespringboot.services.Impl;

import com.ntndev.ecommercespringboot.dtos.CartItemDTO;
import com.ntndev.ecommercespringboot.dtos.OrderDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.models.*;
import com.ntndev.ecommercespringboot.repositories.OrderDetailRepository;
import com.ntndev.ecommercespringboot.repositories.OrderRepository;
import com.ntndev.ecommercespringboot.repositories.ProductRepository;
import com.ntndev.ecommercespringboot.repositories.UserRepository;
import com.ntndev.ecommercespringboot.services.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    private final OrderDetailRepository orderDetailRepository;

    // Tạo mới một Order từ OrderDTO
    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws Exception {
        //tìm xem user'id có tồn tại ko
        User user = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException("Cannot find user with id: "+orderDTO.getUserId()));
        //convert orderDTO => Order
        //dùng thư viện Model Mapper
        // Tạo một luồng bảng ánh xạ riêng để kiểm soát việc ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // Cập nhật các trường của đơn hàng từ orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(LocalDate.now());//lấy thời điểm hiện tại
        order.setStatus(OrderStatus.PENDING);
        //Kiểm tra shipping date phải >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null
                ? LocalDate.now() : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException("Date must be at least today !");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);//đoạn này nên set sẵn trong sql
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order);
        // Tạo danh sách các đối tượng OrderDetail từ cartItems
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            // Tạo một đối tượng OrderDetail từ CartItemDTO
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            // Lấy thông tin sản phẩm từ cartItemDTO
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            // Tìm thông tin sản phẩm từ cơ sở dữ liệu (hoặc sử dụng cache nếu cần)
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException("Product not found with id: " + productId));

            // Đặt thông tin cho OrderDetail
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            // Các trường khác của OrderDetail nếu cần
            orderDetail.setPrice(product.getPrice());

            // Thêm OrderDetail vào danh sách
            orderDetails.add(orderDetail);
        }


        // Lưu danh sách OrderDetail vào cơ sở dữ liệu
        orderDetailRepository.saveAll(orderDetails);
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
    @Transactional
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
    @Transactional
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
