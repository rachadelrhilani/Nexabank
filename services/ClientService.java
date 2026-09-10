package services;

import Exceptions.JournalisationException;
import Exceptions.MontantInvalideException;
import Exceptions.SoldeInsuffisantException;
import model.Compte;
import model.Transaction;
import model.TypeTransaction;
import utils.FileLogger;

public class ClientService {

    public void effectuerDepot(Compte compte, int montant, int idTx) 
            throws MontantInvalideException, JournalisationException {
        compte.depotArgent(montant);
        int numCompte = (int) compte.getNumeroCompte();
        
        Transaction tx = new Transaction(idTx, TypeTransaction.DEPOT, montant, numCompte, numCompte);
        compte.ajouterTransaction(tx);
        FileLogger.enregistrerTransaction(numCompte, tx);
    }

    public void effectuerRetrait(Compte compte, int montant, int idTx) 
            throws MontantInvalideException, SoldeInsuffisantException, JournalisationException {
        compte.retraitArgent(montant);
        int numCompte = (int) compte.getNumeroCompte();

        Transaction tx = new Transaction(idTx, TypeTransaction.RETRAIT, montant, numCompte, numCompte);
        compte.ajouterTransaction(tx);
        FileLogger.enregistrerTransaction(numCompte, tx);
    }

    public void effectuerVirement(Compte source, Compte destination, int montant, int idTx) 
            throws MontantInvalideException, SoldeInsuffisantException, JournalisationException {
        if (source == null || destination == null) {
            throw new IllegalArgumentException("Comptes invalides pour le virement.");
        }

        source.retraitArgent(montant);
        destination.depotArgent(montant);

        int srcNum = (int) source.getNumeroCompte();
        int destNum = (int) destination.getNumeroCompte();

        Transaction txSource = new Transaction(idTx, TypeTransaction.VIREMENT, montant, srcNum, destNum);
        Transaction txDest = new Transaction(idTx, TypeTransaction.VIREMENT, montant, srcNum, destNum);

        source.ajouterTransaction(txSource);
        destination.ajouterTransaction(txDest);

        FileLogger.enregistrerTransaction(srcNum, txSource);
        FileLogger.enregistrerTransaction(destNum, txDest);
    }
}