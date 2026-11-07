CREATE TABLE tb_equipamentos (
                                 id BINARY(16) NOT NULL,
                                 nome VARCHAR(150) NOT NULL,
                                 tipo VARCHAR(80) NOT NULL,
                                 patrimonio VARCHAR(80) NOT NULL,
                                 disponivel BOOLEAN NOT NULL DEFAULT TRUE,
                                 reserva_emprestimo_id BINARY(16) NULL,

                                 CONSTRAINT pk_tb_equipamentos
                                     PRIMARY KEY (id),

                                 CONSTRAINT uk_tb_equipamentos_patrimonio
                                     UNIQUE (patrimonio)
);