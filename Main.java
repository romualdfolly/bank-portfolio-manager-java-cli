import database.DatabaseConnection;
import models.Client;
import models.Compte;
import services.BanqueService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        BanqueService banque = new BanqueService();


        // Connection, Schema & initialization
        try {
            // schema instructions
            DatabaseConnection.createSchema();

            // connection et initialization
            try (Connection connection = DatabaseConnection.connect()) {
                System.out.println("Connexion SQLite réussie !");
                DatabaseConnection.initializeDatabase(connection);
            }

        } catch (SQLException | IOException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        
        // clients & comptes
        try {
            // Alice & Bob
            divider("USERS CREATION");
            Client alice = banque.createClient(new Client("CHOPARD", "Alice", "alice@chopard.com"));
            Client bob = banque.createClient(new Client("Kurtz", "Bob", "kurtz@data.bob"));

            System.out.println(alice);
            System.out.println(bob);

            // accounts
            divider("ACCOUNTS CREATION");
            String aNumber = "CH001", bNumber = "CH002";
            banque.openCompte(aNumber, alice.id());
            banque.openCompte(bNumber, bob.id());

            Compte aCompte = banque.findCompte(aNumber);
            Compte bCompte = banque.findCompte(bNumber);
            System.out.println(aCompte);
            System.out.println(bCompte);

            // transctions
            divider("TRANSACTIONS");
            banque.deposit(aCompte.getNumero(), new BigDecimal("1000"));
            banque.deposit(bCompte.getNumero(), new BigDecimal("500"));
            banque.withdraw(aCompte.getNumero(), new BigDecimal("200"));
            banque.transfert(aCompte.getNumero(), bCompte.getNumero(), new BigDecimal("300"));
            banque.withdraw(bCompte.getNumero(), new BigDecimal("100"));

            aCompte = banque.findCompte(aNumber);
            bCompte = banque.findCompte(bNumber);
            System.out.println(aCompte);
            System.out.println(bCompte);

            // history
            divider("HISTORIQUE");
            System.out.println(banque.historique("CH001"));
            System.out.println(banque.historique("CH002"));

            // limit ops
            divider("OPERATIONS LIMITES : 1500 A -> B");
            banque.transfert(aCompte.getNumero(), bCompte.getNumero(), new BigDecimal("1500"));

            aCompte = banque.findCompte(aNumber);
            bCompte = banque.findCompte(bNumber);
            System.out.println(aCompte);
            System.out.println(bCompte);

        } catch (SQLException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    //
    public static void divider() {
        divider("");
    }

    public static void divider(String message) {
        System.out.printf("\n>>------------------ [%s] ------------------\n", message);
    }
}