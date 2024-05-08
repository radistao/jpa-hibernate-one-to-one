The entities and tests are the same. The difference only in Spring Boot, Hibernate and JPA version:


| Module       | `hibernate5` | `hibernate6`  | `EclipseLink4` |
|--------------|--------------|---------------|----------------|
| Spring boot  | 2.7          | 3.2           | 3.2            |
| Hibernate    | 5.6          | 6.3           |
| EclipseLink  | -            | -             | 4.0            |
| JPA          | 2.2 (Javax)  | 3.1 (Jakarta) | 3.1 (Jakarta)  |
| test results | success      | fail          | success        |


Try to run tests in `hibernate5`, `hibernate6` and `eclipselink` directories:
```
./gradlew clean test
# or
./mvnw clean test
```

**Notes**:
  * exception happens after flushing/persisting changes: inserting a "duplicate" one-to-one relation.
    Seems like in hibernate 6 this happens right before calling `findAll()` or `deleteAll()`
    * forcing `flush()` also causes exception on saving _second_ relation
      (see `withBidirectionalRelation_and_hibernate6_and_Flush_failsOnSaveTwo`)
  * In **Hibernate 5** when running the application (see `Hibernate5ApplicationTest`)
    * with `@Transactional` works fine!
    * without `@Transactional` - fails with "More than one row with the given identifier was found"
  * In **Hibernate 6** when running the application (see `Hibernate6ApplicationTest`) - fails with our without
    `@Transactional`: "ConstraintViolationException: Unique index or primary key violation"
  * **Eclipselink** works fine even with `flush()`
