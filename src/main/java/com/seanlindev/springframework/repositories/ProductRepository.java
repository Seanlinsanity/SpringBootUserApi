package com.seanlindev.springframework.repositories;

import com.seanlindev.springframework.model.entities.ProductEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, Long> {
}
