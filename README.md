# Penacony - Application Web avec Spring Boot

Bienvenue dans **Penacony**, une application web développée avec Spring Boot et Thymeleaf. Cette application est conçue pour être simple et intuitive, tout en utilisant des technologies modernes pour offrir une expérience fluide.

## Fonctionnalités principales

- **Page d'accueil** : Une page d'accueil conviviale qui affiche un message de bienvenue.
- **Gestion des données** : L'application est connectée à une base de données MySQL pour stocker et gérer des informations.
- **Interface utilisateur dynamique** : Utilisation de Thymeleaf pour créer des pages web dynamiques et interactives.

## Comment ça fonctionne ?

1. **Page d'accueil** :

   - Lorsque vous accédez à l'application via votre navigateur, vous êtes redirigé vers la page d'accueil.
   - Cette page affiche un message de bienvenue personnalisé.
2. **Connexion à la base de données** :

   - L'application utilise une base de données MySQL pour stocker les données.
   - Les informations sont gérées via des entités Java (modèles) et des contrôleurs REST.
3. **Technologies utilisées** :

   - **Spring Boot** : Le cœur de l'application, qui facilite la création d'applications web robustes.
   - **Thymeleaf** : Un moteur de templates pour générer des pages HTML dynamiques.
   - **MySQL** : Une base de données relationnelle pour stocker les données.
   - **Spring-Security** : Une bibliothèque qui permet la sécurisation des accès de l'application.

## Arborescence de fichiers

```txt
src
 └ main
   ├ java -> le code Java
   │ └ com
   │   └ penacony
   │     └ hotel
   │       ├ controller -> les définitions des Routes Spring Boot
   │       ├ model -> les tables de la db au format Classes
   │       ├ repository -> contient des méthodes d'accès aux données de la db, pour chaque table
   │       └ PenaconyApplication.java -> le main
   └ resources -> les autres fichiers
     ├ static -> équivalent à un dossier "src"
     │ ├ styles -> css
     │ └ img, scripts, etc...
     ├ templates -> les fichiers html
     │ └ fragments - les modules (navbar, footer, header, ...) qui sont présent plusieurs fois dans le site
     └ application.properties -> fichier système
```

## Comment démarrer ?

### Prérequis

- **Java 17** : Assurez-vous que Java 17 est installé sur votre machine.
- **MySQL** : Installez MySQL et créez une base de données nommée `penacony` (pas besoin de créer les tables, l'app les crééra automatiquement).
- **Gradle** : L'application utilise Gradle pour gérer les dépendances.

#### SQL

executer successivement ces 3 actions :

1. `penacony_create.sql` (importer dans Laragon -> HeidiSQL)
2. initialiser l'app
3. `penacony_user.sql` (importer dans Laragon -> HeidiSQL)

#### Java

1. ouvrir un invite de commande à la racine de ce dossier
2. executer grad
3. executer `gradlew.bat bootRun`
4. l'application est accessible à l'url [http://localhost:8080/](http://localhost:8080/), depuis n'importe quel navigateur Web
