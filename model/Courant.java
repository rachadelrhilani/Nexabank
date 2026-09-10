package model;

public class Courant extends Compte {

    public Courant(double numeroCompte, float soldeInitial) {
        super(numeroCompte, soldeInitial);
    }

    // Possibilité d'ajouter des comportements spécifiques au compte courant (ex: découvert autorisé)
}