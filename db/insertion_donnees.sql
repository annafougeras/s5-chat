-- table `groupe`
INSERT INTO `groupe` (`id_groupe`, `nom_groupe`) VALUES
(1, 'Groupe 1.1'),
(2, 'Groupe 1.2'),
(3, 'Groupe 2.1'),
(4, 'Groupe 2.2'),
(5, 'Groupe 3.1'),
(6, 'Groupe 3.2'),
(7, 'Groupe 4.1'),
(8, 'Groupe 4.2'),
(9, 'Secretariat'),
(10, 'Corpo Sciences'),
(11, 'Univ-Tlse3');

-- table `user` mdp : root1 (grain de sel : "qt" && grain de poivre : "pi")
INSERT INTO `user` (`id_user`, `nickname_user`, `password_user`, `nom_user`, `prenom_user`) VALUES
(1, 'pipo1118m', 'faaf32985c1f39ca41c28786d4695c77d1d25d6a47d93fc9bc8032f2b49e0e36', 'Paumerai', 'Peter'),
(2, 'vifo0077i', 'faaf32985c1f39ca41c28786d4695c77d1d25d6a47d93fc9bc8032f2b49e0e36',  'Fougere', 'Vins'),
(3, 'cuma0340a', 'faaf32985c1f39ca41c28786d4695c77d1d25d6a47d93fc9bc8032f2b49e0e36',  'Marty', 'Hubert'),
(4, 'aze', 'faaf32985c1f39ca41c28786d4695c77d1d25d6a47d93fc9bc8032f2b49e0e36',  'Doe', 'John');

-- table `appartenance`
INSERT INTO `appartenance` (`id_user`, `id_groupe`, `inscription`) VALUES
(1, 2, '2017-11-01 00:00:00'),
(1, 10, '2017-11-01 00:00:00'),
(1, 11, '2017-11-01 00:00:00'),
(2, 2, '2017-11-01 00:00:00'),
(2, 11, '2017-11-03 00:00:00'),
(3, 2, '2017-11-01 00:00:00'),
(3, 11, '2017-11-02 00:00:00');

-- table `ticket`
INSERT INTO `ticket` (`id_ticket`, `titre_ticket`, `creation_ticket`, `id_groupe`) VALUES
(1, 'Chauffage', '2017-11-08 00:00:00', 9);

-- table `message`
INSERT INTO `message` (`id_message`, `id_ticket`, `id_user`, `contenu`, `date_message`) VALUES
(1, 1, 1, 'Le chauffe marche plus, il fait froid. Reparez le !', '2017-11-14 00:00:00'),
(2, 1, 3, 'Ouais c\'est pas faux !', '2017-11-15 00:00:00');

-- table `status`
INSERT INTO `statut` (`id_message`, `id_user`, `statut`) VALUES
(1, 1, '2'),
(1, 2, '0'),
(1, 3, '2'),
(2, 1, '1'),
(2, 2, '1'),
(2, 3, '2');
