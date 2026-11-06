package repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import java.sql.Statement;
import database.DatabaseConnection;
import models.Client;

public class ClientRepository {
    // save
    public long save(Client client) throws SQLException {
        String sql = """
                INSERT INTO clients (nom, prenom, email)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.nom());
            statement.setString(2, client.prenom());
            statement.setString(3, client.email());

            statement.executeUpdate();

            // retrieve the ID
            try (Statement idStatement = connection.createStatement();
                    ResultSet result = idStatement.executeQuery(
                            "SELECT last_insert_rowid()")) {

                if (result.next()) {
                    return result.getLong(1);
                }
            }
        }

        throw new SQLException("Impossible de récupérer l'ID du client");
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT 1 FROM clients WHERE email = ?";

        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {
                return result.next();
            }
        }
    }

    // find
    public Client find(Client client) throws SQLException {
        return this.find(client.id());
    }

    public Client find(long clientId) throws SQLException {
        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, clientId);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return new Client(
                            result.getLong("id"),
                            result.getString("nom"),
                            result.getString("prenom"),
                            result.getString("email"));
                }
            }
        }

        return null;
    }

    // find all
    public List<Client> findAll() throws SQLException {
        String sql = "SELECT * FROM clients";

        List<Client> clients = new ArrayList<>();

        try (Connection connection = DatabaseConnection.connect();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Client client = new Client(
                        result.getLong("id"),
                        result.getString("nom"),
                        result.getString("prenom"),
                        result.getString("email"));

                clients.add(client);
            }
        }

        return clients;
    }
}
