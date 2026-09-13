package model;

public abstract class Person {
    protected String nom;
    protected String prenom;
    protected String email;
    protected String motDePasse;

    public Person(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    // method pour connaitre le role
    public abstract String getRole();

    public boolean verifierMotDePasse(String mdp) {
        return this.motDePasse.equals(mdp);
    }

    public void login() {
        System.out.println("Connexion réussie ! Bienvenue " + prenom + " " + nom + " [" + getRole() + "]");
    }

    // getters et setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
}