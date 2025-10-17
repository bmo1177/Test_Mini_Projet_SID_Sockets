/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static DatabaseConfig instance;
    private String url;
    private String username;
    private String password;
    private String driver;
    
    private DatabaseConfig() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));
            
            this.url = props.getProperty("db.url");
            this.username = props.getProperty("db.username");
            this.password = props.getProperty("db.password");
            this.driver = props.getProperty("db.driver");
            
            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors du chargement de la configuration", e);
        }
    }
    
    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    public static int getServerPort() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));
            return Integer.parseInt(props.getProperty("server.port", "5000"));
        } catch (IOException e) {
            return 5000;
        }
    }
    
    public static String getServerHost() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("db.properties"));
            return props.getProperty("server.host", "localhost");
        } catch (IOException e) {
            return "localhost";
        }
    }
}