package models;

import java.math.BigDecimal;
import java.util.Objects;

public class Compte {

    private final String numero;
    private BigDecimal solde;
    private final long clientId;

    private static final BigDecimal LIMITE_DECOUVERT = new BigDecimal("-500.00");

    public Compte(String numero, long clientId) {
        this(numero, BigDecimal.ZERO, clientId);
    }

    public Compte(String numero, BigDecimal solde, long clientId) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("Numéro de Compte obligatoire");
        }

        if (clientId <= 0) {
            throw new IllegalArgumentException("Client invalide");
        }

        Objects.requireNonNull(solde, "Solde obligatoire"); // solde non nul

        if (solde.compareTo(LIMITE_DECOUVERT) < 0) {
            throw new IllegalArgumentException("Limite autorisée (" + LIMITE_DECOUVERT + ")");
        }

        this.numero = numero;
        this.solde = solde;
        this.clientId = clientId;
    }

    public long getClientId() { return clientId; }
    public String getNumero() { return numero; }

    public BigDecimal getSolde() { return solde; }

    //
    public void deposit(BigDecimal montant) {
        if (montant == null || montant.signum() <= 0) {
            throw new IllegalArgumentException("Montant invalide");
        }

        solde = solde.add(montant);
    }

    //
    public void withdraw(BigDecimal montant) {
        if (montant == null || montant.signum() <= 0) {
            throw new IllegalArgumentException("Montant invalide");
        }

        // limit verification
        BigDecimal newSolde = solde.subtract(montant);
        if (newSolde.compareTo(LIMITE_DECOUVERT) < 0) {
            throw new IllegalArgumentException("Retrait impossible. Limite de découvert dépassée");
        }

        solde = newSolde;
    }

    //
    @Override
    public String toString() {
        return String.format("Compte[numero=%s, clientId=%d, solde=%s]", numero, clientId, solde);
    }
}