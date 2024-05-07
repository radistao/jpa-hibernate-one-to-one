package com.example.accessingdatajpa.onetoone;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class Hibernate6EntityTest {

  @Autowired
  EntityOneRepository entityOneRepository;

  @Autowired
  EntityTwoRepository entityTwoRepository;

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
  void withRelation_withoutFindAll_Success() {
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
