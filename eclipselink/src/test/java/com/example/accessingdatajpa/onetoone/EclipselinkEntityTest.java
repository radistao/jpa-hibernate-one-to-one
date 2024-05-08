package com.example.accessingdatajpa.onetoone;

import com.example.accessingdatajpa.EclipseLinkJpaConfiguration;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(EclipseLinkJpaConfiguration.class)
class EclipselinkEntityTest {

  @Autowired
  EntityOneRepository entityOneRepository;

  @Autowired
  EntityTwoRepository entityTwoRepository;

  @Autowired
  EntityManager entityManager;

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
  void withRelation_and_eclipselink4_Success() {
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

  @Test
  void withRelation_and_eclipselink4_and_Flush_Success() {
    var one1 = saveOne("first-one");
    var one2 = saveOne("second-one");

    var two1 = saveTwo("first-two", one1);
    var two2 = saveTwo("second-two", one1);

    one1.setEntityTwo(two1);
    one2.setEntityTwo(two1);
    entityOneRepository.save(one1);
    entityOneRepository.save(one2);

    entityManager.flush();

    assertThat(entityOneRepository.findAll())
        .extracting(EntityOne::getName)
        .containsExactlyInAnyOrder("first-one", "second-one");

    assertThat(entityTwoRepository.findAll())
        .extracting(EntityTwo::getName)
        .containsExactlyInAnyOrder("first-two", "second-two");

    entityManager.flush();

    entityOneRepository.deleteAll();
    assertThat(entityOneRepository.findAll()).isEmpty();

    entityTwoRepository.deleteAll();
    assertThat(entityTwoRepository.findAll()).isEmpty();

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
