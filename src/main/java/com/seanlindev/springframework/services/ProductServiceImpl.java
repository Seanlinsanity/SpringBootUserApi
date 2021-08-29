package com.seanlindev.springframework.services;

import com.seanlindev.springframework.api.dto.ProductDto;
import com.seanlindev.springframework.model.entities.ProductEntity;
import com.seanlindev.springframework.repositories.ProductRepository;
import com.seanlindev.springframework.shared.utils.PublicIdGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ProductDto getProductByProductId(String productId) {
        if (productId.isEmpty()) {
            throw new RuntimeException("Product Id is empty");
        }

        ProductEntity productEntity = productRepository.findByProductId(productId);
        if (productEntity == null) {
            throw new RuntimeException("Invalid productId: " + productId);
        }
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(productEntity, ProductDto.class);
    }

    @Override
    public List<ProductDto> getProducts(int page, int limit) {
        if (page > 0)
            page = page - 1;
        PageRequest pageRequest = PageRequest.of(page, limit);
        Pageable pageable = pageRequest.toOptional().get();
        Page<ProductEntity> productsPage = productRepository.findAll(pageable);
        return productsPage.get().map(productEntity -> {
            ModelMapper modelMapper = new ModelMapper();
            ProductDto productDto = modelMapper.map(productEntity, ProductDto.class);
            return productDto;
        }).collect(Collectors.toList());    }
}
