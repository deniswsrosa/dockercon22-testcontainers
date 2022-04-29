CREATE TABLE wallet (
  id BIGSERIAL PRIMARY KEY,
  game_user_id varchar(255) not null,
  quantity BIGSERIAL not null
);
