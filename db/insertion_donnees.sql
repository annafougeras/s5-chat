-- table `groupe`
INSERT INTO `groupe` (`id_groupe`, `nom_groupe`) VALUES
(1, 'TDA1'),
(2, 'TDA2'),
(3, 'Univ-Tlse3');

-- table `user`
INSERT INTO `user` (`id_user`, `nickname_user`, `nom_user`, `prenom_user`) VALUES
(1, 'pipo1118m', 'Paumerai', 'Peter'),
(2, 'vifo0077i', 'Fougere', 'Vins'),
(3, 'cuma0340a', 'Marty', 'Hubert');

-- table `appartenance`
INSERT INTO `appartenance` (`id_user`, `id_groupe`, `inscription`) VALUES
(1, 2, '2017-11-01 00:00:00'),
(1, 3, '2017-11-01 00:00:00'),
(2, 2, '2017-11-01 00:00:00'),
(2, 3, '2017-11-03 00:00:00'),
(3, 1, '2017-11-01 00:00:00'),
(3, 3, '2017-11-02 00:00:00');

-- table `ticket`
INSERT INTO `ticket` (`id_ticket`, `titre_ticket`, `creation_ticket`, `id_groupe`) VALUES
(1, 'Chauffage', '2017-11-08 00:00:00', 3);

-- table `message`
INSERT INTO `message` (`id_message`, `id_ticket`, `id_user`, `contenu`, `date_message`) VALUES
(1, 1, 2, 'Le chauffe marche plus, il fait froid. Reparez le !', '2017-11-14 00:00:00'),
(2, 1, 1, 'Ouais c\'est pas faux !', '2017-11-15 00:00:00');

-- table `status`
INSERT INTO `statut` (`id_message`, `id_user`, `statut`) VALUES
(1, 1, '2'),
(1, 2, '2'),
(1, 3, '1'),
(2, 1, '2'),
(2, 2, '0'),
(2, 3, '0');
