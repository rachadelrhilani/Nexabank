package utils;

import Exceptions.JournalisationException;
import model.Transaction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class FileLogger {

    public static void enregistrerTransaction(int numeroCompte, Transaction transaction) throws JournalisationException {
        String nomFichier = "compte_" + numeroCompte + ".txt";
        // écrire la date selon un modele
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String ligneLog = String.format("[%s] ID: %d | Type: %s | Montant: %d MAD | Source: %d | Dest: %d%n",
                sdf.format(transaction.getDate()),
                transaction.getIdTransaction(),
                transaction.getType(),
                transaction.getMontant(),
                transaction.getCompteSource(),
                transaction.getCompteDestination()
        );
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier, true))) {
            writer.write(ligneLog);
        } catch (IOException e) {
            throw new JournalisationException("Erreur d'écriture dans le fichier " + nomFichier, e);
        }
    }
}