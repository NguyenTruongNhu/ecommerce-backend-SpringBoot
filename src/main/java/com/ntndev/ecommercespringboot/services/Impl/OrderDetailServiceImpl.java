package com.ntndev.ecommercespringboot.services.Impl;

import com.ntndev.ecommercespringboot.dtos.OrderDetailDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.models.Order;
import com.ntndev.ecommercespringboot.models.OrderDetail;
import com.ntndev.ecommercespringboot.models.Product;
import com.ntndev.ecommercespringboot.repositories.OrderDetailRepository;
import com.ntndev.ecommercespringboot.repositories.OrderRepository;
import com.ntndev.ecommercespringboot.repositories.ProductRepository;
import com.ntndev.ecommercespringboot.services.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    // Tạo mới một OrderDetail từ OrderDetailDTO
    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws Exception {
        // Lấy thông tin Order từ repository theo ID
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        // Lấy thông tin Product từ repository theo ID
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        // Tạo một đối tượng OrderDetail mới
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .price(orderDetailDTO.getPrice())
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .color(orderDetailDTO.getColor())
                .build();

        // Lưu đối tượng OrderDetail mới vào repository và trả về kết quả
        return orderDetailRepository.save(orderDetail);
    }

    // Lấy một OrderDetail theo ID
    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Order detail not found"));
    }

    // Cập nhật một OrderDetail theo ID và dữ liệu từ OrderDetailDTO
    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        OrderDetail existingOrderDetail = orderDetailRepository.findById(id).orElse(null);
        Order existingOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException("Order not found"));

        Product existingProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        // Cập nhật các thuộc tính của OrderDetail
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);

        // Lưu lại OrderDetail đã cập nhật vào repository và trả về kết quả
        return orderDetailRepository.save(existingOrderDetail);
    }

    // Xóa một OrderDetail theo ID
    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    // Tìm danh sách OrderDetail theo Order ID
    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}
