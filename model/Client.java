
package model;
import java.util.HashMap;
import java.util.Map;
public class Client extends Person {
    private int idClient;
    private Map<Double, Compte> comptes;

    public Client(String nom, String prenom, String email, int idClient) {
        super(nom, prenom, email);
        this.idClient = idClient;
        this.comptes = new HashMap<>();
    }

    public int getIdClient() {
        return idClient;
    }

    public Map<Double, Compte> getComptes() {
        return comptes;
    }

    public void ajouterCompte(Compte compte) {
        if (compte != null) {
            this.comptes.put(compte.getNumeroCompte(), compte);
        }
    }
}