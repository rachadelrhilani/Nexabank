package model;

public class Epargne extends Compte {
    private float tauxInteret;

    public Epargne(double numeroCompte, float soldeInitial, float tauxInteret) {
        super(numeroCompte, soldeInitial);
        this.tauxInteret = tauxInteret;
    }

    public float getTauxInteret() {
        return tauxInteret;
    }

    public void calculerInterets() {
        float interets = this.solde * (this.tauxInteret / 100);
        this.solde += interets;
        System.out.println("Intérêts ajoutés : " + interets + " MAD. Nouveau solde : " + this.solde);
    }
}
