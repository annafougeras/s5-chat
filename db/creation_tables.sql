--
-- Création des tables
--

-- table `groupe`
CREATE TABLE IF NOT EXISTS `groupe` (
  `id_groupe` int(4) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `nom_groupe` varchar(64) NOT NULL
);

CREATE TABLE IF NOT EXISTS `ticket` (
  `id_ticket` int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `titre_ticket` varchar(64) NOT NULL,
  `creation_ticket` datetime NOT NULL,
  `id_groupe` int(8) NOT NULL,
    CONSTRAINT fk_groupe_ticket FOREIGN KEY (id_groupe) REFERENCES groupe(id_groupe)
);

-- table `user`
CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(8) NOT NULL PRIMARY KEY AUTO_INCREMENT,
  `password_user` varchar(32) NOT NULL,
  `nickname_user` varchar(32) NOT NULL,
  `nom_user` varchar(32) NOT NULL,
  `prenom_user` varchar(32) NOT NULL
);

-- table `message`
CREATE TABLE IF NOT EXISTS `message` (
    `id_message` int(16) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `id_ticket` int(8) NOT NULL,
    `id_user` int(11) NOT NULL,
    `contenu` text NOT NULL,
    `date_message` datetime NOT NULL,
    CONSTRAINT fk_ticket_message FOREIGN KEY (id_ticket) REFERENCES ticket(id_ticket),
    CONSTRAINT fk_user_message FOREIGN KEY (id_user) REFERENCES user(id_user)
);

-- table `appartenance`
CREATE TABLE IF NOT EXISTS `appartenance` (
    `id_user` int(8) NOT NULL,
    `id_groupe` int(4) NOT NULL,
    `inscription` datetime NOT NULL,
    CONSTRAINT fk_user_appartenance FOREIGN KEY (id_user) REFERENCES user(id_user),
    CONSTRAINT fk_groupe_appartenance FOREIGN KEY (id_groupe) REFERENCES groupe(id_groupe),
    CONSTRAINT pk_appartenance PRIMARY KEY (id_user,id_groupe)
);

-- table `statut`
CREATE TABLE IF NOT EXISTS `statut` (
  `id_message` int(16) NOT NULL,
  `id_user` int(8) NOT NULL,
  `statut` enum('0','1','2','') NOT NULL,
    CONSTRAINT fk_msg_statut FOREIGN KEY (id_message) REFERENCES message(id_message),
    CONSTRAINT fk_user_statut FOREIGN KEY (id_user) REFERENCES user(id_user),
    CONSTRAINT pk_statut PRIMARY KEY (id_message,id_user)
);
