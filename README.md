The entities and tests are the same. The difference only in Spring Boot, Hibernate and JPA version:

|              | 5            | 6            |
|--------------|--------------|--------------|
| module       | `hibernate5` | `hibernate6` |
| Spring boot  | 2            | 3            |
| Hibernate    | 5            | 6            |
| JPA          | Javax        | Jakarta      |
| test results | success      | failed       |


Try to run tests in `hibernate5` and `hibernate` directories:
```
./gradlew clean test
# or
./mvnw clean test
```
