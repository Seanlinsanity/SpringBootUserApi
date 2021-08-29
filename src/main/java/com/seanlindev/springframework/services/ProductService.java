package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.ProductDto;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
}
