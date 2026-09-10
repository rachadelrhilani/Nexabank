package services;

import model.Client;
import model.Compte;
import model.Transaction;

import java.util.Map;

public class GestionnaireService {

    public void creerCompte(Client client, Compte compte) {
        if (client != null && compte != null) {
            client.ajouterCompte(compte);
            System.out.println("Compte N°" + (int)compte.getNumeroCompte() + " attribué avec succès.");
        }
    }

    public void cloturerCompte(Client client, double numeroCompte) {
        if (client != null && client.getComptes().containsKey(numeroCompte)) {
            client.getComptes().remove(numeroCompte);
            System.out.println("Compte N°" + (int)numeroCompte + " clôturé.");
        } else {
            System.out.println("Échec : Compte introuvable.");
        }
    }

    public void modifierInfoClient(Client client, String nom, String prenom, String email) {
        if (client != null) {
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setEmail(email);
            System.out.println("Informations mises à jour pour le client ID: " + client.getIdClient());
        }
    }

    public void consulterReleveClient(Client client) {
        if (client == null) return;

        System.out.println("\n==========================================");
        System.out.println("RELEVÉ CLIENT : " + client.getPrenom() + " " + client.getNom());
        System.out.println("==========================================");

        Map<Double, Compte> comptes = client.getComptes();
        if (comptes.isEmpty()) {
            System.out.println("Aucun compte actif.");
            return;
        }

        for (Compte compte : comptes.values()) {
            System.out.println("\n--- Compte N°: " + (int)compte.getNumeroCompte() + " | Solde: " + compte.getSolde() + " MAD ---");
            if (compte.getHistoriqueTransactions().isEmpty()) {
                System.out.println("  Aucune transaction enregistrée.");
            } else {
                for (Transaction tx : compte.getHistoriqueTransactions()) {
                    System.out.print("  ");
                    tx.historiqueTransaction();
                }
            }
        }
    }
}