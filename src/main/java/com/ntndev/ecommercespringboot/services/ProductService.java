package com.ntndev.ecommercespringboot.services;

import com.ntndev.ecommercespringboot.dtos.ProductDTO;
import com.ntndev.ecommercespringboot.dtos.ProductImageDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.models.Product;
import com.ntndev.ecommercespringboot.models.ProductImage;
import com.ntndev.ecommercespringboot.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDTO productDTO) throws DataNotFoundException;
    Product getProductById(Long id) throws DataNotFoundException;
    Page<ProductResponse> getAllProducts(String keyword, Long categoryId,PageRequest pageRequest);
    Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException;

    void deleteProduct(Long id);
    boolean existsByName(String name);
    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception;

    List<Product> findProductsByIds(List<Long> productIds);
}
