/* package model;
import java.util.Map;


public class Gestionner extends Person {
    private int idGestionner;

    public Gestionner(String nom, String prenom, String email, int idGestionner) {
        super(nom, prenom, email);
        this.idGestionner = idGestionner;
    }

    public int getIdGestionner() {
        return idGestionner;
    }

    public void creerCompte(Client client, Compte compte) {
        if (client == null || compte == null) {
            System.err.println("Erreur : Le client ou le compte ne peut pas être nul.");
            return;
        }

        if (client.getComptes().containsKey(compte.getNumeroCompte())) {
            System.out.println("Le compte N°" + compte.getNumeroCompte() + " existe déjà pour ce client.");
        } else {
            client.ajouterCompte(compte);
            System.out.println("Compte N°" + compte.getNumeroCompte() + " attribué au client : " 
                    + client.getPrenom() + " " + client.getNom() + " (ID: " + client.getIdClient() + ")");
        }
    }

    public void cloturerCompte(Client client, double numeroCompte) {
        if (client == null) {
            System.err.println("Erreur : Client introuvable.");
            return;
        }

        Map<Double, Compte> comptesClient = client.getComptes();
        if (comptesClient.containsKey(numeroCompte)) {
            comptesClient.remove(numeroCompte);
            System.out.println("Le compte N°" + numeroCompte + " du client " + client.getNom() + " a été clôturé.");
        } else {
            System.out.println("Échec : Aucun compte N°" + numeroCompte + " trouvé pour ce client.");
        }
    }

    public void modifierInfoClient(Client client, String nouveauNom, String nouveauPrenom, String nouvelEmail) {
        if (client == null) {
            System.err.println("Erreur : Client invalide.");
            return;
        }
        client.setNom(nouveauNom);
        client.setPrenom(nouveauPrenom);
        client.setEmail(nouvelEmail);
        System.out.println("Mise à jour réussie des données du client ID: " + client.getIdClient());
    }

    public void consulterReleveClient(Client client) {
        if (client == null) {
            System.err.println("Erreur : Client introuvable.");
            return;
        }

        System.out.println("==================================================");
        System.out.println("RELEVÉ BANCAIRE DU CLIENT : " + client.getPrenom() + " " + client.getNom() + " (ID: " + client.getIdClient() + ")");
        System.out.println("==================================================");

        Map<Double, Compte> comptes = client.getComptes();
        if (comptes.isEmpty()) {
            System.out.println("Ce client ne possède aucun compte.");
            return;
        }

        for (Compte compte : comptes.values()) {
            System.out.println("\n--- Compte N°: " + compte.getNumeroCompte() + " | Solde : " + compte.getSolde() + " MAD ---");
            if (compte.getHistoriqueTransactions().isEmpty()) {
                System.out.println("  Aucune transaction enregistrée.");
            } else {
                for (Transaction transaction : compte.getHistoriqueTransactions()) {
                    System.out.print("  ");
                    transaction.historiqueTransaction();
                }
            }
        }
        System.out.println("==================================================");
    }
} */
package model;

public class Gestionner extends Person {
    private int idGestionner;

    public Gestionner(String nom, String prenom, String email, int idGestionner) {
        super(nom, prenom, email);
        this.idGestionner = idGestionner;
    }

    public int getIdGestionner() { return idGestionner; }
}