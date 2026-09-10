package model;
import Exceptions.MontantInvalideException;
import Exceptions.SoldeInsuffisantException;

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

    public void depotArgent(float montant) throws MontantInvalideException {
        if (montant <= 0) {
            throw new MontantInvalideException("Le montant du dépôt doit être strictement positif.");
        }
        this.solde += montant;
    }

    public void retraitArgent(float montant) throws MontantInvalideException, SoldeInsuffisantException {
        if (montant <= 0) {
            throw new MontantInvalideException("Le montant du retrait doit être strictement positif.");
        }
        if (this.solde < montant) {
            throw new SoldeInsuffisantException("Solde insuffisant pour effectuer un retrait de " + montant + " MAD. Solde actuel : " + this.solde);
        }
        this.solde -= montant;
    }

    // Getters et Setters
    public double getNumeroCompte() {
        return numeroCompte;
    }

    public float getSolde() {
        return solde;
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