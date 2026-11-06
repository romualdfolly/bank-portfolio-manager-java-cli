package models;

public record Client(long id, String nom, String prenom, String email) {
    //
    public Client (String nom, String prenom, String email) {
        this(0L, nom, prenom, email);
    }

    //
    public Client {
        if (id < 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        if (nom == null || nom.isBlank()) {
            throw new IllegalArgumentException("Nom obligatoire");
        }

        if (prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("Prénom obligatoire");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email obligatoire");
        }
    }
}