package com.example.accessingdatajpa;

import com.example.accessingdatajpa.onetoone.EntityOne;
import com.example.accessingdatajpa.onetoone.EntityOneRepository;
import com.example.accessingdatajpa.onetoone.EntityTwo;
import com.example.accessingdatajpa.onetoone.EntityTwoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.stream.StreamSupport;

@Component
public class Hibernate6ApplicationTest {

  private static final Logger log = LoggerFactory.getLogger(Hibernate6ApplicationTest.class);

  private final EntityOneRepository entityOneRepository;
  private final EntityTwoRepository entityTwoRepository;

  Hibernate6ApplicationTest(EntityOneRepository entityOneRepository, EntityTwoRepository entityTwoRepository) {
    this.entityOneRepository = entityOneRepository;
    this.entityTwoRepository = entityTwoRepository;
  }


  @EventListener(ContextRefreshedEvent.class)
   @Transactional
  // Note: Opposite to Hibernate5 this will fail with or without @Transactional:
  // "ConstraintViolationException: Unique index or primary key violation"
  public void hibernate6_withNonUniqueRelations_SuccessWhenTransactional(ContextRefreshedEvent event) {
    // given
    var one1 = saveOne("transactional-first-one");
    var one2 = saveOne("transactional-second-one");

    var two1 = saveTwo("transactional-first-two", one1);
    var two2 = saveTwo("transactional-second-two", one1);

    // when
    one1.setEntityTwo(two1);
    one2.setEntityTwo(two1);
    entityOneRepository.save(one1);
    entityOneRepository.save(one2);

    // then
    StreamSupport.stream(entityOneRepository.findAll().spliterator(), false)
        .forEach(e -> log.info("entityOne: name={}, entityTwo={}", e.getName(), e.getEntityTwo().getName()));

    StreamSupport.stream(entityTwoRepository.findAll().spliterator(), false)
        .forEach(e -> log.info("entityTwo: name={}, entityOne={}", e.getName(), e.getEntityOne().getName()));
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
