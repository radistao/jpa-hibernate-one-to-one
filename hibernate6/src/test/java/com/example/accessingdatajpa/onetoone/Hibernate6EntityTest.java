package com.example.accessingdatajpa.onetoone;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

@DataJpaTest
class Hibernate6EntityTest {

  @Autowired
  EntityOneRepository entityOneRepository;

  @Autowired
  EntityTwoRepository entityTwoRepository;

  @Autowired
  EntityManager entityManager;

  @Test
  void withoutRelation_Success() {
    // given
    final var saved1 = saveOne("first-one");
    final var saved2 = saveOne("second-one");

    // then
    assertThat(entityOneRepository.findAll())
        .extracting(EntityOne::getName)
        .containsExactlyInAnyOrder("first-one", "second-one");

    entityOneRepository.deleteAll();
    assertThat(entityOneRepository.findAll()).isEmpty();
  }

  @Test
  void withRelation_and_hibernate6_Fail() {
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

    // then
    assertThat(entityOneRepository.findAll())
        .extracting(EntityOne::getName)
        .containsExactlyInAnyOrder("first-one", "second-one");

    assertThat(entityTwoRepository.findAll())
        .extracting(EntityTwo::getName)
        .containsExactlyInAnyOrder("first-two", "second-two");

    entityOneRepository.deleteAll();
    assertThat(entityOneRepository.findAll()).isEmpty();

    entityTwoRepository.deleteAll();
    assertThat(entityTwoRepository.findAll()).isEmpty();
  }

  @Test
  void withRelation_withoutFindAll_but_FindById_deleteById_Success() {
    // given
    var one1 = saveOne("first-one");
    var one2 = saveOne("second-one");

    var two1 = saveTwo("first-two", one1);

    // when
    one1.setEntityTwo(two1);
    one2.setEntityTwo(two1);
    entityOneRepository.save(one1);
    entityOneRepository.save(one2);

    // then
    assertThat(entityOneRepository.findById(one1.getId()))
        .map(EntityOne::getName)
        .contains("first-one");

    assertThat(entityOneRepository.findById(one2.getId()))
        .map(EntityOne::getName)
        .contains("second-one");

    assertThat(entityTwoRepository.findById(two1.getId()))
        .map(EntityTwo::getName)
        .contains("first-two");

    entityOneRepository.deleteAllById(List.of(one1.getId(), one2.getId()));
    assertThat(entityOneRepository.findAll()).isEmpty();

    // this is also now safe as references to EntityOne removed
    assertThat(entityTwoRepository.findAll())
        .extracting(EntityTwo::getName)
        .containsExactlyInAnyOrder("first-two");
    entityTwoRepository.deleteAll();
    assertThat(entityTwoRepository.findAll()).isEmpty();
  }

  @Test
  void withOneRelation_and_hibernate6_and_Flush_Success() {
    var one1 = saveOne("first-one");
    entityManager.flush();

    var one2 = saveOne("second-one");
    entityManager.flush();

    var two1 = saveTwo("first-two", one1);
    entityManager.flush();

    var two2 = saveTwo("second-two", one1);
    entityManager.flush();

    assertThat(entityOneRepository.findAll())
        .extracting(EntityOne::getName, e -> e.getEntityTwo())
        .containsExactlyInAnyOrder(tuple("first-one", null), tuple("second-one", null));

    assertThat(entityTwoRepository.findAll())
        .extracting(EntityTwo::getName, e -> e.getEntityOne().getName())
        .containsExactlyInAnyOrder(tuple("first-two", "first-one"), tuple("second-two", "first-one"));

    entityOneRepository.deleteAll();
    entityTwoRepository.deleteAll();
  }

  @Test
  void withBidirectionalRelation_and_hibernate6_and_Flush_failsOnSaveTwo() {
    var one1 = saveOne("first-one");
    entityManager.flush();

    var one2 = saveOne("second-one");
    entityManager.flush();

    var two1 = saveTwo("first-two", one1);
    entityManager.flush();

    var two2 = saveTwo("second-two", one1);
    entityManager.flush();

    one1.setEntityTwo(two1);
    entityManager.flush(); // this succeeds for `update entity_one set entity_two_id=1,name=second-one where id=1

    one2.setEntityTwo(two1);
    entityManager.flush(); // exception: Constraint ViolationException: Unique index or primary key violation: "PUBLIC.UK_8BV0X1MY481SFLIMS9L8BAX12_INDEX_C ON PUBLIC.ENTITY_ONE(ENTITY_TWO_ID NULLS FIRST) VALUES ( /* 1 */ CAST(1 AS BIGINT) )"; SQL statement:
                           // update entity_one set entity_two_id=1,name=second-one where id=2
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
