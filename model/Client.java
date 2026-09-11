package model;

import java.util.HashMap;
import java.util.Map;

public class Client extends Person {
    private int idClient;
    private Map<Double, Compte> comptes;

    public Client(String nom, String prenom, String email, String motDePasse, int idClient) {
        super(nom, prenom, email, motDePasse);
        this.idClient = idClient;
        this.comptes = new HashMap<>();
    }

    @Override
    public String getRole() {
        return "CLIENT";
    }

    public int getIdClient() { return idClient; }
    public Map<Double, Compte> getComptes() { return comptes; }

    public void ajouterCompte(Compte compte) {
        if (compte != null) {
            this.comptes.put(compte.getNumeroCompte(), compte);
        }
    }
}