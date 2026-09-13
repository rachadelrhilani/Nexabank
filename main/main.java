

import Exceptions.JournalisationException;
import Exceptions.MontantInvalideException;
import Exceptions.SoldeInsuffisantException;
import model.*;
import services.*;

import java.util.Scanner;

class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final AuthService authService = new AuthService();
    private static final ClientService clientService = new ClientService();
    private static final GestionnaireService gestionnaireService = new GestionnaireService();

    private static int sequenceIdTransaction = 1;

    public static void main(String[] args) {
        // Pré-chargement des comptes et des utilisateurs
        Client clientDemo = new Client("Benali", "Youssef", "client@email.com", "1234", 101);
        Gestionner gestionnaireDemo = new Gestionner("El Amrani", "Karim", "admin@email.com", "admin123", 1);

        gestionnaireService.creerCompte(clientDemo, new Courant(1001.0, 5000.0f, 1000.0f));
        gestionnaireService.creerCompte(clientDemo, new Epargne(1002.0, 2000.0f, 2.5f));

        authService.inscrireUtilisateur(clientDemo);
        authService.inscrireUtilisateur(gestionnaireDemo);

        boolean applicationActive = true;

        while (applicationActive) {
            System.out.println("\n==========================================");
            System.out.println("       CONNEXION SYSTÈME BANCAIRE         ");
            System.out.println("==========================================");

            Person utilisateurConnecte = null;

            // L'authentification
            while (utilisateurConnecte == null) {
                System.out.print("Email : ");
                String email = scanner.nextLine();
                System.out.print("Mot de passe : ");
                String mdp = scanner.nextLine();

                utilisateurConnecte = authService.authentifier(email, mdp);

                if (utilisateurConnecte == null) {
                    System.out.println("Identifiants incorrects. Veuillez réessayer.\n");
                }
            }

            // Message de bienvenue polymorphe
            utilisateurConnecte.login();

            // redirection automatique
            if ("CLIENT".equals(utilisateurConnecte.getRole())) {
                menuClient((Client) utilisateurConnecte);
            } else if ("GESTIONNAIRE".equals(utilisateurConnecte.getRole())) {
                menuGestionnaire();
            }

            System.out.println("\n1. Se connecter avec un autre compte");
            System.out.println("0. Quitter le système");
            System.out.print("Choix : ");
            if (lireEntier() == 0) {
                applicationActive = false;
            }
        }

        System.out.println("Fermeture de l'application. Au revoir !");
        scanner.close();
    }

    // ==========================================
    // ESPACE CLIENT
    // ==========================================
    private static void menuClient(Client client) {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- BIENVENUE DANS VOTRE ESPACE CLIENT ---");
            System.out.println("1. Consulter le solde de mes comptes");
            System.out.println("2. Effectuer un dépôt");
            System.out.println("3. Effectuer un retrait");
            System.out.println("4. Réaliser un virement entre comptes");
            System.out.println("5. Consulter mon relevé bancaire");
            System.out.println("0. Déconnexion");
            System.out.print("Option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> afficherSoldes(client);
                case 2 -> faireDepot(client);
                case 3 -> faireRetrait(client);
                case 4 -> faireVirement(client);
                case 5 -> gestionnaireService.consulterReleveClient(client);
                case 0 -> System.out.println("Déconnexion en cours...");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void afficherSoldes(Client client) {
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

    private static void faireDepot(Client client) {
        Compte compte = selectionnerCompte(client);
        if (compte == null) return;

        System.out.print("Saisissez le montant à déposer : ");
        float montant = lireFloat();

        try {
            clientService.effectuerDepot(compte, montant, sequenceIdTransaction++);
            System.out.println("Dépôt réussi. Nouveau solde : " + compte.getSolde() + " MAD");
        } catch (MontantInvalideException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void faireRetrait(Client client) {
        Compte compte = selectionnerCompte(client);
        if (compte == null) return;

        System.out.print("Saisissez le montant à retirer : ");
        float montant = lireFloat();

        try {
            clientService.effectuerRetrait(compte, montant, sequenceIdTransaction++);
            System.out.println("Retrait réussi. Nouveau solde : " + compte.getSolde() + " MAD");
        } catch (MontantInvalideException | SoldeInsuffisantException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    private static void faireVirement(Client client) {
        System.out.println("\n--- COMPTE SOURCE ---");
        Compte source = selectionnerCompte(client);
        if (source == null) return;

        System.out.println("\n--- COMPTE DESTINATION ---");
        Compte destination = selectionnerCompte(client);
        if (destination == null) return;

        System.out.print("Saisissez le montant du virement : ");
        float montant = lireFloat();

        try {
            clientService.effectuerVirement(source, destination, montant, sequenceIdTransaction++);
            System.out.println("Virement de " + montant + " MAD effectué avec succès !");
        } catch (MontantInvalideException | SoldeInsuffisantException | JournalisationException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // ==========================================
    // espace de gestionner
    // ==========================================
    private static void menuGestionnaire() {
        int choix = -1;
        while (choix != 0) {
            System.out.println("\n--- ESPACE GESTIONNAIRE ---");
            System.out.println("1. Créer un compte pour un client");
            System.out.println("2. Clôturer un compte client");
            System.out.println("3. Modifier les informations d'un client");
            System.out.println("4. Consulter le relevé d'un client");
            System.out.println("0. Déconnexion");
            System.out.print("Option : ");

            choix = lireEntier();

            switch (choix) {
                case 1 -> ajouterCompteGestionnaire();
                case 2 -> cloturerCompteGestionnaire();
                case 3 -> modifierInfoGestionnaire();
                case 4 -> consulterReleveGestionnaire();
                case 0 -> System.out.println("Déconnexion en cours...");
                default -> System.out.println("Option invalide.");
            }
        }
    }

    private static void ajouterCompteGestionnaire() {
        Client target = chercherClientParEmail();
        if (target == null) return;

        System.out.print("Numéro du nouveau compte : ");
        double num = scanner.nextDouble();
        System.out.print("Solde initial : ");
        float solde = scanner.nextFloat();

        System.out.println("Type de compte : 1. Courant | 2. Épargne");
        int type = lireEntier();

        Compte nouveauCompte = (type == 2) 
                ? new Epargne(num, solde, 2.5f) 
                : new Courant(num, solde, 500.0f);

        gestionnaireService.creerCompte(target, nouveauCompte);
    }

    private static void cloturerCompteGestionnaire() {
        Client target = chercherClientParEmail();
        if (target == null) return;

        System.out.print("Numéro du compte à clôturer : ");
        double num = scanner.nextDouble();
        gestionnaireService.cloturerCompte(target, num);
    }

    private static void modifierInfoGestionnaire() {
        Client target = chercherClientParEmail();
        if (target == null) return;

        System.out.print("Nouveau nom : ");
        String nom = scanner.nextLine();
        System.out.print("Nouveau prénom : ");
        String prenom = scanner.nextLine();
        System.out.print("Nouvel email : ");
        String email = scanner.nextLine();

        gestionnaireService.modifierInfoClient(target, nom, prenom, email);
    }

    private static void consulterReleveGestionnaire() {
        Client target = chercherClientParEmail();
        if (target != null) {
            gestionnaireService.consulterReleveClient(target);
        }
    }

    private static Client chercherClientParEmail() {
        System.out.print("Saisissez l'email du client ciblé : ");
        String email = scanner.nextLine();
        Person user = authService.trouverParEmail(email);

        if (user instanceof Client client) {
            return client;
        }
        System.out.println("Erreur : Aucun client trouvé avec l'email " + email);
        return null;
    }

    
    private static Compte selectionnerCompte(Client client) {
        afficherSoldes(client);
        if (client.getComptes().isEmpty()) return null;

        System.out.print("Entrez le numéro du compte à cibler : ");
        double num = scanner.nextDouble();

        Compte compte = client.getComptes().get(num);
        if (compte == null) {
            System.out.println("Erreur : Aucun compte trouvé avec ce numéro.");
        }
        return compte;
    }
    // validation de type d'entre
    private static int lireEntier() {
        while (!scanner.hasNextInt()) {
            System.out.print("Saisie invalide. Entrez un nombre entier : ");
            scanner.next();
        }
        int val = scanner.nextInt();
        scanner.nextLine(); // Consomme le retour à la ligne
        return val;
    }

    private static float lireFloat() {
        while (!scanner.hasNextFloat()) {
            System.out.print("Saisie invalide. Entrez un nombre décimal : ");
            scanner.next();
        }
        float val = scanner.nextFloat();
        scanner.nextLine(); // Consomme le retour à la ligne
        return val;
    }
}