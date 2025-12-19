-- pgvector setup for Postgres (run once per database)
create extension if not exists vector;

-- Exercise embedding table for pgvector
create table if not exists exercise_embedding (
  id bigserial primary key,
  createdAt timestamp,
  updatedAt timestamp,
  exercise_id bigint not null,
  embedding_text text,
  embedding_vector vector(1536)
);

create index if not exists idx_exercise_embedding_exercise_id
  on exercise_embedding (exercise_id);

-- Approximate nearest neighbor index (optional, tune later)
create index if not exists idx_exercise_embedding_vector_hnsw
  on exercise_embedding using hnsw (embedding_vector vector_cosine_ops);
