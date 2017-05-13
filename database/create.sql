CREATE TABLE Moomintroll (
  moomintroll_id BIGSERIAL PRIMARY KEY,
  name           TEXT    NOT NULL,
  is_male        BOOLEAN NOT NULL,
  color          INT     NOT NULL,
  kindness       INT     NOT NULL,
  position       BIGINT  NOT NULL
);


CREATE FUNCTION update_moomintroll(
  id           BIGINT,
  new_name     TEXT,
  new_is_male  BOOLEAN,
  new_color    INT,
  new_kindness INT,
  new_position BIGINT)
  RETURNS VOID AS $$
UPDATE Moomintroll
SET name = $2, is_male = $3, color = $4, kindness = $5, position = $6
WHERE moomintroll_id = $1;
$$ LANGUAGE SQL;