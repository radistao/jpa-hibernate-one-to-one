package com.example.accessingdatajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Hibernate6Application {

  public static void main(String[] args) {
    SpringApplication.run(Hibernate6Application.class);
  }


}
