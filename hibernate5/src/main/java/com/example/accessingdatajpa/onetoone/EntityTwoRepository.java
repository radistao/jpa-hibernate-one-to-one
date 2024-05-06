package com.example.accessingdatajpa.onetoone;

import org.springframework.data.repository.CrudRepository;

public interface EntityTwoRepository extends CrudRepository<EntityTwo, Long> {
}
