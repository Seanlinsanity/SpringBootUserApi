package com.seanlindev.springframework.controllers;

import com.seanlindev.springframework.api.dto.ProductDto;
import com.seanlindev.springframework.api.request.ProductDetailsRequestModel;
import com.seanlindev.springframework.api.response.ProductResponse;
import com.seanlindev.springframework.services.ProductService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @PostMapping()
    public ProductResponse createProduct(@RequestBody ProductDetailsRequestModel productDetailsRequestModel) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        ProductDto productDto = modelMapper.map(productDetailsRequestModel, ProductDto.class);
        ProductDto createdProductDto = productService.createProduct(productDto);
        return modelMapper.map(createdProductDto, ProductResponse.class);
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value= "Bearer JWT Token", paramType = "hearder")
    })
    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable String id) throws Exception {
        ModelMapper modelMapper = new ModelMapper();
        ProductDto productDto = productService.getProductByProductId(id);
        return modelMapper.map(productDto, ProductResponse.class);
    }
}
