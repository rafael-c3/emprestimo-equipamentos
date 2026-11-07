CREATE TABLE tb_usuarios (
                             id BINARY(16) NOT NULL,
                             nome VARCHAR(150) NOT NULL,
                             email VARCHAR(180) NOT NULL,
                             matricula VARCHAR(50) NOT NULL,
                             CONSTRAINT pk_tb_usuarios PRIMARY KEY (id),
                             CONSTRAINT uk_tb_usuarios_email UNIQUE (email),
                             CONSTRAINT uk_tb_usuarios_matricula UNIQUE (matricula)
);