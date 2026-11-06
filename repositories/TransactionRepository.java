package repositories;

import database.DatabaseConnection;
import models.Transaction;
import types.OperationType;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {

    // CREATE
    public void save(Transaction transaction) throws SQLException {
        try (Connection connection = DatabaseConnection.connect()) {
            this.save(connection, transaction);
        }
    }

    public void save(Connection connection, Transaction transaction) throws SQLException {
        String sql = """
            INSERT INTO transactions
            (type, montant, compte_source, compte_destination, date_transaction)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, transaction.type().name());
            statement.setBigDecimal(2, transaction.montant());
            statement.setString(3, transaction.compteSource());
            statement.setString(4, transaction.compteDestination());
            statement.setString(5, transaction.dateTransaction().toString());

            statement.executeUpdate();
        }
    }


    // READ
    public Transaction find(long id) throws SQLException {
        try (Connection connection = DatabaseConnection.connect()) {
            return this.find(connection, id);
        }
    }


    public Transaction find(Connection connection, long id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapToTransaction(result);
                }
            }
        }

        return null;
    }
    

    // READ ALL
    public List<Transaction> findAll() throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY id DESC";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                transactions.add(mapToTransaction(result));
            }
        }

        return transactions;
    }

    // Historique d'un compte
    public List<Transaction> findByCompte(String numero)
            throws SQLException {

        String sql = """
            SELECT * FROM transactions
            WHERE compte_source = ? OR compte_destination = ?
            ORDER BY id DESC
            """;

        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numero);
            statement.setString(2, numero);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    transactions.add(mapToTransaction(result));
                }
            }
        }

        return transactions;
    }

    // Convertir une ligne SQL en objet Transaction
    private Transaction mapToTransaction(ResultSet result)
            throws SQLException {

        return new Transaction(
                result.getLong("id"),
                OperationType.valueOf(result.getString("type")),
                result.getBigDecimal("montant"),
                result.getString("compte_source"),
                result.getString("compte_destination"),
                LocalDateTime.parse(result.getString("date_transaction")
                        .replace(" ", "T"))
        );
    }
}