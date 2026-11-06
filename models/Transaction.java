package models;

import types.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record Transaction(
        long id,
        OperationType type,
        BigDecimal montant,
        String compteSource,
        String compteDestination,
        LocalDateTime dateTransaction
) {
    // with default id
    public Transaction(
            OperationType type,
            BigDecimal montant,
            String compteSource,
            String compteDestination
    ) {
        this(0L, type, montant, compteSource, compteDestination, LocalDateTime.now());
    }

    //
    public Transaction {
        Objects.requireNonNull(type, "Type obligatoire");
        Objects.requireNonNull(montant, "Montant obligatoire");
        Objects.requireNonNull(dateTransaction, "Date obligatoire");

        if (id < 0) {
            throw new IllegalArgumentException("ID invalide");
        }

        if (montant.signum() <= 0) {
            throw new IllegalArgumentException("Montant invalide");
        }

        switch (type) {

            case DEPOT -> {
                // Argent extérieur -> Compte bancaire
                if (compteSource != null) {
                    throw new IllegalArgumentException(
                            "Cette transaction doit être un virement"
                    );
                }

                if (compteDestination == null || compteDestination.isBlank()) {
                    throw new IllegalArgumentException(
                            "Compte destinataire obligatoire pour un dépôt"
                    );
                }
            }

            case RETRAIT -> {
                // Compte bancaire -> Argent extérieur
                if (compteSource == null || compteSource.isBlank()) {
                    throw new IllegalArgumentException(
                            "Compte source obligatoire pour un retrait"
                    );
                }

                if (compteDestination != null) {
                    throw new IllegalArgumentException(
                            "Un retrait ne doit pas avoir de compte destinataire"
                    );
                }
            }

            case VIREMENT -> {
                // Compte bancaire -> Compte bancaire
                if (compteSource == null || compteSource.isBlank()) {
                    throw new IllegalArgumentException(
                            "Compte source obligatoire pour un virement"
                    );
                }

                if (compteDestination == null || compteDestination.isBlank()) {
                    throw new IllegalArgumentException(
                            "Compte destinataire obligatoire pour un virement"
                    );
                }

                if (compteSource.equals(compteDestination)) {
                    throw new IllegalArgumentException(
                            "Le compte source et le compte destinataire doivent être différents"
                    );
                }
            }
        }
    }
}