package model;
import Exceptions.*;
import java.util.Scanner;

class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Gestionner gestionnaire = new Gestionner("El Amrani", "Karim", "karim.gestion@banque.ma", 1);
    private static Client client = new Client("Benali", "Youssef", "youssef.benali@email.ma", 101);
    private static int sequenceIdTransaction = 1;

    public static void main(String[] args) {
        gestionnaire.creerCompte(client, new Courant(1001.0, 5000.0f));
        gestionnaire.creerCompte(client, new Epargne(1002.0, 2000.0f, 2.5f));

        int choix = -1;
        while (choix != 0) {
            System.out.println("\n==========================================");
            System.out.println("       SYSTÈME DE GESTION BANCAIRE        ");
            System.out.println("==========================================");
            System.out.println("1. Espace Client");
            System.out.println("2. Espace Gestionnaire");
            System.out.println("0. Quitter l'application");
            System.out.print("Choisissez une option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> menuClient();
                case 2 -> menuGestionnaire();
                case 0 -> System.out.println("Merci d'avoir utilisé notre application. Au revoir !");
                default -> System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    // ==========================================
    // MENU ET FONCTIONNALITÉS CLIENT
    // ==========================================
    private static void menuClient() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- ESPACE CLIENT ---");
            System.out.println("1. Consulter le solde de mes comptes");
            System.out.println("2. Effectuer un dépôt");
            System.out.println("3. Effectuer un retrait");
            System.out.println("4. Realiser un virement");
            System.out.println("5. Afficher mes relevés bancaires");
            System.out.println("0. Retour au menu principal");
            System.out.print("Option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> consulterSoldesClient();
                case 2 -> faireDepot();
                case 3 -> faireRetrait();
                case 4 -> faireVirement();
                case 5 -> gestionnaire.consulterReleveClient(client);
                case 0 -> System.out.println("Retour au menu principal...");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void consulterSoldesClient() {
        System.out.println("\n--- VOS COMPTES ---");
        if (client.getComptes().isEmpty()) {
            System.out.println("Vous ne possédez aucun compte.");
            return;
        }
        for (Compte c : client.getComptes().values()) {
            System.out.println("Compte N°" + (int) c.getNumeroCompte() + " | Solde : " + c.consulterSolde() + " MAD");
        }
    }

    private static void faireDepot() {
        Compte compte = selectionnerCompte();
        if (compte == null) return;

        System.out.print("Saisissez le montant à déposer : ");
        int montant = lireEntier();

        try {
            compte.depotArgent(montant);
            int numCompteInt = (int) compte.getNumeroCompte();
            
            Transaction t = new Transaction(sequenceIdTransaction++, TypeTransaction.DEPOT, montant, numCompteInt, numCompteInt);
            compte.ajouterTransaction(t);
            t.enregistreFichier(numCompteInt);

            System.out.println("Dépôt réussi. Nouveau solde : " + compte.consulterSolde() + " MAD");
        } catch (MontantInvalideException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void faireRetrait() {
        Compte compte = selectionnerCompte();
        if (compte == null) return;

        System.out.print("Saisissez le montant à retirer : ");
        int montant = lireEntier();

        try {
            compte.retraitArgent(montant);
            int numCompteInt = (int) compte.getNumeroCompte();

            Transaction t = new Transaction(sequenceIdTransaction++, TypeTransaction.RETRAIT, montant, numCompteInt, numCompteInt);
            compte.ajouterTransaction(t);
            t.enregistreFichier(numCompteInt);

            System.out.println("Retrait réussi. Nouveau solde : " + compte.consulterSolde() + " MAD");
        } catch (MontantInvalideException | SoldeInsuffisantException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void faireVirement() {
        System.out.println("\n--- SÉLECTION DU COMPTE SOURCE ---");
        Compte source = selectionnerCompte();
        if (source == null) return;

        System.out.println("\n--- SÉLECTION DU COMPTE DESTINATION ---");
        Compte destination = selectionnerCompte();
        if (destination == null) return;

        if (source.getNumeroCompte() == destination.getNumeroCompte()) {
            System.out.println("Erreur : Impossible de faire un virement sur le même compte.");
            return;
        }

        System.out.print("Saisissez le montant du virement : ");
        int montant = lireEntier();

        try {
            Transaction.Virement(source, destination, montant, sequenceIdTransaction++);
            System.out.println("Virement de " + montant + " MAD effectué avec succès !");
        } catch (MontantInvalideException | SoldeInsuffisantException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // ==========================================
    // MENU ET FONCTIONNALITÉS GESTIONNAIRE
    // ==========================================
    private static void menuGestionnaire() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- ESPACE GESTIONNAIRE ---");
            System.out.println("1. Créer un compte pour le client");
            System.out.println("2. Clôturer un compte client");
            System.out.println("3. Modifier les informations du client");
            System.out.println("4. Consulter le relevé bancaire complet du client");
            System.out.println("0. Retour au menu principal");
            System.out.print("Option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> ajouterCompteGestionnaire();
                case 2 -> cloturerCompteGestionnaire();
                case 3 -> modifierInfosClient();
                case 4 -> gestionnaire.consulterReleveClient(client);
                case 0 -> System.out.println("Retour au menu principal...");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void ajouterCompteGestionnaire() {
        System.out.print("Saisir le numéro du nouveau compte : ");
        double num = scanner.nextDouble();
        System.out.print("Saisir le solde initial : ");
        float solde = scanner.nextFloat();

        System.out.println("Type de compte : 1. Courant | 2. Épargne");
        int type = lireEntier();

        Compte nouveauCompte = (type == 2) 
                ? new Epargne(num, solde, 2.0f) 
                : new Courant(num, solde);

        gestionnaire.creerCompte(client, nouveauCompte);
    }

    private static void cloturerCompteGestionnaire() {
        System.out.print("Saisir le numéro du compte à clôturer : ");
        double num = scanner.nextDouble();
        gestionnaire.cloturerCompte(client, num);
    }

    private static void modifierInfosClient() {
        scanner.nextLine(); // Consommer le retour à la ligne
        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();
        System.out.print("Nouveau prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Nouvel email : ");
        String email = scanner.nextLine();

        gestionnaire.modifierInfoClient(client, nom, prenom, email);
    }

    // ==========================================
    // MÉRTHODES UTILITAIRES DE L'INTERFACE
    // ==========================================
    private static Compte selectionnerCompte() {
        consulterSoldesClient();
        if (client.getComptes().isEmpty()) return null;

        System.out.print("Entrez le numéro du compte à cibler : ");
        double num = scanner.nextDouble();

        Compte compte = client.getComptes().get(num);
        if (compte == null) {
            System.out.println("Compte introuvable.");
        }
        return compte;
    }

    private static int lireEntier() {
        while (!scanner.hasNextInt()) {
            System.out.print("Saisie invalide. Entrez un nombre entier : ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}