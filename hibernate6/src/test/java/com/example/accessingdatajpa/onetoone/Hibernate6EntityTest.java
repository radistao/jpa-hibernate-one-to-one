package com.example.accessingdatajpa.onetoone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class Hibernate6EntityTest {

  @Autowired
  EntityOneRepository entityOneRepository;

  @Autowired
  EntityTwoRepository entityTwoRepository;

  @Test
  void withoutRelation_Success() {
    final var saved1 = saveOne("first-one");
    final var saved2 = saveOne("second-one");

    assertThat(entityOneRepository.findAll())
        .extracting(EntityOne::getName)
        .containsExactlyInAnyOrder("first-one", "second-one");

    entityOneRepository.deleteAll();
    assertThat(entityOneRepository.findAll()).isEmpty();
  }

  @Test
  void withRelation_and_hibernate6_fails() {
    var one1 = saveOne("first-one");
    var one2 = saveOne("second-one");

    var two1 = saveTwo("first-two", one1);
    var two2 = saveTwo("second-two", one1);

    one1.setEntityTwo(two1);
    one2.setEntityTwo(two1);
    entityOneRepository.save(one1);
    entityOneRepository.save(one2);

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
