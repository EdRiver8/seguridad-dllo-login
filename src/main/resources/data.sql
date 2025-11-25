-- Precarga de usuarios de Dragon Ball Z
-- Contraseñas: password (hasheada con BCrypt)
-- IMPORTANTE: Se eliminó el prefijo {bcrypt} ya que PasswordEncoder ya lo maneja

-- USUARIOS CON ROL USER (Guerreros)
INSERT INTO users (username, password, full_name, race, power_level, role, enabled) VALUES
('goku', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Son Goku', 'Saiyan', 9000000000, 'USER', true),
('vegeta', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Vegeta', 'Saiyan', 8500000000, 'USER', true),
('gohan', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Son Gohan', 'Half-Saiyan', 7000000000, 'USER', true),
('piccolo', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Piccolo', 'Namekian', 5000000000, 'USER', true),
('trunks', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Trunks', 'Half-Saiyan', 6500000000, 'USER', true),
('krillin', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Krillin', 'Human', 75000, 'USER', true);

-- USUARIOS CON ROL ADMIN (Administradores del sistema)
INSERT INTO users (username, password, full_name, race, power_level, role, enabled) VALUES
('bulma', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Bulma', 'Human', 5, 'ADMIN', true),
('master_roshi', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'Master Roshi', 'Human', 139, 'ADMIN', true);