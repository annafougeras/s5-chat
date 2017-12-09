Ce document est une pré-ébauche de dossier de conception. J'ai mis un peu de ce dont je m'occupe, et le reste au pif. Vous pouvez modifier, ajouter, etc. selon vos goûts :-)



# Dossier de conception

Logiciel : Projet S5

Equipe de développement :

 - Vincent FOUGERAS
 - Quentin MARTY
 - Pierre POMERET-COQUOT






## 1. Cahier des charges

Production de trois logiciels :

 - Application client
 - Application administrateur
 - Serveur


### 1.a. Application client

Interface graphique client.

Fonctionnalités :

 - Connexion avec couple identifiant/mot de passe
 - Consultation de la liste des groupes
 - Création d'un ticket à destination d'un groupe
 - Ajout d'un message sur un ticket (crée par l'utilisateur ou a destination de l'un de ses groupes).
 - Consultation des tickets et messages

Propriétés des messages : 

 - Date d'émission
 - Statut de lecture (non lu, lu, etc.)


### 1.b. Application administrateur

Ne peut être démarrée que sur le même processeur que le serveur.

Fonctionnalités :

 - Création/suppression de groupes
 - Création/suppression de comptes utilisateur
 - Création/suppression de liens d'appartenance utilisateur-groupe
 - Suppression de tickets / messages

### 1.c. Serveur

Serveur gérant les applications client et administrateur.

Fonctionnalités :

 - Permettre la connexion simultanée de multiples applications client
 - Répondre aux requètes des applications client
 - Vérifier la validité des requetes provenant des applications client
 - Enregistrer l'information nécessaire aux applications client.
 - Permettre la connexion d'une application administrateur
 - Exécuter et répondre aux requêtes de l'application administrateur

Le serveur utilisera une base de données





## 2. Analyse

Analyse du problème

### 2.1. Modèle de données

L'information peut être modélisée ainsi :

![Modèle de données](modele_de_donnees.svg  "Modèle de données")

Maintenant on peut travailler :-)


## 3. Axes de développement

L'application sera écrite en Java. La base de donnée utilisera le SGDB mySQL.

### 3.1. Modèle de données

#### 3.1.a. SQL

#### 3.1.b. Java

Au sein d'un package *modele* destiné aux applications et au serveur.

L'information sera contenue dans les classes suivantes : 

 - Groupe (contient des tickets)
 - Ticket (contient des messages)
 - Message (contient un utilisateur)
 - Utilisateur

Chacune de ces classes peut être identifiée par un entier ou une chaîne de caractère unique.
Par ailleurs, la classe Ticket pourra être instanciée avec des lacunes (sans savoir les messages contenus dans ce ticket) : ces lacunes seront comblées à la demande par l'application.

Diagramme de classe : 
![Diagramme de classe](class_diagram_modele.png  "Diagramme de classe")

### 3.2. Communication réseau

Idée générale :

![organisation du réseau](reseau/reseau.png  "Organisation du réseau")

Communication sur TCP : 

![Communication](reseau/com_reseau.png  "Communication")


La communication réseau est assurée par trois packages :

#### 3.2.a. Package *communication*

Interfaces pour un outil générique permettant la connexion authentifiée de clients à un serveur, et leur dialogue. 

 - **ControleurComClient** : dialogue avec un ControleurComServeur
 - **ControleurComServeur** : dialogue avec plusieurs ControleurComClient
 - **ObservateurComClient** : est informé des messages reçus par un ControleurComClient
 - **ObservateurComServeur** : est informé des messages reçus par un ControleurComServeur
 
Prévu pour être réutilisé dans d'autres logiciels : ne dépend pas du modèle de données choisi.

#### 3.2.b. Package *communication.simple*

Implémentations concrètes des interfaces définies dans le package *communication*.

#### 3.2.c. Package *commChatS5*

Implémentation de contrôleurs spécifiques aux applications client :

 - Transmission de requêtes pour la liste des groupes et les tickets
 - Transmission de Set < Groupe >  et de Ticket


### 3.3. IHM et Controleur de l'application client

Diagramme de classe :
![Diagramme de classe](class_diagram_vue.png  "Diagramme de classe")

Maquettes de l'IHM :
Ecran de connexion :
![](maquettes/connexion.png)
Ouverture de l'application :
![](maquettes/opening_screen.png)
Création d'un nouveau ticket :
![](maquettes/new_ticket.png)
Après la création du ticket :
![](maquettes/ticket_added.png)
Déroulement du groupe "INFO 3A Groupe 1" :
![](maquettes/tickets_groupe.png)
Ouverture d'un ticket contenant plusieurs messages :
![](maquettes/conversation_groupe.png)
Ajout d'un message sur ce ticket :
![](maquettes/conversation_groupe_new.png)
Détails d'un message :
![](maquettes/liste_lus.png)

### 3.4. IHM et Controleur de l'interface administrateur

Diagramme de classe :
![Diagramme de classe](class_diagram_admin.png  "Diagramme de classe")

Maquettes de l'IHM :
Onglet des groupes :
![](maquettes/liste_groupes.png)
Onglet des utilisateurs :
![](maquettes/liste_users.png)
Détails d'un groupe :
![](maquettes/infos_groupe.png)
Détails d'un utilisateur :
![](maquettes/infos_user.png)

## 4. Calendrier

**Semaine 48**

 - Définition modèle de données
 - Définition com réseau
 - Définition base de donnée
 - Définition application client

**Semaine 49**

 - Dossier de conception complet
 - Implémentation com réseau générique et spécifique simple

**Semaine 50**

 - Fermeture du dépôt : dossier de conception (lundi 11/12)
 - Com réseau : reprise sur incident

**Semaine 51**

 - Examens

**Semaine 52**

 - Vacances

**Semaine 1**

 - Vacances

**Semaine 2**

 - Relectures et fioritures

**Semaine 3**

 - Fermeture du dépot : code source (lundi 15/01)
 - Soutenance (mercredi 17/01)


### Planning

J'ai surtout rempli ma partie, et j'ai mis au pif pour vous...

![Planning](planning.png  "Planning")

