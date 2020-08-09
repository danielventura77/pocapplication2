/* INSERE SETORES */
INSERT INTO `mobicare`.`setor`(`nome`)VALUES('Setor A');
INSERT INTO `mobicare`.`setor`(`nome`)VALUES('Setor B');
INSERT INTO `mobicare`.`setor`(`nome`)VALUES('Setor C');

/* INSERE COLABORADORES */
INSERT INTO `mobicare`.`colaborador`(`cpf`,`email`,`nome`,`telefone`, `fk_id_setor`)VALUES('07874836723','dsventura@gmail.com','Daniel Ventura','(21) 97781-2923', 1);
INSERT INTO `mobicare`.`colaborador`(`cpf`,`email`,`nome`,`telefone`, `fk_id_setor`)VALUES('36510033020','mariajose@gmail.com','Maria José','(21) 88681-2663', 2);
INSERT INTO `mobicare`.`colaborador`(`cpf`,`email`,`nome`,`telefone`, `fk_id_setor`)VALUES('77033057075','luiz@gmail.com','Luiz Bertollo','(21) 97681-2113', 2);
INSERT INTO `mobicare`.`colaborador`(`cpf`,`email`,`nome`,`telefone`, `fk_id_setor`)VALUES('24770535074','amanda@gmail.com','Amanda Rineu','(21) 96661-2913', 3);
INSERT INTO `mobicare`.`colaborador`(`cpf`,`email`,`nome`,`telefone`, `fk_id_setor`)VALUES('43863113004','yan@gmail.com','Yan Lee','(21) 97681-2773', 3);

/* INSERE USUÁRIOS DA APLICAÇÃO (Senhas: pass) */
INSERT INTO `mobicare`.`user`(`admin`,`name`,`password`,`username`)VALUES(0,'Usuário Guest (Somente Consultas)','$2a$10$XA2G8sz4mxet9n0e8KsnZ.R6EzipMG2Xx60ZHvRt6Pb3vl8pDOLGq','user');
INSERT INTO `mobicare`.`user`(`admin`,`name`,`password`,`username`)VALUES(1,'Usuário Admin (Acesso Full)','$2a$10$XA2G8sz4mxet9n0e8KsnZ.R6EzipMG2Xx60ZHvRt6Pb3vl8pDOLGq','admin');