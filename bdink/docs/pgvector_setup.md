# Pgvector Local Setup

## 1) Start pgvector with Docker

```bash
docker compose up -d
```

## 2) Create extension and table

```bash
psql "postgresql://bdink:bdinkpass@localhost:5432/bdink_vector" -f docs/pgvector_setup.sql
```

## 3) App connection defaults

The app uses these defaults unless environment variables override them:

- `pgvector.datasource.url` → `jdbc:postgresql://localhost:5432/bdink_vector`
- `pgvector.datasource.username` → `bdink`
- `pgvector.datasource.password` → `bdinkpass`

You can override them with environment variables or JVM `-D` properties.
