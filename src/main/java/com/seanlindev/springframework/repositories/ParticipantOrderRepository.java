package com.seanlindev.springframework.repositories;

import com.seanlindev.springframework.model.entities.ParticipantOrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantOrderRepository extends CrudRepository<ParticipantOrderEntity, Long> {
}
