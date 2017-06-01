CREATE TABLE Moomintroll (
  moomintroll_id BIGSERIAL PRIMARY KEY,
  name           TEXT    NOT NULL,
  is_male        BOOLEAN NOT NULL,
  color          INT     NOT NULL,
  kindness       INT     NOT NULL,
  position       BIGINT  NOT NULL,
  creation_date  TIMESTAMP WITH TIME ZONE
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

CREATE OR REPLACE FUNCTION set_date()
  RETURNS TRIGGER AS $$
BEGIN
  IF new.creation_date IS NULL
  THEN
    UPDATE Moomintroll
    SET creation_date = current_timestamp
    WHERE moomintroll_id = new.moomintroll_id;
  END IF;
  RETURN new;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_date_trigger
AFTER INSERT ON Moomintroll
FOR EACH ROW
EXECUTE PROCEDURE set_date();