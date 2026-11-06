package database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    public static Connection connect() throws SQLException {
        Connection connection =
            DriverManager.getConnection("jdbc:sqlite:database/bank.db");
        
        // activate foreign keys
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException e) {
            connection.close();
            throw e;
        }

        return connection;
    }

    /**
     * contains sql codes
     * @throws IOException
     */
    public static void createSchema() throws IOException {
        Path schemaPath = Path.of("database/schema.sql");

        if (Files.notExists(schemaPath)) {
            String sql = """
                CREATE TABLE IF NOT EXISTS clients (
                    id INTEGER PRIMARY KEY,
                    nom TEXT NOT NULL,
                    prenom TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE
                );

                CREATE TABLE IF NOT EXISTS comptes (
                    numero TEXT PRIMARY KEY,
                    solde NUMERIC NOT NULL DEFAULT 0,
                    client_id INTEGER NOT NULL,
                    FOREIGN KEY (client_id) REFERENCES clients(id)
                );

                CREATE TABLE IF NOT EXISTS transactions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    type TEXT NOT NULL CHECK (
                        type IN ('DEPOT', 'RETRAIT', 'VIREMENT')
                    ),
                    montant NUMERIC NOT NULL CHECK (montant > 0),
                    compte_source TEXT,
                    compte_destination TEXT,
                    date_transaction TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,

                    FOREIGN KEY (compte_source) REFERENCES comptes(numero),
                    FOREIGN KEY (compte_destination) REFERENCES comptes(numero)
                );
                """;

            Files.createDirectories(schemaPath.getParent());
            Files.writeString(schemaPath, sql);

            System.out.println("schema.sql créé avec succès !");
        } else {
            System.out.println("schema.sql existe déjà.");
        }
    }

    /**
     * 
     * @param connection
     * @throws SQLException
     * @throws IOException
     */
    public static void initializeDatabase(Connection connection)
        throws SQLException, IOException {

        String sql = Files.readString(Path.of("database/schema.sql"));

        try (Statement statement = connection.createStatement()) {
            for (String query : sql.split(";")) {
                if (!query.isBlank()) {
                    statement.execute(query.trim());
                }
            }
        }

        System.out.println("Tables créées avec succès !");
    }
}