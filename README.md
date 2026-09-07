# Crate-Simulator

Simulates crate (loot box) openings to visualise drop rates and provide analytics. Built as three Spring Boot microservices communicating over Kafka, each owning its own Postgres database.

## Services

- crate-service: CRUD and admin approval for crates
- reward-service: CRUD and admin approval for rewards, with chance weightings
- opening-service: single and bulk simulated openings, publishing open requests to Kafka and processing them through a listener

## Running it

Everything spins up through docker compose (three Postgres instances, Zookeeper, Kafka and the services):

```
docker compose up --build
```

## Tests

Unit tests per service for controllers, services, the Kafka publisher and listener, and exception handlers. They run on every push via GitHub Actions:

```
cd backend/<service>
./gradlew test
```

Mutation testing with PIT, report lands in `build/reports/pitest`:

```
./gradlew pitest
```

The context-loads tests need a Postgres instance. The Feign base URLs and database settings are read from a `.env` in the repo root when present, otherwise from environment variables.

Development is done through feature branches and pull requests, with issue and PR templates in .github.
