package model;

public class Epargne extends Compte {
    private float tauxInteret;

    public Epargne(double numeroCompte, float soldeInitial, float tauxInteret) {
        super(numeroCompte, soldeInitial);
        this.tauxInteret = tauxInteret;
    }


    public float getTauxInteret() { return tauxInteret; }
    public void setTauxInteret(float tauxInteret) { this.tauxInteret = tauxInteret; }
}