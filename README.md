

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

Attention, sous Linux vous devrez démarrer l'API avec le mode sudo à cause de l'utilisation des ports 80, et 443. Ces ports
sont actuellement utilisés, car l'API va générer des pages internet. Voir la documentation de l'endpoint **/check-mail/{token}**

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

