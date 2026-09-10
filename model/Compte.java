package model;

import java.util.HashSet;
import java.util.Set;

public abstract class Compte {
    
    protected double numeroCompte;
    protected float solde;
    protected Set<Transaction> historiqueTransactions;

    public Compte(double numeroCompte, float soldeInitial) {
        this.numeroCompte = numeroCompte;
        this.solde = soldeInitial;
        this.historiqueTransactions = new HashSet<>();
    }

    public float consulterSolde() {
        return this.solde;
    }
    public void depotArgent(float montant) {
        this.solde += montant;
    }

    public void retraitArgent(float montant) {
        this.solde -= montant;
    }

    // public abstract void depotArgent(float montant);

    // public abstract void retraitArgent(float montant);

    // Getters et Setters
    public double getNumeroCompte() {
        return numeroCompte;
    }

    public float getSolde() {
        return solde;
    }
    public void setSolde(float solde){
        this.solde = solde;
    }

    public Set<Transaction> getHistoriqueTransactions() {
        return historiqueTransactions;
    }

    public void ajouterTransaction(Transaction transaction) {
        if (transaction != null) {
            this.historiqueTransactions.add(transaction);
        }
    }
}