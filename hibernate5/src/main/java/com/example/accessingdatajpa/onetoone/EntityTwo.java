package com.example.accessingdatajpa.onetoone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class EntityTwo {
  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private String name;

  @OneToOne(mappedBy = "entityTwo")
  private EntityOne entityOne;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public EntityOne getEntityOne() {
    return entityOne;
  }

  public void setEntityOne(EntityOne entityOne) {
    this.entityOne = entityOne;
  }
}
