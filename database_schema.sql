-- Base de données pour le suivi de scolarité EISTI
CREATE DATABASE IF NOT EXISTS eisti_scolarite;
USE eisti_scolarite;

-- Table Étudiant
CREATE TABLE etudiant (
    id_etudiant INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    origine_scolaire VARCHAR(50) NOT NULL,
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Année Scolaire
CREATE TABLE annee_scolaire (
    id_annee INT PRIMARY KEY AUTO_INCREMENT,
    annee VARCHAR(20) NOT NULL UNIQUE,
    date_debut DATE,
    date_fin DATE
);

-- Table Programme
CREATE TABLE programme (
    id_programme INT PRIMARY KEY AUTO_INCREMENT,
    code_programme VARCHAR(20) NOT NULL UNIQUE,
    nom_programme VARCHAR(200) NOT NULL,
    annee INT NOT NULL,
    type VARCHAR(50) NOT NULL,
    CONSTRAINT chk_annee CHECK (annee IN (1, 2, 3))
);

-- Table Prérequis
CREATE TABLE prerequis (
    id_programme_requis INT NOT NULL,
    id_programme_necessitant INT NOT NULL,
    PRIMARY KEY (id_programme_requis, id_programme_necessitant),
    FOREIGN KEY (id_programme_requis) REFERENCES programme(id_programme) ON DELETE CASCADE,
    FOREIGN KEY (id_programme_necessitant) REFERENCES programme(id_programme) ON DELETE CASCADE
);

-- Table Matière
CREATE TABLE matiere (
    id_matiere INT PRIMARY KEY AUTO_INCREMENT,
    nom_matiere VARCHAR(200) NOT NULL,
    objectif TEXT
);

-- Table Enseigner (Matière enseignée dans une année)
CREATE TABLE enseigner (
    id_matiere INT NOT NULL,
    id_annee INT NOT NULL,
    semestre INT NOT NULL,
    ponderation DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    PRIMARY KEY (id_matiere, id_annee),
    FOREIGN KEY (id_matiere) REFERENCES matiere(id_matiere) ON DELETE CASCADE,
    FOREIGN KEY (id_annee) REFERENCES annee_scolaire(id_annee) ON DELETE CASCADE,
    CONSTRAINT chk_semestre CHECK (semestre IN (1, 2))
);

-- Table Composer (Programme contient des matières)
CREATE TABLE composer (
    id_programme INT NOT NULL,
    id_matiere INT NOT NULL,
    PRIMARY KEY (id_programme, id_matiere),
    FOREIGN KEY (id_programme) REFERENCES programme(id_programme) ON DELETE CASCADE,
    FOREIGN KEY (id_matiere) REFERENCES matiere(id_matiere) ON DELETE CASCADE
);

-- Table Inscription
CREATE TABLE inscription (
    id_etudiant INT NOT NULL,
    id_programme INT NOT NULL,
    id_annee INT NOT NULL,
    moyenne_generale DECIMAL(5,2),
    statut VARCHAR(50) DEFAULT 'En cours',
    PRIMARY KEY (id_etudiant, id_programme, id_annee),
    FOREIGN KEY (id_etudiant) REFERENCES etudiant(id_etudiant) ON DELETE CASCADE,
    FOREIGN KEY (id_programme) REFERENCES programme(id_programme) ON DELETE CASCADE,
    FOREIGN KEY (id_annee) REFERENCES annee_scolaire(id_annee) ON DELETE CASCADE,
    CONSTRAINT chk_statut CHECK (statut IN ('En cours', 'Passage', 'Redoublant', 'Exclu'))
);

-- Table Projet
CREATE TABLE projet (
    id_projet INT PRIMARY KEY AUTO_INCREMENT,
    nom_projet VARCHAR(200) NOT NULL,
    description TEXT
);

-- Table Contenir Projet (Projet regroupe plusieurs matières)
CREATE TABLE contenir_projet (
    id_projet INT NOT NULL,
    id_matiere INT NOT NULL,
    PRIMARY KEY (id_projet, id_matiere),
    FOREIGN KEY (id_projet) REFERENCES projet(id_projet) ON DELETE CASCADE,
    FOREIGN KEY (id_matiere) REFERENCES matiere(id_matiere) ON DELETE CASCADE
);

-- Table Épreuve
CREATE TABLE epreuve (
    id_epreuve INT PRIMARY KEY AUTO_INCREMENT,
    nom_epreuve VARCHAR(200) NOT NULL,
    type_epreuve VARCHAR(50) NOT NULL
);

-- Table Noter Épreuve
CREATE TABLE noter_epreuve (
    id_notation INT PRIMARY KEY AUTO_INCREMENT,
    id_etudiant INT NOT NULL,
    id_epreuve INT NOT NULL,
    id_matiere INT NOT NULL,
    id_annee INT NOT NULL,
    note DECIMAL(5,2) NOT NULL,
    ponderation DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    date_notation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_etudiant) REFERENCES etudiant(id_etudiant) ON DELETE CASCADE,
    FOREIGN KEY (id_epreuve) REFERENCES epreuve(id_epreuve) ON DELETE CASCADE,
    FOREIGN KEY (id_matiere) REFERENCES matiere(id_matiere) ON DELETE CASCADE,
    FOREIGN KEY (id_annee) REFERENCES annee_scolaire(id_annee) ON DELETE CASCADE,
    CONSTRAINT chk_note_epreuve CHECK (note >= 0 AND note <= 20)
);

-- Table Noter Projet
CREATE TABLE noter_projet (
    id_notation INT PRIMARY KEY AUTO_INCREMENT,
    id_etudiant INT NOT NULL,
    id_projet INT NOT NULL,
    id_annee INT NOT NULL,
    note DECIMAL(5,2) NOT NULL,
    ponderation DECIMAL(5,2) NOT NULL DEFAULT 1.0,
    date_notation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_etudiant) REFERENCES etudiant(id_etudiant) ON DELETE CASCADE,
    FOREIGN KEY (id_projet) REFERENCES projet(id_projet) ON DELETE CASCADE,
    FOREIGN KEY (id_annee) REFERENCES annee_scolaire(id_annee) ON DELETE CASCADE,
    CONSTRAINT chk_note_projet CHECK (note >= 0 AND note <= 20)
);

-- Index pour améliorer les performances
CREATE INDEX idx_etudiant_nom ON etudiant(nom, prenom);
CREATE INDEX idx_inscription_annee ON inscription(id_annee);
CREATE INDEX idx_notes_etudiant ON noter_epreuve(id_etudiant);
CREATE INDEX idx_notes_projet_etudiant ON noter_projet(id_etudiant);

-- Données d'exemple
INSERT INTO annee_scolaire (annee, date_debut, date_fin) VALUES 
('2025/2026', '2025-09-01', '2026-06-30'),
('2024/2025', '2024-09-01', '2025-06-30');

INSERT INTO programme (code_programme, nom_programme, annee, type) VALUES
('ING1_TC', 'Tronc Commun 1ère année', 1, 'Tronc Commun'),
('ING2_TC', 'Tronc Commun 2ème année', 2, 'Tronc Commun'),
('ING2_GI', 'Spécialité Génie Informatique', 2, 'Spécialité'),
('ING2_GM', 'Spécialité Génie Mathématique', 2, 'Spécialité'),
('ING2_MSI', 'Orientation MSI', 2, 'Orientation'),
('ING2_TSI', 'Orientation TSI', 2, 'Orientation'),
('ING2_IFI', 'Orientation IFI', 2, 'Orientation'),
('ING3_ISIN', 'Ingénierie des Systèmes d''Information et des Réseaux', 3, 'Option'),
('ING3_GL', 'Génie Logiciel', 3, 'Option'),
('ING3_ISICO', 'Ingénierie des Systèmes Informatiques et de Communication', 3, 'Option'),
('ING3_IAD', 'Informatique et Aide à la Décision', 3, 'Option');

INSERT INTO matiere (nom_matiere, objectif) VALUES
('Algorithmique', 'Apprendre les bases de l''algorithmique et des structures de données'),
('Programmation Java', 'Maîtriser la programmation orientée objet avec Java'),
('Base de Données', 'Comprendre la conception et l''exploitation des bases de données'),
('Réseaux', 'Apprendre les fondamentaux des réseaux informatiques'),
('Mathématiques', 'Acquérir les bases mathématiques pour l''informatique');

INSERT INTO composer (id_programme, id_matiere) VALUES
(1, 1), (1, 2), (1, 5),
(2, 2), (2, 3), (2, 4);

INSERT INTO enseigner (id_matiere, id_annee, semestre, ponderation) VALUES
(1, 1, 1, 2.0),
(2, 1, 1, 3.0),
(3, 1, 2, 2.5),
(4, 1, 2, 2.0),
(5, 1, 1, 1.5);