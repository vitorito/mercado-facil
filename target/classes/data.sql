insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, CATEGORIA, PRECO, IS_DISPONIVEL)
values(10001,'Leite Integral', '87654321-B', 'Parmalat', 'Mercearia', 4.5, FALSE);

insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, CATEGORIA, PRECO, IS_DISPONIVEL)
values(10002,'Arroz Integral', '87654322-B', 'Tio Joao', 'Perecíveis', 5.5, FALSE);

insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, CATEGORIA, PRECO, IS_DISPONIVEL)
values(10003,'Sabao em Po', '87654323-B', 'OMO', 'Limpeza', 12, FALSE);

insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, CATEGORIA, PRECO, IS_DISPONIVEL)
values(10004,'Agua Sanitaria', '87654324-C', 'Dragao', 'Limpeza', 3, FALSE);

insert into produto (ID, NOME, CODIGO_BARRA, FABRICANTE, CATEGORIA, PRECO, IS_DISPONIVEL)
values(10005,'Creme Dental', '87654325-C', 'Colgate', 'Higiene', 2.5, FALSE);

insert into lote (ID, PRODUTO_ID, QUANTIDADE)
values(1, 10005, 5);

update produto set IS_DISPONIVEL = TRUE where ID = 10005;

insert into cliente (ID, CPF, NOME, IDADE, ENDERECO)
values(1001, 10020030006, 'Fulano', 23, 'Rua tal');
