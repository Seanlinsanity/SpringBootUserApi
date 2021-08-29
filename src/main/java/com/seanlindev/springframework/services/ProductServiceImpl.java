package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.ProductDto;
import com.seanlindev.springframework.model.entities.ProductEntity;
import com.seanlindev.springframework.repositories.ProductRepository;
import com.seanlindev.springframework.shared.utils.PublicIdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;
    private PublicIdGenerator publicIdGenerator;

    public ProductServiceImpl(ProductRepository productRepository,
                              PublicIdGenerator publicIdGenerator) {
        this.productRepository = productRepository;
        this.publicIdGenerator = publicIdGenerator;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        if (productDto.getName().isEmpty() || productDto.getPrice().compareTo(new BigDecimal(0)) != 1) {
            throw new RuntimeException("Invalid product");
        }
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(publicIdGenerator.generateProductId(30));
        productEntity.setPrice(productDto.getPrice());
        productEntity.setName(productDto.getName());
        ProductEntity savedProduct = productRepository.save(productEntity);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(savedProduct, ProductDto.class);
    }
}
