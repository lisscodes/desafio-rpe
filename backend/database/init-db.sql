-- Create clientes table
CREATE TABLE IF NOT EXISTS clientes (
                                        id SERIAL PRIMARY KEY,
                                        nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    data_nascimento DATE NOT NULL,
    status_bloqueio VARCHAR(1) NOT NULL CHECK (status_bloqueio IN ('A', 'B')),
    limite_credito DOUBLE PRECISION NOT NULL
    );

-- Create faturas table
CREATE TABLE IF NOT EXISTS faturas (
                                       id SERIAL PRIMARY KEY,
                                       cliente_id BIGINT NOT NULL REFERENCES clientes(id),
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    valor DECIMAL(10, 2) NOT NULL,
    status VARCHAR(1) NOT NULL CHECK (status IN ('P', 'A', 'B'))
    );

-- Insert example data into clientes
INSERT INTO clientes (nome, cpf, data_nascimento, status_bloqueio, limite_credito) VALUES
                                                                                       ('João Silva', '12345678901', '1990-05-15', 'A', 1000.00),
                                                                                       ('Maria Santos', '98765432100', '1985-10-22', 'A', 2000.00),
                                                                                       ('Ana Costa', '11122233344', '1995-03-10', 'B', 0.00),
                                                                                       ('Pedro Almeida', '55566677788', '1988-07-20', 'A', 1500.00),
                                                                                       ('Lucas Pereira', '99988877766', '1992-12-05', 'A', 3000.00),
                                                                                       ('Clara Mendes', '44455566633', '1993-01-30', 'B', 0.00),
                                                                                       ('Fernanda Lima', '77788899911', '1987-06-25', 'A', 2500.00),
                                                                                       ('Rafael Souza', '22233344455', '1990-09-12', 'A', 1800.00),
                                                                                       ('Beatriz Oliveira', '66677788899', '1994-04-18', 'A', 2200.00),
                                                                                       ('Gabriel Rocha', '33344455566', '1991-11-11', 'A', 1700.00);

-- Insert example data into faturas
INSERT INTO faturas (cliente_id, data_vencimento, data_pagamento, valor, status) VALUES
                                                                                     (1, '2025-06-01', NULL, 150.75, 'B'),
                                                                                     (1, '2025-05-01', '2025-05-02', 200.00, 'P'),
                                                                                     (2, '2025-07-01', NULL, 300.50, 'B'),
                                                                                     (3, '2025-04-01', NULL, 120.00, 'A'),
                                                                                     (4, '2025-06-15', NULL, 180.25, 'B'),
                                                                                     (5, '2025-07-10', NULL, 250.00, 'B'),
                                                                                     (6, '2025-03-01', NULL, 100.00, 'A'),
                                                                                     (7, '2025-06-20', '2025-06-18', 400.00, 'P'),
                                                                                     (8, '2025-07-05', NULL, 175.50, 'B'),
                                                                                     (9, '2025-06-25', NULL, 220.75, 'B');