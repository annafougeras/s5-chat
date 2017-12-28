# s5-chat

**s5-Chat** est une application de type client-serveur permettant a des *utilisateurs* d'écrire des *messages*.  
Les *utilisateurs* appartiennent à des *groupes*, et postent leurs *messages* sur des *tickets*.  
Ces derniers peuvent être consultés par un *groupe* destinataire, et par l'*utilisateur* émetteur.

**Contenu :**

 - Serveur multi-thread avec base de données
 - Application client : lecture et écriture de messages
 - Application administrateur : modifier tout ce qui est modifiable

Les programmes sont écrit en Java, et offrent donc une compatibilité avec de nombreux OS (Windows, MacOS, Linux'n'cie)

***

### Installation

#### Application client

Récupérez [l'application client](#make_jars), puis exécutez :

	java -jar appli_client.jar [adresse_serveur port_serveur]

#### Application administrateur

Récupérez [l'application administrateur](#make_jars), puis exécutez :

	java -jar appli_admin.jar [adresse_serveur port_serveur]


#### Serveur

La machine sur laquelle le serveur sera installée doit disposer du SGDB mysql *(essayez LAMP / WAMP / MAMP)*  
Installation de la base de données :

	make install_db

*Pour l'instant, pas mieux*

***

### Documentation

La documentation est disponible [sur cette page](http://pomeret.free.fr/s5doc).

***

### Makefile

Le Makefile présente plusieurs options


Compilation :

	make java

<a name="make_jars"></a>
Création de jars :

	# Tous les jars
	make
	
	# Ou indépendamment
	make appli_client.jar
	make appli_admin.jar

Documentation :

	# Documentation
	make doc
	
	# Documentation (visibilité private à public)
	make doc_private
	
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
	
	# Ferme les fenêtres xterm ouvertes pour les tests réseau
	make kill_tests

Nettoyage :

	# Supprime les jars
	make clean
	
	# Supprime tout ce qui peut être re-généré
	make maxclean
