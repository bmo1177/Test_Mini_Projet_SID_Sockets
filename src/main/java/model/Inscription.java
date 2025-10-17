/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

public class Inscription implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int idEtudiant;
    private int idProgramme;
    private int idAnnee;
    private double moyenneGenerale;
    private String statut;
    
    // Informations additionnelles pour l'affichage
    private String nomEtudiant;
    private String prenomEtudiant;
    private String nomProgramme;
    private String annee;
    
    public Inscription() {}
    
    public Inscription(int idEtudiant, int idProgramme, int idAnnee) {
        this.idEtudiant = idEtudiant;
        this.idProgramme = idProgramme;
        this.idAnnee = idAnnee;
        this.statut = "En cours";
    }
    
    // Getters et Setters
    public int getIdEtudiant() {
        return idEtudiant;
    }
    
    public void setIdEtudiant(int idEtudiant) {
        this.idEtudiant = idEtudiant;
    }
    
    public int getIdProgramme() {
        return idProgramme;
    }
    
    public void setIdProgramme(int idProgramme) {
        this.idProgramme = idProgramme;
    }
    
    public int getIdAnnee() {
        return idAnnee;
    }
    
    public void setIdAnnee(int idAnnee) {
        this.idAnnee = idAnnee;
    }
    
    public double getMoyenneGenerale() {
        return moyenneGenerale;
    }
    
    public void setMoyenneGenerale(double moyenneGenerale) {
        this.moyenneGenerale = moyenneGenerale;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public String getNomEtudiant() {
        return nomEtudiant;
    }
    
    public void setNomEtudiant(String nomEtudiant) {
        this.nomEtudiant = nomEtudiant;
    }
    
    public String getPrenomEtudiant() {
        return prenomEtudiant;
    }
    
    public void setPrenomEtudiant(String prenomEtudiant) {
        this.prenomEtudiant = prenomEtudiant;
    }
    
    public String getNomProgramme() {
        return nomProgramme;
    }
    
    public void setNomProgramme(String nomProgramme) {
        this.nomProgramme = nomProgramme;
    }
    
    public String getAnnee() {
        return annee;
    }
    
    public void setAnnee(String annee) {
        this.annee = annee;
    }
    
    @Override
    public String toString() {
        return "Inscription{" +
                "idEtudiant=" + idEtudiant +
                ", idProgramme=" + idProgramme +
                ", idAnnee=" + idAnnee +
                ", moyenneGenerale=" + moyenneGenerale +
                ", statut='" + statut + '\'' +
                ", nomEtudiant='" + nomEtudiant + '\'' +
                ", prenomEtudiant='" + prenomEtudiant + '\'' +
                ", nomProgramme='" + nomProgramme + '\'' +
                ", annee='" + annee + '\'' +
                '}';
    }
}