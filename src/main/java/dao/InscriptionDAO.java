/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import config.DatabaseConfig;
import model.Inscription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InscriptionDAO {
    
    public boolean insererInscription(Inscription inscription) throws SQLException {
        String sql = "INSERT INTO inscription (id_etudiant, id_programme, id_annee, statut) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, inscription.getIdEtudiant());
            stmt.setInt(2, inscription.getIdProgramme());
            stmt.setInt(3, inscription.getIdAnnee());
            stmt.setString(4, inscription.getStatut());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public List<Inscription> getInscriptionsByEtudiant(int idEtudiant) throws SQLException {
        String sql = "SELECT i.*, e.nom, e.prenom, p.nom_programme, a.annee " +
                     "FROM inscription i " +
                     "JOIN etudiant e ON i.id_etudiant = e.id_etudiant " +
                     "JOIN programme p ON i.id_programme = p.id_programme " +
                     "JOIN annee_scolaire a ON i.id_annee = a.id_annee " +
                     "WHERE i.id_etudiant = ?";
        List<Inscription> inscriptions = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEtudiant);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Inscription insc = new Inscription();
                insc.setIdEtudiant(rs.getInt("id_etudiant"));
                insc.setIdProgramme(rs.getInt("id_programme"));
                insc.setIdAnnee(rs.getInt("id_annee"));
                insc.setMoyenneGenerale(rs.getDouble("moyenne_generale"));
                insc.setStatut(rs.getString("statut"));
                insc.setNomEtudiant(rs.getString("nom"));
                insc.setPrenomEtudiant(rs.getString("prenom"));
                insc.setNomProgramme(rs.getString("nom_programme"));
                insc.setAnnee(rs.getString("annee"));
                inscriptions.add(insc);
            }
        }
        return inscriptions;
    }
    
    public List<Inscription> getToutesInscriptions() throws SQLException {
        String sql = "SELECT i.*, e.nom, e.prenom, p.nom_programme, a.annee " +
                     "FROM inscription i " +
                     "JOIN etudiant e ON i.id_etudiant = e.id_etudiant " +
                     "JOIN programme p ON i.id_programme = p.id_programme " +
                     "JOIN annee_scolaire a ON i.id_annee = a.id_annee " +
                     "ORDER BY a.annee DESC, e.nom, e.prenom";
        List<Inscription> inscriptions = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Inscription insc = new Inscription();
                insc.setIdEtudiant(rs.getInt("id_etudiant"));
                insc.setIdProgramme(rs.getInt("id_programme"));
                insc.setIdAnnee(rs.getInt("id_annee"));
                insc.setMoyenneGenerale(rs.getDouble("moyenne_generale"));
                insc.setStatut(rs.getString("statut"));
                insc.setNomEtudiant(rs.getString("nom"));
                insc.setPrenomEtudiant(rs.getString("prenom"));
                insc.setNomProgramme(rs.getString("nom_programme"));
                insc.setAnnee(rs.getString("annee"));
                inscriptions.add(insc);
            }
        }
        return inscriptions;
    }
    
    public boolean updateStatutEtMoyenne(int idEtudiant, int idProgramme, int idAnnee, 
                                         double moyenne, String statut) throws SQLException {
        String sql = "UPDATE inscription SET moyenne_generale = ?, statut = ? " +
                     "WHERE id_etudiant = ? AND id_programme = ? AND id_annee = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, moyenne);
            stmt.setString(2, statut);
            stmt.setInt(3, idEtudiant);
            stmt.setInt(4, idProgramme);
            stmt.setInt(5, idAnnee);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    public boolean supprimerInscription(int idEtudiant, int idProgramme, int idAnnee) throws SQLException {
        String sql = "DELETE FROM inscription WHERE id_etudiant = ? AND id_programme = ? AND id_annee = ?";
        
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idEtudiant);
            stmt.setInt(2, idProgramme);
            stmt.setInt(3, idAnnee);
            
            return stmt.executeUpdate() > 0;
        }
    }
}