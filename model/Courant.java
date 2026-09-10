package model;

public class Courant extends Compte {
    private float decouvertAutorise;

    public Courant(double numeroCompte, float soldeInitial, float decouvertAutorise) {
        super(numeroCompte, soldeInitial);
        this.decouvertAutorise = decouvertAutorise;
    }

    public Courant(double numeroCompte, float soldeInitial) {
        this(numeroCompte, soldeInitial, 500.0f);
    }


    public float getDecouvertAutorise() { return decouvertAutorise; }
    public void setDecouvertAutorise(float decouvertAutorise) { this.decouvertAutorise = decouvertAutorise; }
}