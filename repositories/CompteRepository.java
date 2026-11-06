package repositories;

import database.DatabaseConnection;
import models.Compte;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompteRepository {

    // CREATE
    public void save(Compte compte) throws SQLException {
        String sql = """
            INSERT INTO comptes (numero, solde, client_id)
            VALUES (?, ?, ?)
            """;

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, compte.getNumero());
            statement.setBigDecimal(2, compte.getSolde());
            statement.setLong(3, compte.getClientId());

            statement.executeUpdate();
        }
    }

    // READ
    public Compte find(String numero) throws SQLException {
        try (Connection connection = DatabaseConnection.connect()) {
            return this.find(connection, numero);
        }
    }

    public Compte find(Connection connection, String numero) throws SQLException {
        String sql = "SELECT * FROM comptes WHERE numero = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numero);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapToCompte(result);
                }
            }
        }

        return null;
    }

    // READ ALL
    public List<Compte> findAll() throws SQLException {
        String sql = "SELECT * FROM comptes";
        List<Compte> comptes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                comptes.add(mapToCompte(result));
            }
        }

        return comptes;
    }

    // READ BY CLIENT
    public List<Compte> findByClientId(long clientId) throws SQLException {
        String sql = "SELECT * FROM comptes WHERE client_id = ?";
        List<Compte> comptes = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, clientId);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    comptes.add(mapToCompte(result));
                }
            }
        }

        return comptes;
    }

    // UPDATE
    public void updateSolde(String numero, BigDecimal solde) throws SQLException {
        try (Connection connection = DatabaseConnection.connect()) {
            this.updateSolde(connection, numero, solde);
        }
    }

    public void updateSolde(Connection connection, String numero, BigDecimal solde) throws SQLException {

        String sql = "UPDATE comptes SET solde = ? WHERE numero = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, solde);
            statement.setString(2, numero);

            statement.executeUpdate();
        }
    }

    // DELETE
    public void delete(String numero) throws SQLException {
        String sql = "DELETE FROM comptes WHERE numero = ?";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, numero);
            statement.executeUpdate();
        }
    }

    // Convertir une ligne SQL en objet Compte
    private Compte mapToCompte(ResultSet result) throws SQLException {
        return new Compte(
                result.getString("numero"),
                result.getBigDecimal("solde"),
                result.getLong("client_id")
        );
    }
}