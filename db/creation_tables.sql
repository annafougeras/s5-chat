--
-- Création des tables
--

-- table `groupe`
CREATE TABLE `groupe` (
  `id_groupe` int(4) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `nom_groupe` varchar(64) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- table `user`
CREATE TABLE `user` (
  `id_user` int(8) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `nickname_user` varchar(32) NOT NULL,
  `nom_user` varchar(32) NOT NULL,
  `prenom_user` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- table `ticket`
CREATE TABLE `ticket` (
  `id_ticket` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `titre_ticket` varchar(64) NOT NULL,
  `creation_ticket` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- table `message`
CREATE TABLE `message` (
	`id_message` int(16) NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`id_ticket` int(8) NOT NULL,
	`id_user` int(11) NOT NULL,
	`contenu` text NOT NULL,
	`date_message` datetime NOT NULL,
	CONSTRAINT fk_ticket_message FOREIGN KEY (id_ticket) REFERENCES ticket(id_ticket),
	CONSTRAINT fk_user_message FOREIGN KEY (id_user) REFERENCES user(id_user)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- table `appartenance`
CREATE TABLE `appartenance` (
	`id_user` int(8) NOT NULL,
	`id_groupe` int(4) NOT NULL,
	`inscription` datetime NOT NULL,
	CONSTRAINT fk_user_appartenance FOREIGN KEY (id_user) REFERENCES user(id_user),
	CONSTRAINT fk_groupe_appartenance FOREIGN KEY (id_groupe) REFERENCES groupe(id_groupe),
	CONSTRAINT pk_appartenance PRIMARY KEY (id_user,id_groupe)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;



-- table `statut`
CREATE TABLE `statut` (
  `id_message` int(16) NOT NULL,
  `id_user` int(8) NOT NULL,
  `statut` enum('0','1','2','') NOT NULL,
	CONSTRAINT fk_msg_statut FOREIGN KEY (id_message) REFERENCES message(id_message),
	CONSTRAINT fk_user_statut FOREIGN KEY (id_user) REFERENCES user(id_user),
	CONSTRAINT pk_statut PRIMARY KEY (id_message,id_user)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

