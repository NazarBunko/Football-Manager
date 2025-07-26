CREATE TABLE team (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(255) NOT NULL CHECK (LENGTH(name) <= 255),
                      money BIGINT NOT NULL CHECK (money > 0),
                      percent DOUBLE PRECISION CHECK (percent > 0),
                      photo BYTEA
);

CREATE TABLE player (
                        id SERIAL PRIMARY KEY,
                        team_id BIGINT REFERENCES team(id) ON DELETE CASCADE,
                        name VARCHAR(100) NOT NULL CHECK (LENGTH(name) BETWEEN 1 AND 100),
                        age INT NOT NULL CHECK (age > 0),
                        position VARCHAR(100) NOT NULL CHECK (LENGTH(position) BETWEEN 1 AND 100),
                        experience INT NOT NULL CHECK (experience > 0),
                        photo BYTEA
);

INSERT INTO team (name, money, percent, photo) VALUES
                                                   ('Real Madrid', 500000000, 10.5, NULL),
                                                   ('FC Barcelona', 480000000, 9.8, NULL),
                                                   ('Manchester United', 450000000, 9.2, NULL);

INSERT INTO player (team_id, name, age, position, experience, photo) VALUES
                                                                         (1, 'Vinicius Jr.', 23, 'Forward', 60, NULL),
                                                                         (1, 'Jude Bellingham', 21, 'Midfielder', 50, NULL),
                                                                         (1, 'Toni Kroos', 34, 'Midfielder', 120, NULL),
                                                                         (1, 'David Alaba', 31, 'Defender', 110, NULL),
                                                                         (1, 'Thibaut Courtois', 32, 'Goalkeeper', 130, NULL),

                                                                         (2, 'Robert Lewandowski', 35, 'Forward', 150, NULL),
                                                                         (2, 'Pedri', 21, 'Midfielder', 50, NULL),
                                                                         (2, 'Frenkie de Jong', 27, 'Midfielder', 100, NULL),
                                                                         (2, 'Ronald Araujo', 25, 'Defender', 70, NULL),
                                                                         (2, 'Marc-André ter Stegen', 32, 'Goalkeeper', 140, NULL),

                                                                         (3, 'Bruno Fernandes', 29, 'Midfielder', 120, NULL),
                                                                         (3, 'Marcus Rashford', 27, 'Forward', 90, NULL),
                                                                         (3, 'Casemiro', 32, 'Midfielder', 130, NULL),
                                                                         (3, 'Raphaël Varane', 31, 'Defender', 110, NULL),
                                                                         (3, 'André Onana', 28, 'Goalkeeper', 100, NULL);
