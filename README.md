
# TO DO



- Indiquer l'adresse du bon serveur PRODUCTION de MYNRISTA (et non erdline) + bon PORT aussi
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

Attention, sous Linux, vous devrez démarrer l'API avec le mode sudo. Sinon une suite d'erreur sera déclenchée, car 
seul l'administrateur à le droit d'ouvrir le port 80.

```sh
sudo mvn spring-boot:run
```


Vous pouvez ensuite vérifier que l'API fonctionne en vous connectant au swagger.

http://localhost/swagger-ui/index.html  

**Note:** Il a été choisit d'utiliser pour l'API les ports 80, et 443, comme pour un site internet. Et cela car l'API va
générer des pages internet. Voir l'url "/check-mail/{token}".

