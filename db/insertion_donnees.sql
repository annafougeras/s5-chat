-- table `groupe`
INSERT INTO `groupe` (`id_groupe`, `nom_groupe`) VALUES
(1, 'univtlse3');

-- table `user`
INSERT INTO `user` (`id_user`, `nom_user`, `prenom_user`) VALUES
(1, 'pipo', 'pomme', 'pmp'),
(2, 'cuma', 'ty', 'mrq');
(3, 'fougere', 'race', 'fgv');

-- table `ticket`
INSERT INTO `ticket` (`id_ticket`, `titre_ticket`, `creation_ticket`) VALUES
(1, 'Un ticket simple', '2017-11-08 13:09:19');

-- table `message`
INSERT INTO `message` VALUES
(1, 1, 1, 'wsh c\'est pas normal le contrôle de graphe était trop dur, j\'ai dû tout copier sur philibert', '2017-11-08 10:00:00', '0');

-- table `appartenance`
INSERT INTO `appartenance` (`id_user`, `id_groupe`, `inscription`) VALUES
(1, 1, '2017-11-08 00:00:00'),
(2, 1, '2017-11-08 00:00:00');

-- table `status`
INSERT INTO `status` (`id_message`, `id_user`, `status`) VALUES
(1, 1, '0'),
(1, 2, '2');
