/* CRIA SCHEMA */
CREATE SCHEMA `mobicare` DEFAULT CHARACTER SET utf8;

/* CRIA TABELA SETOR */
CREATE TABLE `mobicare`.`setor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nome` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ilff4g8q8odqgs0ihr7uedsow` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


/* CRIA TABELA COLABORADOR */
CREATE TABLE `mobicare`.`colaborador` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cpf` varchar(11) NOT NULL,
  `email` varchar(50) NOT NULL,
  `nome` varchar(60) NOT NULL,
  `telefone` varchar(15) NOT NULL,
  `fk_id_setor` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dluwox59xryije1xr98mqc60n` (`cpf`),
  KEY `FKl57wo6vwjjeypjjgdjt725m7e` (`fk_id_setor`),
  CONSTRAINT `FKl57wo6vwjjeypjjgdjt725m7e` FOREIGN KEY (`fk_id_setor`) REFERENCES `mobicare`.`setor` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;


/* CRIA TABELA USER */
CREATE TABLE `mobicare`.`user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admin` bit(1) NOT NULL,
  `name` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  `username` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

