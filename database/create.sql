CREATE TABLE Moomintroll (
  moomintroll_id BIGSERIAL PRIMARY KEY,
  name TEXT NOT NULL,
  is_male BOOLEAN NOT NULL,
  color BIGINT NOT NULL,
  kindness BIGINT NOT NULL,
  position BIGINT NOT NULL
);