# NexaBank

## Description
NexaBank est un système bancaire en ligne de commande (console) développé en Java. Il permet de gérer les comptes bancaires, d'effectuer des transactions financières et de gérer les profils clients grâce à deux rôles principaux : **Client** et **Gestionnaire**.

## Fonctionnalités

### Espace Client
* **Consultation de solde** : Voir le solde des différents comptes possédés (Courant, Épargne).
* **Dépôts et Retraits** : Ajouter ou retirer des fonds de ses propres comptes.
* **Virements** : Transférer de l'argent entre deux de ses propres comptes.
* **Relevé bancaire** : Consulter l'historique de ses transactions.

### Espace Gestionnaire
* **Création de compte** : Assigner de nouveaux comptes (Courant ou Épargne) à un client.
* **Clôture de compte** : Supprimer un compte client existant.
* **Gestion des informations** : Modifier les informations personnelles des clients (Nom, Prénom, Email).
* **Consultation globale** : Accéder aux relevés bancaires des clients.

## Structure du projet

* `main/` : Contient le point d'entrée principal de l'application (`Main.java`).
* `model/` : Entités du domaine (ex: `Client`, `Gestionner`, `Compte`, `Transaction`).
* `services/` : Logique métier et gestion de l'application (`AuthService`, `ClientService`, `GestionnaireService`).
* `Exceptions/` : Exceptions personnalisées (`MontantInvalideException`, `SoldeInsuffisantException`, `JournalisationException`).
* `utils/` : Classes utilitaires pour le fonctionnement de l'application.

## Comptes de Démonstration

Lors du lancement de l'application, des comptes sont pré-chargés pour faciliter vos tests :

**Profil Client**
* **Email** : `client@email.com`
* **Mot de passe** : `1234`

**Profil Gestionnaire**
* **Email** : `admin@email.com`
* **Mot de passe** : `admin123`

## Installation et Lancement

1. Clonez ou téléchargez le dépôt du projet.
2. Assurez-vous d'avoir Java (JDK) installé sur votre machine.
3. Compilez les fichiers du projet.
4. Exécutez la classe principale `Main` située dans le dossier `main`.
