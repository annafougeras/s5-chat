# s5-chat



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

	# Communicateur réseau
	make test_communication
