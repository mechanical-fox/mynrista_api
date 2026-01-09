
# TO DO

Etape 1: Fait --> Pouvoir ajouter un utilisateur + mot de passe en HASH
Etape 2: TO DO --> Gérer l'envoie en mail + click lien mail et alors vérifie

2.1 -> Fait: Changer le mail pour le mail de mynrista en configuration
2.2 -> Fait: Gestion du mot de passe sender email en caché + indiquer en README comment faire
2.3 -> Fait: Faire generation du lien pour clic + on indique uuid verification en base

2.4 -> Fait: En API faire par l'API renvoit de page html lors du clic sur le lien + faire VALIDER USER en base
         + gérer les cas d'erreurs

2.5 -> Fait: AI-je bien pensé à valider l'email en base de donnée ?

2.6 -> NOW --> En IHM indiquer faire ecran pour indiquer que mail à vérifier (demande d'inscription)


Etape 3 : Faire script sql avec bonne tables + penser à ajouter les index nécessaires en base localhost
Etape 4: Ajouter une url de connexion avec token
Etape 5: Script sql ajout table pour token + doit avoir la date aussi
Etape 6: Script sql ajout index pour les tokens + penser à ajouter les index nécessaires en base localhost

- Indiquer l'adresse du bon serveur PRODUCTION
- Pour le reste voir en partie IHM le projet
- Ne pas oublier de faire des tests unitaires + avec % de couverture de test


# Projet

Ce projet contiendra le code de la partie backend / serveur, du site Mynrista. Le site est en cours de
création. Il est prévue d'utiliser la technologie Vue pour la partie graphique.

Les fonctionnalités prévues pour le site internet sont les suivantes:
- Pouvoir créer des comptes
- Pouvoir consulter la présentation de plusieurs Visual Novels
- Affichage des Visual Novels par date les plus récents
- Affichage de tout les Visual Novels

Une fois connecté l'on devra pourvoir également:
- Créer une page de Présentation d'un Visual Novel
- Modifier la présentation d'une Page de Visual Novel


# Changement de Certificat SSL

Actuellement le certificat SSL utilisé à keystore/cert.p12 est un certificat auto-signé. Ce qui est utilisé
en développement. Mais cela a le soucis de faire afficher des messages d'erreurs en navigateur client, et de
forcer l'utilisateur à accepter le risque de sécurité.

Donc ne pas oublier lors du déploiement de remplacer keystore/cert.p12 par un certificat valide. Pour les
propriétés à utiliser pour le certificat voir le fichier suivant
     
[src/main/resources/application.yml](./src/main/resources/application.yml)      


# Execution   

Avant de lancer l'application, veuillez définir en variable d'environnement le mot de passe de l'email utilisé 
par l'API Mynrista. Sinon au démarrage le programme affichera une erreur, en l'absence de la déclaration
de la variable d'environment adéquate.

Version windows(powershell):

```sh
$Env:MYNRISTA_EMAIL_PASSWORD="password"
```

Vous pouvez ensuite lancer l'API en http en utilisant une base de donnée localhost avec le profil defaut. 
Une base de donnée devra tourner sur votre ordinateur en port 5432, ou bien le programme s'arretera avec une
erreur de connexion.

```sh
mvn spring-boot:run
```


Vous pouvez ensuite vérifier que l'API fonctionne en vous connectant au swagger.

http://localhost/swagger-ui/index.html  