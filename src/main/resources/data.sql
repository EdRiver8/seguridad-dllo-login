-- Precarga de usuarios de Dragon Ball Z
-- Contraseñas: {bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG (password: "password")

INSERT INTO users (username, password, full_name, race, power_level, role, enabled) VALUES
('goku', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Son Goku', 'Saiyan', 9000000000, 'WARRIOR', true),
('vegeta', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Vegeta', 'Saiyan', 8500000000, 'WARRIOR', true),
('gohan', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Son Gohan', 'Half-Saiyan', 7000000000, 'WARRIOR', true),
('piccolo', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Piccolo', 'Namekian', 5000000000, 'WARRIOR', true),
('trunks', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Trunks', 'Half-Saiyan', 6500000000, 'WARRIOR', true),
('krillin', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Krillin', 'Human', 75000, 'WARRIOR', true),
('bulma', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Bulma', 'Human', 5, 'ADMIN', true),
('master_roshi', '{bcrypt}$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Master Roshi', 'Human', 139, 'MASTER', true);
