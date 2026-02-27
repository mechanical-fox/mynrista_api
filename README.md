

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

Version windows (powershell):

```sh
$Env:MYNRISTA_EMAIL_PASSWORD="password"
```

Vous pouvez ensuite lancer l'API en http en utilisant une base de donnée localhost avec le profil defaut. 
Une base de donnée devra tourner sur votre ordinateur en port 5432, ou bien le programme s'arretera avec une
erreur de connexion.

```sh
mvn spring-boot:run
```

Attention, sous Linux vous devrez démarrer l'API avec le mode sudo à cause de l'utilisation des ports 80, et 443. 
Ces ports sont actuellement utilisés, car l'API va générer des pages internet. Voir la documentation de l'endpoint 
**/check-mail/{token}**

```sh
sudo mvn spring-boot:run
```


Vous pouvez ensuite vérifier que l'API fonctionne en vous connectant au swagger.

http://localhost/swagger-ui/index.html  



# Tests unitaires

Vous pouvez lancer les tests unitaire, puis vérifier le taux de coverage avec la commande suivante. La commande verify
est configuré pour échouer si le taux de couverture de code est inférieur à 70%

```sh
mvn verify
```

Après les tests, un rapport html avec la couverture de test sera alors crée à l'emplacement suivant

**target/site/jacoco/index.html**


# Déploiement


## Etape 1: Initialiser la base de donnée   

Créez la structure de la base de donnée avec le script suivant. Celui ci créera les tables, et index
nécessaires.

[script/init.sql](./script/init.sql)


## Etape 2: Modifier le certificat SSL  

Actuellement le certificat SSL utilisé à keystore/cert.p12 est un certificat auto-signé. Ce qui est utilisé
en développement. Mais cela a le soucis de faire afficher des messages d'erreurs en navigateur client, et de
forcer l'utilisateur à accepter le risque de sécurité.

Donc avant de déployer, il va falloir remplacer keystore/cert.p12 par un certificat valide. Pour les 
propriétés à utiliser pour le certificat voir le fichier suivant
     
[src/main/resources/application.yml](./src/main/resources/application.yml)  



## Etape 3: Création de l'image docker   

Une fois le certificat SSL mis à jour, vous pouvez ensuite construire l'image docker avec la commande suivante.
Faites attention à modifier le numéro de version, selon la version de l'application.    


```sh
docker build -t mynrista_1_0  .
```

## Etape 4: Execution de l'image docker   


Une fois l'image docker crée, vous pouvez maintenant la démarrer avec la commande suivante. Faites attention à changer les
mots de passe pour MYNRISTA_EMAIL_PASSWORD, et DATABASE_PASSWORD. Et à ne pas laisser ceux-ci à "password".

MYNRISTA_EMAIL_PASSWORD : Mot de passe de contact@mynrista.fr qui est utilisé par l'API afin d'envoyer des emails
DATABASE_PASSWORD : Mot de passe de la base de donnée


```sh
docker run -d --name container_mynrista_1_0  -p 443:443 -e DATABASE_PASSWORD=password -e MYNRISTA_EMAIL_PASSWORD=password  mynrista_1_0
```

Vérifiez alors que vous puissez vous connecter au swagger en production.

Pour un déploiement vers registration-mynrista.fr comme actuellement, l'url du swagger est donc

https://registration-mynrista.fr/swagger-ui/index.html    

