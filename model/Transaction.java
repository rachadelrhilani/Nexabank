package model;

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

    public void historiqueTransaction() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        System.out.println("Transaction #" + idTransaction +
                " | Date: " + sdf.format(date) +
                " | Type: " + type +
                " | Montant: " + montant + " MAD" +
                " | Source: " + compteSource +
                " | Destination: " + compteDestination);
    }

    public int getIdTransaction() { return idTransaction; }
    public TypeTransaction getType() { return type; }
    public int getMontant() { return montant; }
    public Date getDate() { return date; }
    public int getCompteSource() { return compteSource; }
    public int getCompteDestination() { return compteDestination; }
}