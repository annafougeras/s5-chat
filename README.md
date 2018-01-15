
***

**[À l'attention du correcteur...](readme.txt)**

***



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

Créez [l'application client](#make_jars), puis exécutez :

	java -jar appli_client.jar [adresse_serveur port_serveur]

(Par défaut, adresse=localhost et port=8888)

#### Application administrateur

Créez [l'application administrateur](#make_jars), puis exécutez :

	java -jar appli_admin.jar [adresse_serveur port_serveur]

(Par défaut, adresse=localhost et port=8888)

#### Serveur

Deux cas : 

**1. Le serveur dispose du SGDB mySQL**

Créez [le serveur](#make_jars), puis exécutez :

	# Installe la base de données (utilisateur 's5', mot de passe 's5)
	make install_db
	
	# Insère des données bidons
	make -C db fake_data
	
	# Démarre le serveur sur le port xxxx (habituellement 8888) en utilisant la base de données nouvellement créée
	java -jar serveur.jar xxxx local

**2. Le serveur ne dispose pas du SGBD mySQL, mais possède une connexion internet**

Créez [le serveur](#make_jars), puis exécutez :

	# Démarre le serveur sur le port xxxx (habituellement 8888) en utilisant une base de données distante
	java -jar serveur.jar xxxx distant

Le serveur reconnaît quelques commandes :

	list : affiche la liste des clients connectés
	exit : ferme le serveur et quitte

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
	make serveur.jar

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

	# Démonstration : lance le serveur et l'application client (serveur local, db locale)
	make demo
	make demo_local
	
	# Démonstration : lance le serveur et l'application client (serveur local, db distante)
	make demo_distant

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
	
	# Efface la base du serveur mysql local
	make -C db uninstall
	
