package com.example.accessingdatajpa.onetoone;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class EntityTwo {
  @Id
  @GeneratedValue(strategy= GenerationType.AUTO)
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

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EntityTwo{");
    sb.append("id=").append(id);
    sb.append(", name='").append(name).append('\'');
    sb.append(", entityOne_id=").append(entityOne.getId());
    sb.append('}');
    return sb.toString();
  }
}
