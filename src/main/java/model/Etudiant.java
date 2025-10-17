/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

public class Etudiant implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int idEtudiant;
    private String nom;
    private String prenom;
    private String origineScolaire;
    
    public Etudiant() {}
    
    public Etudiant(String nom, String prenom, String origineScolaire) {
        this.nom = nom;
        this.prenom = prenom;
        this.origineScolaire = origineScolaire;
    }
    
    public Etudiant(int idEtudiant, String nom, String prenom, String origineScolaire) {
        this.idEtudiant = idEtudiant;
        this.nom = nom;
        this.prenom = prenom;
        this.origineScolaire = origineScolaire;
    }
    
    // Getters et Setters
    public int getIdEtudiant() {
        return idEtudiant;
    }
    
    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getOrigineScolaire() {
        return origineScolaire;
    }
    
    public void setOrigineScolaire(String origineScolaire) {
        this.origineScolaire = origineScolaire;
    }
    
    @Override
    public String toString() {
        return "Etudiant{" +
                "idEtudiant=" + idEtudiant +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", origineScolaire='" + origineScolaire + '\'' +
                '}';
    }
}