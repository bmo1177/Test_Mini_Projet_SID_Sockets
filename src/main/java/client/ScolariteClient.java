/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package client;

import config.DatabaseConfig;
import model.*;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class ScolariteClient {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Scanner scanner;
    
    public ScolariteClient() {
        scanner = new Scanner(System.in);
    }
    
    public void connecter(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        System.out.println("✓ Connecté au serveur " + host + ":" + port + "\n");
    }
    
    public void deconnecter() {
        try {
            Request request = new Request("DISCONNECT");
            envoyerRequete(request);
            
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("\n✓ Déconnexion réussie");
        } catch (IOException e) {
            System.err.println("Erreur déconnexion: " + e.getMessage());
        }
    }
    
    private Response envoyerRequete(Request request) throws IOException {
        try {
            out.writeObject(request);
            out.flush();
            return (Response) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Erreur de réception: " + e.getMessage());
        }
    }
    
    public void afficherMenu() {
        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   SYSTÈME DE GESTION SCOLARITÉ EISTI      ║");
        System.out.println("╠════════════════════════════════════════════╣");
        System.out.println("║  GESTION ÉTUDIANTS                         ║");
        System.out.println("║  1. Ajouter un étudiant                    ║");
        System.out.println("║  2. Modifier un étudiant                   ║");
        System.out.println("║  3. Supprimer un étudiant                  ║");
        System.out.println("║  4. Lister tous les étudiants              ║");
        System.out.println("║  5. Rechercher un étudiant                 ║");
        System.out.println("║  6. Consulter un étudiant                  ║");
        System.out.println("║                                            ║");
        System.out.println("║  GESTION INSCRIPTIONS                      ║");
        System.out.println("║  7. Inscrire un étudiant à un programme    ║");
        System.out.println("║  8. Consulter les inscriptions             ║");
        System.out.println("║  9. Mettre à jour moyenne et statut        ║");
        System.out.println("║                                            ║");
        System.out.println("║  0. Quitter                                ║");
        System.out.println("╚════════════════════════════════════════════╝");
        System.out.print("Votre choix: ");
    }
    
    public void demarrer() {
        boolean continuer = true;
        
        while (continuer) {
            afficherMenu();
            
            try {
                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer le retour à la ligne
                
                switch (choix) {
                    case 1:
                        ajouterEtudiant();
                        break;
                    case 2:
                        modifierEtudiant();
                        break;
                    case 3:
                        supprimerEtudiant();
                        break;
                    case 4:
                        listerEtudiants();
                        break;
                    case 5:
                        rechercherEtudiants();
                        break;
                    case 6:
                        consulterEtudiant();
                        break;
                    case 7:
                        ajouterInscription();
                        break;
                    case 8:
                        listerInscriptions();
                        break;
                    case 9:
                        updateMoyenne();
                        break;
                    case 0:
                        continuer = false;
                        deconnecter();
                        break;
                    default:
                        System.out.println("⚠ Choix invalide!");
                }
            } catch (Exception e) {
                System.err.println("⚠ Erreur: " + e.getMessage());
                scanner.nextLine(); // Vider le buffer
            }
        }
    }
    
    private void ajouterEtudiant() throws IOException {
        System.out.println("\n=== AJOUTER UN ÉTUDIANT ===");
        
        System.out.print("Nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Prénom: ");
        String prenom = scanner.nextLine();
        
        System.out.print("Origine scolaire (DUT/CPI/CPGE): ");
        String origine = scanner.nextLine();
        
        Request request = new Request("AJOUTER_ETUDIANT");
        request.addData("nom", nom);
        request.addData("prenom", prenom);
        request.addData("origine", origine);
        
        Response response = envoyerRequete(request);
        
        if (response.isSuccess()) {
            System.out.println("✓ " + response.getMessage());
        } else {
            System.out.println("✗ " + response.getMessage());
        }
    }
    
    private void modifierEtudiant() throws IOException {
        System.out.println("\n=== MODIFIER UN ÉTUDIANT ===");
        
        System.out.print("ID de l'étudiant: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Nouveau nom: ");
        String nom = scanner.nextLine();
        
        System.out.print("Nouveau prénom: ");
        String prenom = scanner.nextLine();
        
        System.out.print("Nouvelle origine scolaire: ");
        String origine = scanner.nextLine();
        
        Request request = new Request("MODIFIER_ETUDIANT");
        request.addData("id", id);
        request.addData("nom", nom);
        request.addData("prenom", prenom);
        request.addData("origine", origine);
        
        Response response = envoyerRequete(request);
        System.out.println(response.isSuccess() ? "✓ " : "✗ " + response.getMessage());
    }
    
    private void supprimerEtudiant() throws IOException {
        System.out.println("\n=== SUPPRIMER UN ÉTUDIANT ===");
        
        System.out.print("ID de l'étudiant: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Confirmer la suppression? (O/N): ");
        String confirm = scanner.nextLine();
        
        if (confirm.equalsIgnoreCase("O")) {
            Request request = new Request("SUPPRIMER_ETUDIANT");
            request.addData("id", id);
            
            Response response = envoyerRequete(request);
            System.out.println(response.isSuccess() ? "✓ " : "✗ " + response.getMessage());
        } else {
            System.out.println("✗ Suppression annulée");
        }
    }
    
    private void listerEtudiants() throws IOException {
        System.out.println("\n=== LISTE DES ÉTUDIANTS ===");
        
        Request request = new Request("LISTER_ETUDIANTS");
        Response response = envoyerRequete(request);
        
        if (response.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Etudiant> etudiants = (List<Etudiant>) response.getData();
            
            if (etudiants.isEmpty()) {
                System.out.println("Aucun étudiant enregistré.");
            } else {
                System.out.println("\n┌─────┬─────────────────────┬─────────────────────┬────────────┐");
                System.out.println("│ ID  │ Nom                 │ Prénom              │ Origine    │");
                System.out.println("├─────┼─────────────────────┼─────────────────────┼────────────┤");
                
                for (Etudiant e : etudiants) {
                    System.out.printf("│ %-3d │ %-19s │ %-19s │ %-10s │%n",
                        e.getIdEtudiant(),
                        e.getNom().length() > 19 ? e.getNom().substring(0, 19) : e.getNom(),
                        e.getPrenom().length() > 19 ? e.getPrenom().substring(0, 19) : e.getPrenom(),
                        e.getOrigineScolaire().length() > 10 ? e.getOrigineScolaire().substring(0, 10) : e.getOrigineScolaire()
                    );
                }
                System.out.println("└─────┴─────────────────────┴─────────────────────┴────────────┘");
                System.out.println("\nTotal: " + etudiants.size() + " étudiants");
            }
        } else {
            System.out.println("✗ " + response.getMessage());
        }
    }
    
    private void rechercherEtudiants() throws IOException {
        System.out.println("\n=== RECHERCHER UN ÉTUDIANT ===");
        
        System.out.print("Nom ou prénom à rechercher: ");
        String recherche = scanner.nextLine();
        
        Request request = new Request("RECHERCHER_ETUDIANT");
        request.addData("recherche", recherche);
        
        Response response = envoyerRequete(request);
        
        if (response.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Etudiant> etudiants = (List<Etudiant>) response.getData();
            
            if (etudiants.isEmpty()) {
                System.out.println("✗ Aucun résultat trouvé.");
            } else {
                System.out.println("\n┌─────┬─────────────────────┬─────────────────────┬────────────┐");
                System.out.println("│ ID  │ Nom                 │ Prénom              │ Origine    │");
                System.out.println("├─────┼─────────────────────┼─────────────────────┼────────────┤");
                
                for (Etudiant e : etudiants) {
                    System.out.printf("│ %-3d │ %-19s │ %-19s │ %-10s │%n",
                        e.getIdEtudiant(), e.getNom(), e.getPrenom(), e.getOrigineScolaire());
                }
                System.out.println("└─────┴─────────────────────┴─────────────────────┴────────────┘");
                System.out.println("\nRésultats: " + etudiants.size());
            }
        } else {
            System.out.println("✗ " + response.getMessage());
        }
    }
    
    private void consulterEtudiant() throws IOException {
        System.out.println("\n=== CONSULTER UN ÉTUDIANT ===");
        
        System.out.print("ID de l'étudiant: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        
        Request request = new Request("GET_ETUDIANT");
        request.addData("id", id);
        
        Response response = envoyerRequete(request);
        
        if (response.isSuccess()) {
            Etudiant e = (Etudiant) response.getData();
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║     INFORMATIONS ÉTUDIANT              ║");
            System.out.println("╠════════════════════════════════════════╣");
            System.out.printf("║ ID:               %-20d ║%n", e.getIdEtudiant());
            System.out.printf("║ Nom:              %-20s ║%n", e.getNom());
            System.out.printf("║ Prénom:           %-20s ║%n", e.getPrenom());
            System.out.printf("║ Origine scolaire: %-20s ║%n", e.getOrigineScolaire());
            System.out.println("╚════════════════════════════════════════╝");
        } else {
            System.out.println("✗ " + response.getMessage());
        }
    }
    
    private void ajouterInscription() throws IOException {
        System.out.println("\n=== INSCRIRE UN ÉTUDIANT ===");
        
        System.out.print("ID de l'étudiant: ");
        int idEtudiant = scanner.nextInt();
        
        System.out.print("ID du programme: ");
        int idProgramme = scanner.nextInt();
        
        System.out.print("ID de l'année scolaire: ");
        int idAnnee = scanner.nextInt();
        scanner.nextLine();
        
        Request request = new Request("AJOUTER_INSCRIPTION");
        request.addData("idEtudiant", idEtudiant);
        request.addData("idProgramme", idProgramme);
        request.addData("idAnnee", idAnnee);
        
        Response response = envoyerRequete(request);
        System.out.println(response.isSuccess() ? "✓ " : "✗ " + response.getMessage());
    }
    
    private void listerInscriptions() throws IOException {
        System.out.println("\n=== CONSULTER LES INSCRIPTIONS ===");
        System.out.println("1. Toutes les inscriptions");
        System.out.println("2. Inscriptions d'un étudiant");
        System.out.print("Choix: ");
        
        int choix = scanner.nextInt();
        scanner.nextLine();
        
        Request request = new Request("LISTER_INSCRIPTIONS");
        
        if (choix == 2) {
            System.out.print("ID de l'étudiant: ");
            int idEtudiant = scanner.nextInt();
            scanner.nextLine();
            request.addData("idEtudiant", idEtudiant);
        }
        
        Response response = envoyerRequete(request);
        
        if (response.isSuccess()) {
            @SuppressWarnings("unchecked")
            List<Inscription> inscriptions = (List<Inscription>) response.getData();
            
            if (inscriptions.isEmpty()) {
                System.out.println("✗ Aucune inscription trouvée.");
            } else {
                System.out.println("\n┌──────────────────┬──────────────────┬─────────────────────────────┬──────────┬────────────┐");
                System.out.println("│ Étudiant         │ Programme        │ Année                       │ Moyenne  │ Statut     │");
                System.out.println("├──────────────────┼──────────────────┼─────────────────────────────┼──────────┼────────────┤");
                
                for (Inscription insc : inscriptions) {
                    String etudiant = insc.getNomEtudiant() + " " + insc.getPrenomEtudiant();
                    String moyenneStr = insc.getMoyenneGenerale() > 0 ? 
                        String.format("%.2f", insc.getMoyenneGenerale()) : "-";
                    
                    System.out.printf("│ %-16s │ %-16s │ %-27s │ %-8s │ %-10s │%n",
                        etudiant.length() > 16 ? etudiant.substring(0, 16) : etudiant,
                        insc.getNomProgramme().length() > 16 ? insc.getNomProgramme().substring(0, 16) : insc.getNomProgramme(),
                        insc.getAnnee(),
                        moyenneStr,
                        insc.getStatut()
                    );
                }
                System.out.println("└──────────────────┴──────────────────┴─────────────────────────────┴──────────┴────────────┘");
                System.out.println("\nTotal: " + inscriptions.size() + " inscriptions");
            }
        } else {
            System.out.println("✗ " + response.getMessage());
        }
    }
    
    private void updateMoyenne() throws IOException {
        System.out.println("\n=== METTRE À JOUR MOYENNE ET STATUT ===");
        
        System.out.print("ID de l'étudiant: ");
        int idEtudiant = scanner.nextInt();
        
        System.out.print("ID du programme: ");
        int idProgramme = scanner.nextInt();
        
        System.out.print("ID de l'année: ");
        int idAnnee = scanner.nextInt();
        
        System.out.print("Moyenne générale: ");
        double moyenne = scanner.nextDouble();
        scanner.nextLine();
        
        System.out.print("Statut (En cours/Passage/Redoublant/Exclu): ");
        String statut = scanner.nextLine();
        
        Request request = new Request("UPDATE_MOYENNE");
        request.addData("idEtudiant", idEtudiant);
        request.addData("idProgramme", idProgramme);
        request.addData("idAnnee", idAnnee);
        request.addData("moyenne", moyenne);
        request.addData("statut", statut);
        
        Response response = envoyerRequete(request);
        System.out.println(response.isSuccess() ? "✓ " : "✗ " + response.getMessage());
    }
    
    public static void main(String[] args) {
        try {
            String host = DatabaseConfig.getServerHost();
            int port = DatabaseConfig.getServerPort();
            
            ScolariteClient client = new ScolariteClient();
            client.connecter(host, port);
            client.demarrer();
            
        } catch (IOException e) {
            System.err.println("✗ Erreur de connexion: " + e.getMessage());
            System.err.println("Assurez-vous que le serveur est démarré.");
        }
    }
}