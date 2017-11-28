# Package *modele*

Le modèle de données est dans le package *modele*.

### Types

**Types énumérés :**

 - **StatutDeLecture** : lu, non lu, etc.
 
**Interfaces :**

 - **Identifiable** : Objets possédant un identifiant unique 
 (typiquement, celui utilisé par mysql).
 - **PotentiellementLacunaire** : Objets pouvant être instanciés avec tout ou partie de leurs données. Ces dernières peuvent être insérées a posteriori.

**Classes modélisant les données :**

 - **Utilisateur**
 - **Groupe**
 - **Ticket**
 - **Message**

Ces quatre classes implémentent Serializable, Comparable, Identifiable.
Seule Ticket est PotentiellementLacunaire.


### Diagramme de classe

![Diagramme de classe](../../doc/class_diagram_modele.png) 
