# Projet CAFHEG

Ce projet vise à développer et tester des services liés à la gestion des allocataires et des allocations. Voici un résumé des solutions apportées pour chaque exercice.

---

## Exercice 1 : Prise en main et refactoring

### Partie 1 – Prise en main
- **Analyse du projet** : Nous avons étudié les classes existantes et leurs interactions, notamment les services, mappers et entités.
- **Lancement de l'application** : L'application a été démarrée via la classe `Application` en mode standalone ou sur un serveur d'applications.
- **Test du RESTController** : Le endpoint `/droits/quel-parent` a été testé avec un outil dédié aux tests d'API REST (Postman). Les paramètres nécessaires ont été identifiés dans la classe `RESTController`.

### Partie 2 – Ajout de tests
- **Harnais de tests** : Une couverture de tests complète (100%) a été réalisée sur la méthode `AllocationService#getParentDroitAllocation` pour comprendre son fonctionnement et identifier ses défauts.

### Partie 3 – TDD & Refactoring
- **Refactoring** : La méthode `AllocationService#getParentDroitAllocation` a été modifiée pour remplacer l'utilisation de `Map<String, Object>` par une classe dédiée. Les tests ont été adaptés pour garantir une couverture de 100%.
- **Contrôle de l'API REST** : Après refactoring, le bon fonctionnement du endpoint `/droits/quel-parent` a été vérifié.

### Partie 4 – Implémentation
- **Modification de la méthode** : La méthode `AllocationService#getParentDroitAllocation` a été réimplémentée en suivant un nouveau schéma, avec une approche TDD. Des paramètres supplémentaires ont été ajoutés pour une implémentation correcte.

---

## Exercice 2 : Gestion des allocataires

### Partie 1 – Suppression d’un allocataire
- **Service de suppression** : Un service a été implémenté pour supprimer un allocataire uniquement s'il ne possède pas de versements. Une vérification préalable est effectuée via le `VersementMapper`.

### Partie 2 – Modification d’un allocataire
- **Service de modification** : Un service a été créé pour modifier le nom et le prénom d’un allocataire. Les contraintes suivantes ont été respectées :
    - Le numéro AVS est immuable.
    - La modification n’est effectuée que si le nom ou le prénom a changé.

### Partie 3 – Exposition des services
- **API REST** : Les services de suppression et de modification ont été exposés via des endpoints REST. Ces endpoints ont été testés avec un outil d’API REST.

---

## Exercice 4 : Gestion des logs

- **Remplacement des `System.out.println`** : Tous les appels à `System.out.println` et `printStackTrace` ont été remplacés par des loggers.
- **Niveaux de logs** :
    - `error` : Utilisé pour les exceptions, avec la cause incluse.
    - `warn` : Utilisé pour les problèmes non critiques.
    - `info` : Utilisé pour les accès aux services.
    - `debug` : Utilisé pour les informations techniques.
    - `trace` : Utilisé pour les détails.
- **Configuration des loggers** :
    - Les logs `error` des packages contenant `ch` sont enregistrés dans `err.log`.
    - Les logs `info` des services sont enregistrés dans `cafheg_{date-jour}.log`.
    - Les logs `debug` sont affichés dans la console.

---

## Exercice 5 : Tests d’intégration

### Partie 1 – Nouveau répertoire pour les tests d’intégration
- Une nouvelle structure de répertoires a été créée pour séparer les tests unitaires et les tests d’intégration :
    - `src/integration-test/java` pour le code des tests d’intégration.
    - `src/integration-test/resources` pour les fichiers de configuration et datasets.

### Partie 2 – Premier test d’intégration
- Une classe `MyTestsIT` a été créée avec un test simple toujours passant (`1 == 1`) pour valider la configuration.

### Partie 3 – Configuration des tests d’intégration
- Les fichiers JAR de DBUnit ont été ajoutés au projet et configurés dans IntelliJ.

### Partie 4 – Rédaction de tests d’intégration
- **Suppression d’un allocataire** :
    - Un fichier XML DBUnit a été créé pour peupler les tables.
    - Un test JUnit 5 a été écrit pour vérifier la suppression d’un allocataire.
- **Modification d’un allocataire** :
    - Un fichier XML DBUnit a été utilisé pour tester la modification d’un allocataire.
- **Résultat** : Deux tests d’intégration (suppression + modification) ont été implémentés avec succès.

---

## Structure du projet
- **Langages** : Java, SQL
- **Frameworks** : Spring Boot, JUnit 5, DBUnit, AssertJ
- **Outils** : IntelliJ IDEA, Postman