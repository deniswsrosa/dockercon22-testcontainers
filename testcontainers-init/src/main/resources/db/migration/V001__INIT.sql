CREATE TABLE game_user (
  id BIGSERIAL PRIMARY KEY,
  name varchar(255) not null,
  email varchar(255) not null
);

CREATE TABLE wallet (
  id BIGSERIAL PRIMARY KEY,
  game_user_id BIGSERIAL not null,
  quantity INTEGER not null,
  CONSTRAINT game_user_ctr FOREIGN KEY (game_user_id) REFERENCES game_user (id)
);
