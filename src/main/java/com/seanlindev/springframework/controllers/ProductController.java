package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.dto.ProductDto;
import com.seanlindev.springframework.api.request.ProductDetailsRequestModel;
import com.seanlindev.springframework.api.response.ProductResponse;
import com.seanlindev.springframework.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ProductResponse createProduct(@RequestBody ProductDetailsRequestModel productDetailsRequestModel) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        ProductDto productDto = modelMapper.map(productDetailsRequestModel, ProductDto.class);
        ProductDto createdProductDto = productService.createProduct(productDto);
        return modelMapper.map(createdProductDto, ProductResponse.class);
    }
}
