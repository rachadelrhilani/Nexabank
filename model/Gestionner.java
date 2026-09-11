package model;

public class Gestionner extends Person {
    private int idGestionner;

    public Gestionner(String nom, String prenom, String email, String motDePasse, int idGestionner) {
        super(nom, prenom, email, motDePasse);
        this.idGestionner = idGestionner;
    }

    @Override
    public String getRole() {
        return "GESTIONNAIRE";
    }

    public int getIdGestionner() { return idGestionner; }
}