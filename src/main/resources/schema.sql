CREATE TABLE teams (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(255) NOT NULL CHECK (LENGTH(name) <= 255),
                       money BIGINT NOT NULL CHECK (money > 0),
                       percent DOUBLE PRECISION CHECK (percent > 0),
                       photo VARCHAR(255) NOT NULL CHECK (LENGTH(photo) <= 255)
);

CREATE TABLE players (
                         id SERIAL PRIMARY KEY,
                         team_id BIGINT REFERENCES teams(id) ON DELETE CASCADE,
                         name VARCHAR(100) NOT NULL CHECK (LENGTH(name) BETWEEN 1 AND 100),
                         age INT NOT NULL CHECK (age > 0),
                         position VARCHAR(100) NOT NULL CHECK (LENGTH(position) BETWEEN 1 AND 100),
                         experience INT NOT NULL CHECK (experience > 0),
                         photo VARCHAR(255) NOT NULL CHECK (LENGTH(photo) <= 255)
);


-- Додавання команд
INSERT INTO teams (name, money, percent, photo) VALUES
                                                    ('Real Madrid', 500000000, 10.5, 'real_madrid_logo.png'),
                                                    ('FC Barcelona', 480000000, 9.8, 'barcelona_logo.png'),
                                                    ('Manchester United', 450000000, 9.2, 'manchester_united_logo.png');

-- Додавання гравців для Real Madrid (id команди буде 1, якщо автоінкремент йде з 1)
INSERT INTO players (team_id, name, age, position, experience, photo) VALUES
                                                                          (1, 'Vinicius Jr.', 23, 'Forward', 60, 'vinicius.png'),
                                                                          (1, 'Jude Bellingham', 21, 'Midfielder', 50, 'bellingham.png'),
                                                                          (1, 'Toni Kroos', 34, 'Midfielder', 120, 'kroos.png'),
                                                                          (1, 'David Alaba', 31, 'Defender', 110, 'alaba.png'),
                                                                          (1, 'Thibaut Courtois', 32, 'Goalkeeper', 130, 'courtois.png');

-- Додавання гравців для FC Barcelona (id команди буде 2)
INSERT INTO players (team_id, name, age, position, experience, photo) VALUES
                                                                          (2, 'Robert Lewandowski', 35, 'Forward', 150, 'lewandowski.png'),
                                                                          (2, 'Pedri', 21, 'Midfielder', 50, 'pedri.png'),
                                                                          (2, 'Frenkie de Jong', 27, 'Midfielder', 100, 'dejong.png'),
                                                                          (2, 'Ronald Araujo', 25, 'Defender', 70, 'araujo.png'),
                                                                          (2, 'Marc-André ter Stegen', 32, 'Goalkeeper', 140, 'ter_stegen.png');

-- Додавання гравців для Manchester United (id команди буде 3)
INSERT INTO players (team_id, name, age, position, experience, photo) VALUES
                                                                          (3, 'Bruno Fernandes', 29, 'Midfielder', 120, 'fernandes.png'),
                                                                          (3, 'Marcus Rashford', 27, 'Forward', 90, 'rashford.png'),
                                                                          (3, 'Casemiro', 32, 'Midfielder', 130, 'casemiro.png'),
                                                                          (3, 'Raphaël Varane', 31, 'Defender', 110, 'varane.png'),
                                                                          (3, 'André Onana', 28, 'Goalkeeper', 100, 'onana.png');
