# Package *commChatS5*

Contrôleur permettant la communication authentifiée de Ticket et Set < Groupe >  sur TCP


### Interfaces

Deux interfaces sont implémentés par des contrôleurs : 

 - ICtrlComClient 
 - ICtrlComServeur

Deux interfaces sont implémentées par les utilisateurs (observateurs) de ces contrôleurs. 
Leurs méthodes permettent d'informer l'utilisateur d'un message reçu.

 - S5Client 
 - S5Serveur


### Classes concrètes

Les controleurs sont implémentés dans les classes

 - CtrlComClient
 - CtrlComServeur

### Diagramme de classe

Ce diagramme illustre l'utilisation des contrôleurs avec les classes

 - tests.TestS5Client.Client
 - tests.TestS5Serveur.Serveur

![Class Diagram](../../doc/class_diagram_S5com.png) 

Il est complètement illisible, isn't it ?