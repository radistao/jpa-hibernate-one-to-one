package com.example.accessingdatajpa.onetoone;

import org.springframework.data.repository.CrudRepository;

public interface EntityOneRepository extends CrudRepository<EntityOne, Long> {
}
