package services;

import Exceptions.JournalisationException;
import Exceptions.MontantInvalideException;
import Exceptions.SoldeInsuffisantException;
import model.Compte;
import model.Courant;
import model.Epargne;
import model.Transaction;
import model.TypeTransaction;
import utils.FileLogger;

public class ClientService {

    private static final float FRAIS_REGULARISATION = 20.0f;
    private static final float SOLDE_MINIMUM_EPARGNE = 100.0f;
    private static final float PLAFOND_MAXIMAL_EPARGNE = 100000.0f;

    public void effectuerDepot(Compte compte, float montant, int idTx) 
            throws MontantInvalideException, JournalisationException {
        
        if (compte == null) {
            throw new IllegalArgumentException("Compte introuvable.");
        }
        if (montant <= 0) {
            throw new MontantInvalideException("Le montant du dépôt doit être strictement positif.");
        }

        // la verification
        if (compte instanceof Courant) {
            if (compte.getSolde() < 0) {
                System.out.println("Compte à découvert : application de " + FRAIS_REGULARISATION + " MAD de frais.");
                compte.setSolde(compte.getSolde() - FRAIS_REGULARISATION);
            }
        } else if (compte instanceof Epargne) {
            if (compte.getSolde() + montant > PLAFOND_MAXIMAL_EPARGNE) {
                throw new MontantInvalideException(
                    "Dépôt refusé : Le plafond maximal du compte épargne (" + PLAFOND_MAXIMAL_EPARGNE + " MAD) sera dépassé."
                );
            }
        }

        // depot de l'argent apres la verification
        compte.depotArgent(montant);

        // la transaction et l'enregistrement dans un fichier text
        int numCompte = (int) compte.getNumeroCompte();
        Transaction tx = new Transaction(idTx, TypeTransaction.DEPOT, (int) montant, numCompte, numCompte);
        compte.ajouterTransaction(tx);
        FileLogger.enregistrerTransaction(numCompte, tx);
    }

    public void effectuerRetrait(Compte compte, float montant, int idTx) 
            throws MontantInvalideException, SoldeInsuffisantException, JournalisationException {
        
        if (compte == null) {
            throw new IllegalArgumentException("Compte introuvable.");
        }
        if (montant <= 0) {
            throw new MontantInvalideException("Le montant du retrait doit être strictement positif.");
        }

        float soldeActuel = compte.getSolde();

        // la verification
        if (compte instanceof Courant courant) {
            if (soldeActuel + courant.getDecouvertAutorise() < montant) {
                throw new SoldeInsuffisantException(
                    "Découvert dépassé ! Solde disponible : " + (soldeActuel + courant.getDecouvertAutorise()) + " MAD."
                );
            }
        } else if (compte instanceof Epargne) {
            if (soldeActuel - montant < SOLDE_MINIMUM_EPARGNE) {
                throw new SoldeInsuffisantException(
                    "Opération refusée : Un compte épargne doit maintenir un solde minimum de " + SOLDE_MINIMUM_EPARGNE + " MAD."
                );
            }
        }

        // retirer l'argent apres la verification
        compte.retraitArgent(montant);

        // la transaction et l'enregistrement dans un fichier text
        int numCompte = (int) compte.getNumeroCompte();
        Transaction tx = new Transaction(idTx, TypeTransaction.RETRAIT, (int) montant, numCompte, numCompte);
        compte.ajouterTransaction(tx);
        FileLogger.enregistrerTransaction(numCompte, tx);
    }

    public void effectuerVirement(Compte source, Compte destination, float montant, int idTx) 
            throws MontantInvalideException, SoldeInsuffisantException, JournalisationException {
        // les exceptions
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Comptes invalides.");
        }
        if (source.getNumeroCompte() == destination.getNumeroCompte()) {
            throw new MontantInvalideException("Virement impossible sur le même compte.");
        }

        // retrait le compte source
        effectuerRetrait(source, montant, idTx);

        // 
        try {
            effectuerDepot(destination, montant, idTx);
        } catch (MontantInvalideException e) {
            source.depotArgent(montant); // Rollback
            throw new MontantInvalideException("Virement annulé : " + e.getMessage());
        }

        int srcNum = (int) source.getNumeroCompte();
        int destNum = (int) destination.getNumeroCompte();

        Transaction txSrc = new Transaction(idTx, TypeTransaction.VIREMENT, (int) montant, srcNum, destNum);
        Transaction txDest = new Transaction(idTx, TypeTransaction.VIREMENT, (int) montant, srcNum, destNum);

        source.ajouterTransaction(txSrc);
        destination.ajouterTransaction(txDest);

        FileLogger.enregistrerTransaction(srcNum, txSrc);
        FileLogger.enregistrerTransaction(destNum, txDest);
    }
}