package com.example.accessingdatajpa.onetoone;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class EntityOne {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
  private Long id;

  private String name;

  @OneToOne
  private EntityTwo entityTwo;

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

  public EntityTwo getEntityTwo() {
    return entityTwo;
  }

  public void setEntityTwo(EntityTwo entityTwo) {
    this.entityTwo = entityTwo;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityOne{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", entityTwo_id=").append(entityTwo.getId());
    sb.append('}');
    return sb.toString();
  }
}
