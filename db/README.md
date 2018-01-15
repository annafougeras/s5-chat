# Dossier src/

Base de données


### Modèle de données

![Modèle de données](../doc/modele_de_donnees.svg  "MDD")


### Instructions pour manipuler la base de données

Situées dans ces fichiers : 

 - creation_base.sql
 - creation_tables.sql
 - insertion_donnees.sql
 - suppression_base.sql


### Makefile

Le makefile permet d'exécuter les instructions sql.
On peut modifier deux paramètres : 

	HOST  = 127.0.0.1
	USER  = root


Installation

	# Exécute les fichiers creation_base.sql et creation_table.sql
	make install

Insertion de données factices

	# Exécute le fichier insertion_donnees.sql
	make fake_data

Suppression de la base
	
	# Exécute le fichier suppression_base.sql
	make uninstall

Affiche la base
	
	# Structure de chacune des tables de la base
	make list_tables
