# Dossier src/

Code source des packages java.

### Package *modele*

Le modèle de données est dans le package *modele*.

Types énumérés : 

 - **StatutDeLecture** : lu, non lu, etc.

Interfaces :

 - **Identifiable** : Objets possédant un identifiant unique 
 (typiquement, celui utilisé par mysql).
 - **PotentiellementLacunaire** : Objets pouvant être instanciés avec tout ou 
 partie de leurs données. Ces dernières peuvent être insérées a posteriori.

Classes modélisant les données : 

 - **Utilisateur**
 - **Groupe**
 - **Ticket**
 - **Message**
