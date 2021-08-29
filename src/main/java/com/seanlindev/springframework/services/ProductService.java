package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    ProductDto getProductByProductId(String productId);
    List<ProductDto> getProducts(int page, int limit);
}
