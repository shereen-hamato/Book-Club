

CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username varchar(255) NOT NULL,
  password varchar(255) NOT NULL
);

CREATE TABLE genre(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name CHAR(50) NOT NULL UNIQUE,
  description VARCHAR(2000) NOT NULL
);

CREATE TABLE book (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  overview VARCHAR(2000) NOT NULL,
  author VARCHAR(50) NOT NULL,
  isdn VARCHAR(50) NOT NULL,
  image_url VARCHAR(2000) not null,
  book_genre BIGINT not null,
  book_owner BIGINT not null
);

ALTER TABLE book ADD FOREIGN KEY (book_genre) REFERENCES genre(id);
ALTER TABLE book ADD FOREIGN KEY (book_owner) REFERENCES user(id);


CREATE TABLE rating (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  rank INTEGER,
  comment VARCHAR(2000),
  read_status Boolean,
  user_id INTEGER not null,
  book_id INTEGER not null
);

ALTER TABLE rating ADD FOREIGN KEY (user_id) REFERENCES user(id);
ALTER TABLE rating ADD FOREIGN KEY (book_id) REFERENCES book(id);
ALTER TABLE rating ADD UNIQUE MyConstraint (book_id, user_id);

