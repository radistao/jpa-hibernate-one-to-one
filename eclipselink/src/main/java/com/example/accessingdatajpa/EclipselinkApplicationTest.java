package com.example.accessingdatajpa;

import com.example.accessingdatajpa.onetoone.EntityOne;
import com.example.accessingdatajpa.onetoone.EntityOneRepository;
import com.example.accessingdatajpa.onetoone.EntityTwo;
import com.example.accessingdatajpa.onetoone.EntityTwoRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.StreamSupport;

@Component
public class EclipselinkApplicationTest {

  private static final Logger log = LoggerFactory.getLogger(EclipselinkApplicationTest.class);

  private final EntityOneRepository entityOneRepository;
  private final EntityTwoRepository entityTwoRepository;
  private final EntityManager entityManager;


  EclipselinkApplicationTest(EntityOneRepository entityOneRepository, EntityTwoRepository entityTwoRepository, EntityManager entityManager) {
    this.entityOneRepository = entityOneRepository;
    this.entityTwoRepository = entityTwoRepository;
    this.entityManager = entityManager;
  }

  @Transactional
  @EventListener(ContextRefreshedEvent.class)
  void saveFindDelete_worksPerfectWithEclipselink(ContextRefreshedEvent event) {
    // given
    var one1 = saveOne("first-one");
    var one2 = saveOne("second-one");

    var two1 = saveTwo("first-two", one1);
    var two2 = saveTwo("second-two", one1);

    // when
    one1.setEntityTwo(two1);
    one2.setEntityTwo(two1);
    entityOneRepository.save(one1);
    entityOneRepository.save(one2);

    entityManager.flush();

    // then
    log.info("##########################################################################################");
    log.info("Find all");
    StreamSupport.stream(entityOneRepository.findAll().spliterator(), false)
        .forEach(e -> log.info("entityOne: {}", e));

    StreamSupport.stream(entityTwoRepository.findAll().spliterator(), false)
        .forEach(e -> log.info("entityTwo: {}", e));

    entityManager.flush();

    log.info("##########################################################################################");
    log.info("Delete all");
    entityOneRepository.deleteAll();
    entityTwoRepository.deleteAll();
    entityManager.flush();
  }

  private EntityOne saveOne(String name) {
    var entityOne = new EntityOne();
    entityOne.setName(name);
    return entityOneRepository.save(entityOne);
  }

  private EntityTwo saveTwo(String name, EntityOne entityOne) {
    var entityTwo = new EntityTwo();
    entityTwo.setName(name);
    entityTwo.setEntityOne(entityOne);
    return entityTwoRepository.save(entityTwo);
  }

}
