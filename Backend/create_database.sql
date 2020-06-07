-- Será utilizado apenas quando o MySQL não estiver criado

-- Criando o database que será utilizado
CREATE DATABASE TCC;

USE TCC;

-- Criando as tabelas
CREATE TABLE Clientes (
	id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cpf varchar(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL);

CREATE TABLE Compras (
	id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    data DATE NOT NULL,
    valor_total FLOAT NOT NULL);

CREATE TABLE Itens_comprados (
	id INT AUTO_INCREMENT PRIMARY KEY,
    id_compras INT NOT NULL,
    id_produtos INT NOT NULL,
    valor FLOAT NOT NULL);

CREATE TABLE Produtos (
	id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    valor FLOAT NOT NULL);

-- Adicionando FOREIGN KEYS
ALTER TABLE Compras
ADD FOREIGN KEY (id_cliente) REFERENCES Clientes(id);

ALTER TABLE Itens_comprados
ADD FOREIGN KEY (id_compras) REFERENCES Compras(id);

ALTER TABLE Itens_comprados
ADD FOREIGN KEY (id_produtos) REFERENCES Produtos(id);

-- Populando Clientes
INSERT INTO Clientes (nome, cpf, data_nascimento) VALUES ("Leonardo", "00000000000", "1997-05-25");
INSERT INTO Clientes (nome, cpf, data_nascimento) VALUES ("Luan", "11111111111", "1995-01-13");
INSERT INTO Clientes (nome, cpf, data_nascimento) VALUES ("Matheus", "22222222222", "1997-06-04");
INSERT INTO Clientes (nome, cpf, data_nascimento) VALUES ("Paulo", "33333333333", "1990-10-22");
INSERT INTO Clientes (nome, cpf, data_nascimento) VALUES ("Alex", "44444444444", "1996-09-26");

-- Populando Compras
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (1, "1997-05-25", 530.5);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (1, "2020-05-25", 292.9);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (2, "2020-06-05", 789.1);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (2, "2020-05-05", 515.8);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (2, "2020-04-05", 971.7);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (2, "2020-03-05", 56.9);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (3, "2020-01-24", 1003.8);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (3, "2020-02-13", 38.8);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (3, "2020-03-20", 890.7);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (4, "2020-04-20", 1003.8);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (5, "2020-03-28", 123.4);
INSERT INTO Compras (id_cliente, data, valor_total) VALUES (5, "2020-04-24", 321.85);

-- Populando Produtos
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Xadrez", 100);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Xadrez 2", 123.3);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Xadrez 3", 147.2);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Xadrez 4", 77);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Xadrez 5", 81.4);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Xadrez 2", 123.9);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Xadrez 3", 99.5);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Polo", 50);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Polo 1", 59);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Polo 2", 55.9);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Polo 3", 85.9);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Polo 2", 65.9);
INSERT INTO Produtos (nome, valor) VALUES ("Camisa Polo 2", 95.9);
INSERT INTO Produtos (nome, valor) VALUES ("Tenis Xadrez", 325);
INSERT INTO Produtos (nome, valor) VALUES ("Tenis polo", 300.5);
INSERT INTO Produtos (nome, valor) VALUES ("Tenis DC", 400);
INSERT INTO Produtos (nome, valor) VALUES ("Tenis sapato", 150.9);
INSERT INTO Produtos (nome, valor) VALUES ("Tenis s/ cadarço", 551.9);
INSERT INTO Produtos (nome, valor) VALUES ("Tenis Xadrez 2", 225.8);
INSERT INTO Produtos (nome, valor) VALUES ("Calça Xadrez 2", 23.9);
INSERT INTO Produtos (nome, valor) VALUES ("Calça Xadrez 3", 89.5);
INSERT INTO Produtos (nome, valor) VALUES ("Calça Polo", 51.0);
INSERT INTO Produtos (nome, valor) VALUES ("Calça Polo 1", 159.9);

-- Populando Itens_comprados
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (1, 22, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (1, 13, 200.1);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (1, 2, 170.5);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (2, 20, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (2, 1, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (2, 5, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (3, 21, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (3, 11, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (3, 10, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (4, 9, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (4, 8, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (4, 15, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (4, 16, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (5, 3, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (5, 4, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (5, 19, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (6, 3, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (7, 5, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (7, 8, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (7, 7, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (8, 23, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (8, 20, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (8, 13, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (9, 14, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (9, 15, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (9, 16, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (10, 17, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (10, 18, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (10, 23, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (11, 20, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (11, 1, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (11, 2, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (12, 5, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (12, 8, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (13, 8, 159.9);
INSERT INTO Itens_comprados (id_compras, id_produtos, valor) VALUES (13, 17, 159.9);