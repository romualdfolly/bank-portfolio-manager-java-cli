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
