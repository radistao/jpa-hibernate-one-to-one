The entities and tests are the same. The difference only in Spring Boot, Hibernate and JPA version:


| Module       | `hibernate5` | `hibernate6`  |
|--------------|--------------|---------------|
| Spring boot  | 2.7          | 3.2           |
| Hibernate    | 5.6          | 6.3           |
| JPA          | 2.2 (Javax)  | 3.1 (Jakarta) |
| test results | success      | fail          |


Try to run tests in `hibernate5` and `hibernate` directories:
```
./gradlew clean test
# or
./mvnw clean test
```

**Notes**:
  * exception happens not on inserting a "duplicate" one-to-one relation, but later during `ResultSet` mapping
    when trying to `findAll()` or `deleteAll()`
