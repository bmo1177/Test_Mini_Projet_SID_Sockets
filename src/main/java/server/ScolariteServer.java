/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package server;


import config.DatabaseConfig;
import dao.EtudiantDAO;
import dao.InscriptionDAO;
import model.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ScolariteServer {
    private ServerSocket serverSocket;
    private EtudiantDAO etudiantDAO;
    private InscriptionDAO inscriptionDAO;
    
    public ScolariteServer() {
        this.etudiantDAO = new EtudiantDAO();
        this.inscriptionDAO = new InscriptionDAO();
    }
    
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("✓ Serveur démarré sur le port " + port);
            System.out.println("✓ En attente de connexions...\n");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("→ Nouvelle connexion: " + clientSocket.getInetAddress());
                
                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println("Erreur serveur: " + e.getMessage());
        }
    }
    
    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        
        @Override
        public void run() {
            try {
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                
                Object obj;
                while ((obj = in.readObject()) != null) {
                    if (obj instanceof Request) {
                        Request request = (Request) obj;
                        System.out.println("  Requête reçue: " + request.getAction());
                        
                        Response response = traiterRequete(request);
                        out.writeObject(response);
                        out.flush();
                        
                        if ("DISCONNECT".equals(request.getAction())) {
                            break;
                        }
                    }
                }
            } catch (EOFException e) {
                System.out.println("← Client déconnecté");
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erreur communication: " + e.getMessage());
            } finally {
                fermerConnexion();
            }
        }
        
        private Response traiterRequete(Request request) {
            try {
                switch (request.getAction()) {
                    case "AJOUTER_ETUDIANT":
                        return ajouterEtudiant(request);
                    
                    case "MODIFIER_ETUDIANT":
                        return modifierEtudiant(request);
                    
                    case "SUPPRIMER_ETUDIANT":
                        return supprimerEtudiant(request);
                    
                    case "LISTER_ETUDIANTS":
                        return listerEtudiants();
                    
                    case "RECHERCHER_ETUDIANT":
                        return rechercherEtudiants(request);
                    
                    case "GET_ETUDIANT":
                        return getEtudiant(request);
                    
                    case "AJOUTER_INSCRIPTION":
                        return ajouterInscription(request);
                    
                    case "LISTER_INSCRIPTIONS":
                        return listerInscriptions(request);
                    
                    case "UPDATE_MOYENNE":
                        return updateMoyenne(request);
                    
                    case "DISCONNECT":
                        return new Response(true, "Déconnexion réussie");
                    
                    default:
                        return new Response(false, "Action non reconnue");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new Response(false, "Erreur serveur: " + e.getMessage());
            }
        }
        
        private Response ajouterEtudiant(Request request) {
            try {
                String nom = (String) request.getData("nom");
                String prenom = (String) request.getData("prenom");
                String origine = (String) request.getData("origine");
                
                Etudiant etudiant = new Etudiant(nom, prenom, origine);
                int id = etudiantDAO.insererEtudiant(etudiant);
                etudiant.setIdEtudiant(id);
                
                return new Response(true, "Étudiant ajouté avec succès (ID: " + id + ")", etudiant);
            } catch (SQLException e) {
                return new Response(false, "Erreur d'ajout: " + e.getMessage());
            }
        }
        
        private Response modifierEtudiant(Request request) {
            try {
                int id = (Integer) request.getData("id");
                String nom = (String) request.getData("nom");
                String prenom = (String) request.getData("prenom");
                String origine = (String) request.getData("origine");
                
                Etudiant etudiant = new Etudiant(id, nom, prenom, origine);
                boolean success = etudiantDAO.modifierEtudiant(etudiant);
                
                if (success) {
                    return new Response(true, "Étudiant modifié avec succès");
                } else {
                    return new Response(false, "Étudiant non trouvé");
                }
            } catch (SQLException e) {
                return new Response(false, "Erreur de modification: " + e.getMessage());
            }
        }
        
        private Response supprimerEtudiant(Request request) {
            try {
                int id = (Integer) request.getData("id");
                boolean success = etudiantDAO.supprimerEtudiant(id);
                
                if (success) {
                    return new Response(true, "Étudiant supprimé avec succès");
                } else {
                    return new Response(false, "Étudiant non trouvé");
                }
            } catch (SQLException e) {
                return new Response(false, "Erreur de suppression: " + e.getMessage());
            }
        }
        
        private Response listerEtudiants() {
            try {
                List<Etudiant> etudiants = etudiantDAO.getTousEtudiants();
                return new Response(true, "Liste récupérée (" + etudiants.size() + " étudiants)", etudiants);
            } catch (SQLException e) {
                return new Response(false, "Erreur de récupération: " + e.getMessage());
            }
        }
        
        private Response rechercherEtudiants(Request request) {
            try {
                String recherche = (String) request.getData("recherche");
                List<Etudiant> etudiants = etudiantDAO.rechercherEtudiants(recherche);
                return new Response(true, "Recherche terminée (" + etudiants.size() + " résultats)", etudiants);
            } catch (SQLException e) {
                return new Response(false, "Erreur de recherche: " + e.getMessage());
            }
        }
        
        private Response getEtudiant(Request request) {
            try {
                int id = (Integer) request.getData("id");
                Etudiant etudiant = etudiantDAO.getEtudiantById(id);
                
                if (etudiant != null) {
                    return new Response(true, "Étudiant trouvé", etudiant);
                } else {
                    return new Response(false, "Étudiant non trouvé");
                }
            } catch (SQLException e) {
                return new Response(false, "Erreur: " + e.getMessage());
            }
        }
        
        private Response ajouterInscription(Request request) {
            try {
                int idEtudiant = (Integer) request.getData("idEtudiant");
                int idProgramme = (Integer) request.getData("idProgramme");
                int idAnnee = (Integer) request.getData("idAnnee");
                
                Inscription inscription = new Inscription(idEtudiant, idProgramme, idAnnee);
                boolean success = inscriptionDAO.insererInscription(inscription);
                
                if (success) {
                    return new Response(true, "Inscription enregistrée avec succès");
                } else {
                    return new Response(false, "Échec de l'inscription");
                }
            } catch (SQLException e) {
                return new Response(false, "Erreur d'inscription: " + e.getMessage());
            }
        }
        
        private Response listerInscriptions(Request request) {
            try {
                Integer idEtudiant = (Integer) request.getData("idEtudiant");
                List<Inscription> inscriptions;
                
                if (idEtudiant != null) {
                    inscriptions = inscriptionDAO.getInscriptionsByEtudiant(idEtudiant);
                } else {
                    inscriptions = inscriptionDAO.getToutesInscriptions();
                }
                
                return new Response(true, "Liste récupérée (" + inscriptions.size() + " inscriptions)", inscriptions);
            } catch (SQLException e) {
                return new Response(false, "Erreur: " + e.getMessage());
            }
        }
        
        private Response updateMoyenne(Request request) {
            try {
                int idEtudiant = (Integer) request.getData("idEtudiant");
                int idProgramme = (Integer) request.getData("idProgramme");
                int idAnnee = (Integer) request.getData("idAnnee");
                double moyenne = (Double) request.getData("moyenne");
                String statut = (String) request.getData("statut");
                
                boolean success = inscriptionDAO.updateStatutEtMoyenne(
                    idEtudiant, idProgramme, idAnnee, moyenne, statut);
                
                if (success) {
                    return new Response(true, "Moyenne et statut mis à jour");
                } else {
                    return new Response(false, "Inscription non trouvée");
                }
            } catch (SQLException e) {
                return new Response(false, "Erreur: " + e.getMessage());
            }
        }
        
        private void fermerConnexion() {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                System.err.println("Erreur fermeture: " + e.getMessage());
            }
        }
    }
    
    public static void main(String[] args) {
        int port = DatabaseConfig.getServerPort();
        ScolariteServer server = new ScolariteServer();
        server.start(port);
    }
}