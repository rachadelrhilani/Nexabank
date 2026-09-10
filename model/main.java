package model;

import Exceptions.JournalisationException;
import Exceptions.MontantInvalideException;
import Exceptions.SoldeInsuffisantException;
import services.ClientService;
import services.GestionnaireService;

import java.util.Scanner;

class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ClientService clientService = new ClientService();
    private static GestionnaireService gestionnaireService = new GestionnaireService();

    private static Client client = new Client("Benali", "Youssef", "youssef.benali@email.ma", 101);
    private static int sequenceIdTransaction = 1;

    public static void main(String[] args) {
        gestionnaireService.creerCompte(client, new Courant(1001.0, 5000.0f));
        gestionnaireService.creerCompte(client, new Epargne(1002.0, 2000.0f, 2.5f));

        int choix = -1;
        while (choix != 0) {
            System.out.println("\n==========================================");
            System.out.println("       SYSTÈME DE GESTION BANCAIRE        ");
            System.out.println("==========================================");
            System.out.println("1. Espace Client");
            System.out.println("2. Espace Gestionnaire");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> menuClient();
                case 2 -> menuGestionnaire();
                case 0 -> System.out.println("Au revoir !");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void menuClient() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- ESPACE CLIENT ---");
            System.out.println("1. Consulter mes soldes");
            System.out.println("2. Dépôt");
            System.out.println("3. Retrait");
            System.out.println("4. Virement");
            System.out.println("5. Relevé bancaire");
            System.out.println("0. Retour");
            System.out.print("Option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> consulterSoldes();
                case 2 -> faireDepot();
                case 3 -> faireRetrait();
                case 4 -> faireVirement();
                case 5 -> gestionnaireService.consulterReleveClient(client);
                case 0 -> System.out.println("Retour...");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void menuGestionnaire() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- ESPACE GESTIONNAIRE ---");
            System.out.println("1. Créer un compte");
            System.out.println("2. Clôturer un compte");
            System.out.println("3. Modifier infos client");
            System.out.println("4. Consulter relevé client");
            System.out.println("0. Retour");
            System.out.print("Option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> {
                    System.out.print("Numéro du compte : ");
                    double num = scanner.nextDouble();
                    System.out.print("Solde initial : ");
                    float solde = scanner.nextFloat();
                    gestionnaireService.creerCompte(client, new Courant(num, solde));
                }
                case 2 -> {
                    System.out.print("Numéro du compte à clôturer : ");
                    double num = scanner.nextDouble();
                    gestionnaireService.cloturerCompte(client, num);
                }
                case 3 -> {
                    scanner.nextLine();
                    System.out.print("Nouveau nom : ");
                    String nom = scanner.nextLine();
                    System.out.print("Nouveau prénom : ");
                    String prenom = scanner.nextLine();
                    System.out.print("Nouvel email : ");
                    String email = scanner.nextLine();
                    gestionnaireService.modifierInfoClient(client, nom, prenom, email);
                }
                case 4 -> gestionnaireService.consulterReleveClient(client);
                case 0 -> System.out.println("Retour...");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void consulterSoldes() {
        for (Compte c : client.getComptes().values()) {
            System.out.println("Compte N°" + (int) c.getNumeroCompte() + " | Solde : " + c.consulterSolde() + " MAD");
        }
    }

    private static void faireDepot() {
        Compte c = selectionnerCompte();
        if (c == null) return;
        System.out.print("Montant : ");
        int m = lireEntier();
        try {
            clientService.effectuerDepot(c, m, sequenceIdTransaction++);
            System.out.println("Dépôt réussi. Solde : " + c.consulterSolde() + " MAD");
        } catch (MontantInvalideException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void faireRetrait() {
        Compte c = selectionnerCompte();
        if (c == null) return;
        System.out.print("Montant : ");
        int m = lireEntier();
        try {
            clientService.effectuerRetrait(c, m, sequenceIdTransaction++);
            System.out.println("Retrait réussi. Solde : " + c.consulterSolde() + " MAD");
        } catch (MontantInvalideException | SoldeInsuffisantException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void faireVirement() {
        System.out.println("Sélection compte source :");
        Compte src = selectionnerCompte();
        System.out.println("Sélection compte destination :");
        Compte dest = selectionnerCompte();

        if (src == null || dest == null || src == dest) {
            System.out.println("Comptes invalides.");
            return;
        }

        System.out.print("Montant : ");
        int m = lireEntier();
        try {
            clientService.effectuerVirement(src, dest, m, sequenceIdTransaction++);
            System.out.println("Virement réussi.");
        } catch (MontantInvalideException | SoldeInsuffisantException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static Compte selectionnerCompte() {
        consulterSoldes();
        System.out.print("Entrez le numéro du compte : ");
        double num = scanner.nextDouble();
        return client.getComptes().get(num);
    }

    private static int lireEntier() {
        while (!scanner.hasNextInt()) {
            System.out.print("Saisie invalide, entrez un nombre : ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}