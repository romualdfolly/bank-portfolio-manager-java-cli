# Bank Portfolio Manager — Java SE

Mini-application de gestion bancaire développée en **Java SE**, avec **JDBC** et **SQLite**.

## Fonctionnalités

- Création et consultation des clients
- Ouverture et consultation des comptes bancaires
- Dépôts et retraits
- Virements entre comptes
- Gestion d'une limite de découvert autorisé
- Historique des transactions
- Persistance des données avec SQLite
- Gestion des erreurs et transactions SQL avec `commit()` et `rollback()`

## Technologies utilisées

- Java 21
- JDBC
- SQLite
- Programmation orientée objet (POO)

## Architecture

Le projet est organisé en plusieurs couches :

- **Models** : représentation des clients, comptes et transactions, avec les règles métier.
- **Repositories** : accès aux données avec JDBC.
- **Services** : coordination des opérations bancaires.
- **Database** : connexion SQLite et initialisation du schéma.

## Exécution

Le projet nécessite Java 21 et le driver SQLite JDBC.

Depuis la racine du projet :

```bash
javac -cp ".:lib/*" Main.java && java -cp ".:lib/*" Main
```

Ces commandes sont prévues pour Linux.

## Objectif

Ce projet a été réalisé pour mettre en pratique les concepts fondamentaux de Java, la programmation orientée objet, la gestion des bases de données avec JDBC et les transactions SQL.

Il constitue également une base d'apprentissage pour une future version développée avec Spring Boot.
