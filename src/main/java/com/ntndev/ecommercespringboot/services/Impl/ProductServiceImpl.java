package com.ntndev.ecommercespringboot.services.Impl;

import com.ntndev.ecommercespringboot.dtos.ProductDTO;
import com.ntndev.ecommercespringboot.dtos.ProductImageDTO;
import com.ntndev.ecommercespringboot.exceptions.DataNotFoundException;
import com.ntndev.ecommercespringboot.exceptions.InvalidParamException;
import com.ntndev.ecommercespringboot.models.Category;
import com.ntndev.ecommercespringboot.models.Product;
import com.ntndev.ecommercespringboot.models.ProductImage;
import com.ntndev.ecommercespringboot.repositories.CategoryRepository;
import com.ntndev.ecommercespringboot.repositories.ProductImageRepository;
import com.ntndev.ecommercespringboot.repositories.ProductRepository;
import com.ntndev.ecommercespringboot.responses.ProductResponse;
import com.ntndev.ecommercespringboot.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    // Tạo mới một Product từ ProductDTO
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        // Lấy thông tin Category từ repository theo ID
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() ->
                        new DataNotFoundException(
                                "Category not found with id " + productDTO.getCategoryId()));

        // Tạo một đối tượng Product mới
        Product product = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .category(existingCategory)
                .description(productDTO.getDescription())
                .build();

        // Lưu đối tượng Product mới vào repository và trả về kết quả
        return productRepository.save(product);
    }

    // Lấy một Product theo ID
    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id " + id));
    }

    // Lấy tất cả các Product và trả về dưới dạng trang (Page)
    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest)
                .map(ProductResponse::fromProduct);
    }

    // Cập nhật một Product theo ID và dữ liệu từ ProductDTO
    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);

        if (existingProduct != null) {
            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() ->
                            new DataNotFoundException(
                                    "Category not found with id " + productDTO.getCategoryId()));

            // Cập nhật các thuộc tính của Product
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setCategory(existingCategory);
            existingProduct.setDescription(productDTO.getDescription());

            // Lưu lại Product đã cập nhật vào repository và trả về kết quả
            return productRepository.save(existingProduct);
        }
        return null;
    }

    // Xóa một Product theo ID
    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optProduct = productRepository.findById(id);

        optProduct.ifPresent(productRepository::delete);
    }

    // Kiểm tra sự tồn tại của Product theo tên
    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    // Tạo mới một ProductImage từ ProductImageDTO
    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO) throws Exception {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found with id " + productId));

        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        // Kiểm tra số lượng hình ảnh của Product
        int size = productImageRepository.findByProductId(productId).size();
        if (size > ProductImage.MAXIMUM_IMAGES_PER_PRODUCT) {
            throw new InvalidParamException("Number of images for a product must not exceed " + ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }

        // Lưu đối tượng ProductImage mới vào repository và trả về kết quả
        return productImageRepository.save(newProductImage);
    }
}
