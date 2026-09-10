package model;
import Exceptions.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private int idTransaction;
    private TypeTransaction type;
    private int montant;
    private Date date;
    private int compteSource;
    private int compteDestination;

    public Transaction(int idTransaction, TypeTransaction type, int montant, int compteSource, int compteDestination) {
        this.idTransaction = idTransaction;
        this.type = type;
        this.montant = montant;
        this.date = new Date();
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
    }

    public static void Virement(Compte source, Compte destination, int montant, int idTransaction)
            throws MontantInvalideException, SoldeInsuffisantException, JournalisationException {

        if (source == null || destination == null) {
            throw new IllegalArgumentException("Les comptes source et destination doivent être valides.");
        }

        source.retraitArgent(montant);
        destination.depotArgent(montant);

        int idSrc = (int) source.getNumeroCompte();
        int idDest = (int) destination.getNumeroCompte();

        Transaction tSource = new Transaction(idTransaction, TypeTransaction.VIREMENT, montant, idSrc, idDest);
        Transaction tDest = new Transaction(idTransaction, TypeTransaction.VIREMENT, montant, idSrc, idDest);

        source.ajouterTransaction(tSource);
        destination.ajouterTransaction(tDest);

        tSource.enregistreFichier(idSrc);
        tDest.enregistreFichier(idDest);
    }

    public void enregistreFichier(int numeroCompte) throws JournalisationException {
        String nomFichier = "compte_" + numeroCompte + ".txt";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String ligneLog = String.format("[%s] ID: %d | Type: %s | Montant: %d MAD | Source: %d | Dest: %d%n",
                sdf.format(this.date),
                this.idTransaction,
                this.type,
                this.montant,
                this.compteSource,
                this.compteDestination);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) {
            writer.write(ligneLog);
        } catch (IOException e) {
            throw new JournalisationException(
                    "Erreur lors de l'écriture de la transaction dans le fichier " + nomFichier, e);
        }
    }

    public void historiqueTransaction() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("Transaction #" + idTransaction +
                " | Date: " + sdf.format(date) +
                " | Type: " + type +
                " | Montant: " + montant + " MAD" +
                " | Source: " + compteSource +
                " | Destination: " + compteDestination);
    }

    public int getIdTransaction() {
        return idTransaction;
    }

    public TypeTransaction getType() {
        return type;
    }

    public int getMontant() {
        return montant;
    }

    public Date getDate() {
        return date;
    }

    public int getCompteSource() {
        return compteSource;
    }

    public int getCompteDestination() {
        return compteDestination;
    }
}