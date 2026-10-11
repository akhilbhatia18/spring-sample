# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/4.1.1/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/4.1.1/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/4.1.1/reference/web/servlet.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/4.1.1/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/4.1.1/reference/actuator/index.html)
* [Spring Cache Abstraction](https://docs.spring.io/spring-boot/4.1.1/reference/io/caching.html)
* [OAuth2 Authorization Server](https://docs.spring.io/spring-boot/4.1.1/reference/web/spring-security.html#web.security.oauth2.authorization-server)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Caching Data with Spring](https://spring.io/guides/gs/caching/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### Student API

The API uses the in-memory H2 database. The `students` table is created at startup
from [`schema.sql`](./src/main/resources/schema.sql); Hibernate validates the table
against the JPA entity. Sample student records are inserted at startup from
[`data.sql`](./src/main/resources/data.sql). The table and records are lost when the
application stops.
The H2 console is available at `/h2-console`.

### Application login

The sample account is seeded into the H2 `users` and `authorities` tables at
startup by [`data.sql`](./src/main/resources/data.sql). Use username `student`
and password `Student@123` to sign in at `/login` or authenticate API requests
with HTTP Basic. Passwords are stored as BCrypt hashes, not plaintext.

This is a shared, known demo credential and must not be used for a public or
production deployment. Replace it with a unique secret and use persistent,
managed identity storage before exposing the application to real users.

### CI, Render deployment, and health checks

The GitHub Actions workflow at `.github/workflows/ci-cd.yml` runs tests and
packages the application on each push. A push to the repository's default
branch triggers the Render deploy hook only after verification succeeds.

Create the Render service from [`render.yaml`](./render.yaml), then add its
deploy-hook URL to the GitHub repository as the `RENDER_DEPLOY_HOOK_URL` Actions
secret. Automatic Render Git deployments are disabled in the blueprint to avoid
deploying before the workflow passes.

The service listens on Render's `PORT` and uses `/actuator/health` as its public
health check. Liveness and readiness checks are also available at
`/actuator/health/liveness` and `/actuator/health/readiness`. Only health is
exposed, and the health routes do not require authentication.

The current H2 database uses in-memory storage, so student data is not durable
and is lost when the service restarts or is redeployed. Use a persistent database
before relying on this deployment for retained data.

All endpoints use the existing application security configuration:

| Method | Path | Operation |
| --- | --- | --- |
| `GET` | `/api/students` | List students |
| `GET` | `/api/students/{id}` | Get a student |
| `POST` | `/api/students` | Create a student (`201 Created`) |
| `PUT` | `/api/students/{id}` | Replace a student's details |
| `DELETE` | `/api/students/{id}` | Delete a student (`204 No Content`) |

Create and update requests use JSON with `name`, `email`, and `course` fields, for example:

```json
{
  "name": "Alex Student",
  "email": "alex@example.com",
  "course": "Computer Science"
}
```

Student lookups and lists are cached in memory and the caches are cleared after
successful create, update, and delete operations.

Import [`Student API.postman_collection.json`](./postman/Student%20API.postman_collection.json)
into Postman to try the health check and student CRUD flow. Set the collection's
`password` variable before sending requests. Run **Get CSRF Token** before write
requests; Postman will keep the session cookie and token for those requests.

### Review guides

Repository-level, tool-neutral review instructions are available for use by
Claude, Grok, and other coding tools:

* [Code review](./CODE_REVIEW.md)
* [Architecture review](./ARCHITECTURE_REVIEW.md)
* [Security review](./SECURITY_REVIEW.md)

Each guide includes reviewer instructions, expected output, and a reusable
prompt. Run independent reviews separately and verify findings against the
repository; these guides do not imply that any external model has reviewed the
code.

In VS Code Chat, type `/review` to run the combined code, architecture, and
security review prompt from `.github/prompts/review.prompt.md`. You can add
optional files or a review focus after the command.
