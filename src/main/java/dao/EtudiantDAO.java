/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import config.DatabaseConfig;
import model.Etudiant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EtudiantDAO {
    
    public int insererEtudiant(Etudiant etudiant) throws SQLException {
        String sql = "INSERT INTO etudiant (nom, prenom, origine_scolaire) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getOrigineScolaire());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new SQLException("Échec de l'insertion, aucune ligne affectée.");
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Échec de l'insertion, aucun ID obtenu.");
                }
            }
        }
    }
    
    public Etudiant getEtudiantById(int id) throws SQLException {
        String sql = "SELECT * FROM etudiant WHERE id_etudiant = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Etudiant(
                    rs.getInt("id_etudiant"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("origine_scolaire")
                );
            }
            return null;
        }
    }
    
    public List<Etudiant> getTousEtudiants() throws SQLException {
        String sql = "SELECT * FROM etudiant ORDER BY nom, prenom";
        List<Etudiant> etudiants = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                etudiants.add(new Etudiant(
                    rs.getInt("id_etudiant"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("origine_scolaire")
                ));
            }
        }
        return etudiants;
    }
    
    public boolean modifierEtudiant(Etudiant etudiant) throws SQLException {
        String sql = "UPDATE etudiant SET nom = ?, prenom = ?, origine_scolaire = ? WHERE id_etudiant = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, etudiant.getNom());
            stmt.setString(2, etudiant.getPrenom());
            stmt.setString(3, etudiant.getOrigineScolaire());
            stmt.setInt(4, etudiant.getIdEtudiant());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean supprimerEtudiant(int id) throws SQLException {
        String sql = "DELETE FROM etudiant WHERE id_etudiant = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    public List<Etudiant> rechercherEtudiants(String recherche) throws SQLException {
        String sql = "SELECT * FROM etudiant WHERE nom LIKE ? OR prenom LIKE ? ORDER BY nom, prenom";
        List<Etudiant> etudiants = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String pattern = "%" + recherche + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                etudiants.add(new Etudiant(
                    rs.getInt("id_etudiant"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("origine_scolaire")
                ));
            }
        }
        return etudiants;
    }
}