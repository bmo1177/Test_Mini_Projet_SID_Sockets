# Système de Gestion Scolarité EISTI

Application client-serveur en Java avec sockets pour la gestion de la scolarité des étudiants de l'EISTI.

## 📋 Prérequis

- Java JDK 8 ou supérieur
- MySQL 5.7 ou supérieur
- MySQL Connector/J (JDBC Driver)

## 📁 Structure du projet
```
projet-eisti/
│
├── src/
│   └── com/
│       └── eisti/
│           ├── config/
│           │   └── DatabaseConfig.java
│           ├── model/
│           │   ├── Etudiant.java
│           │   ├── Inscription.java
│           │   ├── Request.java
│           │   └── Response.java
│           ├── dao/
│           │   ├── EtudiantDAO.java
│           │   └── InscriptionDAO.java
│           ├── server/
│           │   └── ScolariteServer.java
│           └── client/
│               └── ScolariteClient.java
│
├── db.properties
├── database_schema.sql
└── README.md
```
## 🔧 Installation

### 1. Configuration de la base de données

```bash
# Connexion à MySQL
mysql -u root -p

# Exécution du script SQL
source database_schema.sql
```

Ou Utlisez MySQL Shell ou MySQL Workbench pour cette étape.


### 2. Configuration de l'application

Modifiez le fichier `db.properties` selon votre configuration:

```properties
db.url=jdbc:mysql://localhost:3306/eisti_scolarite?useSSL=false&serverTimezone=UTC
db.username=root
db.password=<votre_mot_de_passe>
db.driver=com.mysql.cj.jdbc.Driver

server.port=5050
server.host=localhost
```

### 3. Ajout du driver MySQL

Téléchargez MySQL Connector/J depuis: https://dev.mysql.com/downloads/connector/j/

Ajoutez le JAR au classpath de votre projet.

## 🚀 Compilation et Exécution

### Compilation

```bash
# Compilation de tous les fichiers
javac -d bin -cp .:mysql-connector-java-8.0.xx.jar src/com/**/*.java
```

### Démarrage du serveur

```bash
# Terminal 1 - Démarrer le serveur
java -cp bin:mysql-connector-java-8.0.xx.jar server.ScolariteServer
```

### Démarrage du client

```bash
# Terminal 2 - Démarrer le client
java -cp bin:mysql-connector-java-8.0.xx.jar client.ScolariteClient
```

## 📖 Fonctionnalités

### Gestion des Étudiants

- ✅ Ajouter un étudiant
- ✅ Modifier un étudiant
- ✅ Supprimer un étudiant
- ✅ Lister tous les étudiants
- ✅ Rechercher un étudiant
- ✅ Consulter les détails d'un étudiant

### Gestion des Inscriptions

- ✅ Inscrire un étudiant à un programme
- ✅ Consulter les inscriptions
- ✅ Mettre à jour la moyenne et le statut

## 🗄️ Modèle de données

Le système gère:

- **Étudiants**: Informations personnelles et origine scolaire
- **Programmes**: ING1_TC, ING2_TC, ING2_GI, ING3_ISIN, etc.
- **Inscriptions**: Liaison étudiant-programme-année avec moyenne et statut
- **Matières**: Contenu des programmes avec objectifs
- **Notes**: Épreuves et projets
- **Prérequis**: Dépendances entre programmes

## 🔌 Architecture Socket

### Protocole de communication

Le client envoie des objets `Request`:

```java
Request request = new Request("ACTION");
request.addData("key", value);
```

Le serveur répond avec des objets `Response`:

```java
Response response = new Response(success, message, data);
```

### Actions disponibles

- `AJOUTER_ETUDIANT`: Ajouter un nouvel étudiant
- `MODIFIER_ETUDIANT`: Modifier les informations d'un étudiant
- `SUPPRIMER_ETUDIANT`: Supprimer un étudiant
- `LISTER_ETUDIANTS`: Récupérer tous les étudiants
- `RECHERCHER_ETUDIANT`: Rechercher par nom/prénom
- `GET_ETUDIANT`: Obtenir un étudiant par ID
- `AJOUTER_INSCRIPTION`: Inscrire à un programme
- `LISTER_INSCRIPTIONS`: Lister les inscriptions
- `UPDATE_MOYENNE`: Mettre à jour moyenne et statut
- `DISCONNECT`: Déconnexion

## 📊 Diagrammes

### Diagramme de séquence

Le diagramme montre l'interaction Client → Server → DAO → Database pour les opérations CRUD.

### MCD (Modèle Conceptuel de Données)

Le MCD illustre les relations entre:

- Étudiant ↔ Inscription ↔ Programme
- Programme ↔ Matière
- Étudiant ↔ Notes (Épreuves/Projets)
- Programme ↔ Prérequis

## 🛡️ Gestion des erreurs

- Validation des données côté serveur
- Gestion des exceptions SQL
- Messages d'erreur descriptifs
- Transactions pour l'intégrité des données

## 📝 Exemples d'utilisation

### Ajouter un étudiant

```
Nom: Dupont
Prénom: Jean
Origine scolaire: CPGE
```

### Inscrire un étudiant

```
ID étudiant: 1
ID programme: 1 (ING1_TC)
ID année: 1 (2025/2026)
```

### Mettre à jour la moyenne

```
ID étudiant: 1
ID programme: 1
ID année: 1
Moyenne: 14.5
Statut: Passage
```

## 🔐 Sécurité

- Utilisation de PreparedStatement pour éviter les injections SQL
- Validation des données d'entrée
- Gestion sécurisée des connexions

## 🐛 Dépannage

### "Connection refused"

- Vérifiez que le serveur est démarré
- Vérifiez le port dans db.properties - changer vers ex: 5050 ou 5151 

### "Access denied for user"

- Vérifiez les identifiants MySQL dans db.properties
- Vérifiez le mot de passe de la base de données dans votre Setup MySQL
- Assurez-vous que l'utilisateur a les droits sur la base

### "ClassNotFoundException: com.mysql.cj.jdbc.Driver"

- Ajoutez le driver MySQL au classpath

## 👥 Auteurs

Projet réalisé dans le cadre du TD1 - Module SID Université Ibn Khaldoun - Tiaret

## 📄 Licence

Ce projet est à usage éducatif.
