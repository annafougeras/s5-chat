# Package *communication*

Contrôleur permettant la communication authentifiée d'objets sur TCP. 

La sécurité des données transmises n'est pas garantie. Sur des données
potentiellement sensibles, elle doit être assurée par un traitement préalable
(encodage / décodage).

### Interfaces

Deux interfaces sont implémentés par des contrôleurs : 

 - ControleurComClient < Message > 
 - ControleurComServeur < Message >

Deux interfaces sont implémentées par les utilisateurs (observateurs) de ces contrôleurs. 
Leurs méthodes permettent d'informer l'utilisateur d'un message reçu.

 - ObservateurComClient < Message > 
 - ObservateurComServeur < Message > 

