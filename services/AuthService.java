package services;

import model.Person;
import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<Person> utilisateurs = new ArrayList<>();

    public void inscrireUtilisateur(Person person) {
        if (person != null) {
            utilisateurs.add(person);
        }
    }

    public Person authentifier(String email, String motDePasse) {
        for (Person user : utilisateurs) {
            if (user.getEmail().equalsIgnoreCase(email) && user.verifierMotDePasse(motDePasse)) {
                return user; 
            }
        }
        return null; 
    }

    public Person trouverParEmail(String email) {
        if (email == null) {
            return null;
        }

        for (Person user : utilisateurs) {
            if (user.getEmail().equalsIgnoreCase(email.trim())) {
                return user;
            }
        }
        return null;
    }
}