# s5-chat

### Installation

Rien à installer pour l'instant

### Documentation

La documentation est disponible [sur cette page](http://pomeret.free.fr/s5doc).

### Makefile

Le Makefile présente plusieurs options


Compilation :

	make java

Création de jars :

	# rien pour l'instant

Documentation :

	make doc
	
Base de donnée :

	# Création base et tables
	make install_db
	
	# Plus d'options dans le dossier db/
	make -D db install
	make -D db uninstall
	make -D db fake_data
	make -D db list_tables

Tests :

	# Communicateur réseau générique
	make test_communication
	
	# Communicateur réseau spécifique
	make test_S5com
