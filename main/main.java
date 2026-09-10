
import Exceptions.JournalisationException;
import Exceptions.MontantInvalideException;
import Exceptions.SoldeInsuffisantException;
import model.*;
import services.ClientService;
import services.GestionnaireService;

import java.util.Scanner;

class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final ClientService clientService = new ClientService();
    private static final GestionnaireService gestionnaireService = new GestionnaireService();

    private static final Client client = new Client("Benali", "Youssef", "youssef.benali@email.ma", 101);
    private static int sequenceIdTransaction = 1;

    public static void main(String[] args) {
        // Pré-chargement des comptes de démonstration (Compte Courant & Compte Épargne)
        gestionnaireService.creerCompte(client, new Courant(1001.0, 5000.0f, 1000.0f));
        gestionnaireService.creerCompte(client, new Epargne(1002.0, 2000.0f, 2.5f));

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
                case 0 -> System.out.println("Fermeture de l'application. Au revoir !");
                default -> System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
        scanner.close();
    }

    // ==========================================
    // ESPACE CLIENT
    // ==========================================
    private static void menuClient() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- ESPACE CLIENT ---");
            System.out.println("1. Consulter le solde de mes comptes");
            System.out.println("2. Effectuer un dépôt");
            System.out.println("3. Effectuer un retrait");
            System.out.println("4. Réaliser un virement entre comptes");
            System.out.println("5. Consulter mes relevés bancaires");
            System.out.println("0. Retour au menu principal");
            System.out.print("Option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> afficherSoldesClient();
                case 2 -> faireDepot();
                case 3 -> faireRetrait();
                case 4 -> faireVirement();
                case 5 -> gestionnaireService.consulterReleveClient(client);
                case 0 -> System.out.println("Retour au menu principal...");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void afficherSoldesClient() {
        System.out.println("\n--- VOS COMPTES ---");
        if (client.getComptes().isEmpty()) {
            System.out.println("Vous ne possédez aucun compte.");
            return;
        }
        for (Compte c : client.getComptes().values()) {
            String type = (c instanceof Courant) ? "Courant" : "Épargne";
            System.out.println("Compte " + type + " N°" + (int) c.getNumeroCompte() + " | Solde : " + c.consulterSolde() + " MAD");
        }
    }

    private static void faireDepot() {
        Compte compte = selectionnerCompte();
        if (compte == null) return;

        System.out.print("Saisissez le montant à déposer : ");
        float montant = lireFloat();

        try {
            clientService.effectuerDepot(compte, montant, sequenceIdTransaction++);
            System.out.println("Dépôt réussi. Nouveau solde : " + compte.getSolde() + " MAD");
        } catch (MontantInvalideException | JournalisationException e) {
            System.out.println("Erreur Dépôt : " + e.getMessage());
        }
    }

    private static void faireRetrait() {
        Compte compte = selectionnerCompte();
        if (compte == null) return;

        System.out.print("Saisissez le montant à retirer : ");
        float montant = lireFloat();

        try {
            clientService.effectuerRetrait(compte, montant, sequenceIdTransaction++);
            System.out.println("Retrait réussi. Nouveau solde : " + compte.getSolde() + " MAD");
        } catch (MontantInvalideException | SoldeInsuffisantException | JournalisationException e) {
            System.out.println("Erreur Retrait : " + e.getMessage());
        }
    }

    private static void faireVirement() {
        System.out.println("\n--- SÉLECTION DU COMPTE SOURCE ---");
        Compte source = selectionnerCompte();
        if (source == null) return;

        System.out.println("\n--- SÉLECTION DU COMPTE DESTINATION ---");
        Compte destination = selectionnerCompte();
        if (destination == null) return;

        System.out.print("Saisissez le montant du virement : ");
        float montant = lireFloat();

        try {
            clientService.effectuerVirement(source, destination, montant, sequenceIdTransaction++);
            System.out.println("Virement de " + montant + " MAD effectué avec succès !");
        } catch (MontantInvalideException | SoldeInsuffisantException | JournalisationException e) {
            System.out.println("Erreur Virement : " + e.getMessage());
        }
    }

    // ==========================================
    // ESPACE GESTIONNAIRE
    // ==========================================
    private static void menuGestionnaire() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- ESPACE GESTIONNAIRE ---");
            System.out.println("1. Créer un nouveau compte pour le client");
            System.out.println("2. Clôturer un compte client");
            System.out.println("3. Modifier les informations du client");
            System.out.println("4. Consulter le relevé complet du client");
            System.out.println("0. Retour au menu principal");
            System.out.print("Option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> ajouterCompte();
                case 2 -> cloturerCompte();
                case 3 -> modifierInfosClient();
                case 4 -> gestionnaireService.consulterReleveClient(client);
                case 0 -> System.out.println("Retour au menu principal...");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void ajouterCompte() {
        System.out.print("Numéro du nouveau compte : ");
        double num = scanner.nextDouble();
        System.out.print("Solde initial : ");
        float solde = scanner.nextFloat();

        System.out.println("Type de compte : 1. Courant | 2. Épargne");
        int type = lireEntier();

        Compte nouveauCompte = (type == 2) 
                ? new Epargne(num, solde, 2.5f) 
                : new Courant(num, solde, 500.0f);

        gestionnaireService.creerCompte(client, nouveauCompte);
    }

    private static void cloturerCompte() {
        System.out.print("Numéro du compte à clôturer : ");
        double num = scanner.nextDouble();
        gestionnaireService.cloturerCompte(client, num);
    }

    private static void modifierInfosClient() {
        scanner.nextLine();
        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();
        System.out.print("Nouveau prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Nouvel email : ");
        String email = scanner.nextLine();

        gestionnaireService.modifierInfoClient(client, nom, prenom, email);
    }

    // ==========================================
    // UTILITAIRES
    // ==========================================
    private static Compte selectionnerCompte() {
        afficherSoldesClient();
        if (client.getComptes().isEmpty()) return null;

        System.out.print("Entrez le numéro du compte à cibler : ");
        double num = scanner.nextDouble();

        Compte compte = client.getComptes().get(num);
        if (compte == null) {
            System.out.println("Erreur : Aucun compte trouvé avec ce numéro.");
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

    private static float lireFloat() {
        while (!scanner.hasNextFloat()) {
            System.out.print("Saisie invalide. Entrez un nombre décimal : ");
            scanner.next();
        }
        return scanner.nextFloat();
    }
}