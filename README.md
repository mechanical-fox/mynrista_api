
# TO DO



Etape 4: Fait:  Ajouter une url de connexion avec un token qui doit être donné en réponse
           + NOW je fais "AuthentificationEntity" en base (AuthentificationBody fini) 

-- IMPORTANT: Connection avec mon mail yahoo et le mot passe defaut de Itsuki (Swagger) en base de donnée locale ---

Etape 4 bis: HYPER IMPORTANT ajout à ma synthèse TEST TECHNIQUE + NORMAL les dates java normal + date java sql + peut être
        le code de la classe wrapper pour passer de l'un à l'autre ? Et les méthodes de bases. Non car vraiment, cela est 
        de la base. Hyper utile.

Etape 5: Script sql ajout table pour token + doit avoir la date aussi
Etape 6: Script sql ajout index pour les tokens + penser à ajouter les index nécessaires en base localhost
Etape 7: Faire connexion via interface IHM (Juste afficher pseudo - inutile de proposer déconnection ou photo- la plupart des gens 
          ne crééront même pas de compte)
Etape 8:  Faire Test Unitaires ihm NOW car cela risque d'être compliqué / long à apprendre de comment faire tests unitaires en Vue

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

