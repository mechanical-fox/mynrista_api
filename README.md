

# TO DO

PRIS DE LA PARTIE IHM

-> Fait: Passer en port 8082 (new_erdline utilise 8081) sur API + sur configuration IHM
-> Fait: En API modifier pour ne PLUS avoir vérification Link + l'autre truc + plus de vérification en API

-> REGARDER EN MYNRISTA_IHM

-> Supprimer ce to do


# Projet


Ce projet contient le code de la partie backend / serveur, du site Mynrista. Le site mynrista permet
d'ajouter, et de consulter les informations sur différents visual novel. Tels que le sommaire du
visual novel, sa date de parution, son pourcentage d'évaluation positives (Steam), ...

Les fonctionnalités actuelles sont les suivantes:
- Création de compte
- Création de Pages de présentation de Visual Novel
- Affichage des Visual Novels par top "Nouveautés et Tendances"
- Affichage des Visual Novels par top "Meilleurs Evaluations"
- Affichage des Visual Novels par tags / catégories


# Utilisation du mode HTTPS

Cette API fonctionne actuellement en HTTP, et non en HTTPS, y compris en production. Cela est une modification
effectuée, car après test un portfolio sous forme de site internet rencontre assez peu de succès. Et un portfolio
Github a donc été préféré.

Vous pouvez néanmoins activer le mode HTPPS en mettant le paramètre server.ssl.enabled à true au lieu de false. De
plus, actuellement le certificat SSL utilisé avec le protocole HTTPS, et stocké à keystore/cert.p12 est un certificat
auto-signé. Ce qui est utilisé en développement. Mais cela a le soucis de faire afficher des messages d'erreurs en
navigateur client, et de forcer l'utilisateur à accepter le risque de sécurité.

Donc si vous souhaitez effectuer un déploiement en HTTPS, et non en HTTP, ne pas oublier de remplacer keystore/cert.p12
par un certificat valide. Pour les propriétés à utiliser pour le certificat voir le fichier suivant

[src/main/resources/application.yml](./src/main/resources/application.yml)   


# Execution   

Vous pouvez executer l'API en http en utilisant une base de donnée localhost avec le profil default. Une base
de donnée devra tourner sur votre ordinateur en port 5433, ou bien le programme s'arretera avec une erreur
de connexion.


```sh
mvn spring-boot:run
```

Vous pouvez ensuite vérifier que l'API fonctionne en vous connectant au swagger.

http://localhost:8082/swagger-ui/index.html  



# Tests unitaires

Vous pouvez lancer les tests unitaire, puis vérifier le taux de coverage avec la commande suivante. La commande verify
est configuré pour échouer si le taux de couverture de code est inférieur à 70%

```sh
mvn verify
```

Après les tests, un rapport html avec la couverture de test sera alors crée à l'emplacement suivant

**target/site/jacoco/index.html**


# Déploiement


## Etape 1: Création de la base de donnée

Déployez la base de donnée Mynrista, via le projet commun_database qui est également disponible sur mon Github. 
Faites attention que la base de donnée devra être déployée en port 5433, avec l'utilisateur tora, et le mot de
passe password. Mot de passe que vous pourrez changer après.


## Etape 2: Création de l'image docker   

Une fois le certificat SSL mis à jour, vous pouvez ensuite construire l'image docker avec la commande suivante.   


```sh
docker build -t app_mynrista  .
```

## Etape 3: Execution de l'image docker   


Une fois l'image docker crée, vous pouvez maintenant la démarrer avec la commande suivante. Faites attention à
changer le mot de passe pour DATABASE_PASSWORD. Et à ne pas laisser celui-ci à "password". Si nécessaire, le
projet commun_database utilisé pour créer la base de donnée, mentionne comment changer le mot de passe de celle-ci.

DATABASE_PASSWORD : mot de passe utilisé par la base de donnée.


```sh
docker run -d --name capp_mynrista  -p 8082:8082 -e DATABASE_PASSWORD=password  app_mynrista
```

Pour Linux la commande pour docker sera la suivante.

```sh
docker run -d --name capp_mynrista  -p 8082:8082 -e DATABASE_PASSWORD=password  app_mynrista --add-host host.docker.internal:host-gateway
```

Vérifiez alors que vous puissez vous connecter au swagger en production. Actuellement, le swagger de production est
configuré pour démarrer en localhost. Vous pouvez donc vous connecter au swagger via la page internet suivante.

http://localhost:8082/swagger-ui/index.html    

