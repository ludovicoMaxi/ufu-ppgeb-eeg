INSERT INTO car ( id, brand, model, color, year, price, description, IS_NEW, CREATED_DATE, UPDATED_DATE) VALUES ( 1,'FORD', 'Focus', 'Preto', 2015, 35000.10, '2.0', 1, TO_DATE('01/01/2017','dd/MM/RRRR'), null );
INSERT INTO car ( id, brand, model, color, year, price, description, IS_NEW, CREATED_DATE, UPDATED_DATE) VALUES ( 2,'CHEVROLET', 'CORSA', 'BRANCO', 2011, 22000.10, '1.0', 1, TO_DATE('01/01/2017','dd/MM/RRRR'), null );
INSERT INTO car ( id, brand, model, color, year, price, description, IS_NEW, CREATED_DATE, UPDATED_DATE) VALUES ( 3,'HONDA', 'CIVIC', 'AZUL', 2017, 90000.10, '2.0', 1, TO_DATE('01/01/2017','dd/MM/RRRR'), null );
INSERT INTO car ( id, brand, model, color, year, price, description, IS_NEW, CREATED_DATE, UPDATED_DATE) VALUES ( 4,'VOLKSWAGEM', 'GOL', 'AMARELO', 2018, 45000.10, '1.6', 1, TO_DATE('01/01/2017','dd/MM/RRRR'), null );

INSERT INTO customer ( ID, NAME, DOCUMENT_NUMBER, CREATED_AT, CREATED_BY, BIRTHDATE, NACIONALITY, CIVIL_STATUS, JOB) VALUES ( 1,'João Ludovico', '09574539652', TO_DATE('05/09/2018','dd/MM/RRRR'), 'SYSTEM', TO_DATE('25/01/1991','dd/MM/RRRR'), 'BRASILEIRA', 'SOLTEIRO', 'ANALISTA DE TI' );
INSERT INTO customer ( ID, NAME, DOCUMENT_NUMBER, CREATED_AT, CREATED_BY, BIRTHDATE, NACIONALITY, CIVIL_STATUS, JOB) VALUES ( 2,'Rafael Caetano', '08074539652', TO_DATE('05/09/2018','dd/MM/RRRR'), 'SYSTEM', TO_DATE('12/06/1991','dd/MM/RRRR'), 'BRASILEIRA', 'CASADO', 'ANALISTA DE TI' );

INSERT INTO contact ( ID, NAME, CELLPHONE, OBJECT_ID, OBJECT_TYPE, CREATED_AT, CREATED_BY, MAIN, FLAG_ACTIVE) VALUES (1,'João', '034999909311', 1, 1, TO_DATE('05/09/2018','dd/MM/RRRR'), 'SYSTEM', true, true)
INSERT INTO contact ( ID, NAME, CELLPHONE, OBJECT_ID, OBJECT_TYPE, CREATED_AT, CREATED_BY, FLAG_ACTIVE) VALUES (2,'Maria', '034999909311', 1, 1, TO_DATE('05/09/2018','dd/MM/RRRR'), 'SYSTEM', true)
INSERT INTO contact ( ID, NAME, CELLPHONE, OBJECT_ID, OBJECT_TYPE, CREATED_AT, CREATED_BY, FLAG_ACTIVE) VALUES (3,'Caetano', '034999909010', 2, 1, TO_DATE('05/09/2018','dd/MM/RRRR'), 'SYSTEM', true)

INSERT INTO object_type ( id, name) values (1, 'CUSTOMER');