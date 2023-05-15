/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tentativa.looca.api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Cesar
 */
public class Conexao {

    private JdbcTemplate conexaoDoBanco;

    public Conexao() {
        BasicDataSource dataSource = new BasicDataSource();
      
             dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

             dataSource.setUrl("jdbc:sqlserver://Sprint2Semestre.database.windows.net:1433;database=max-solutions;user=admin-max-solutions;password={#Gfgrupo2};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;");

        this.conexaoDoBanco = new JdbcTemplate(dataSource);

    }

    public JdbcTemplate getConexaoDoBanco() {
        return conexaoDoBanco;
    }
    
     public Connection conectaBD() {
        Connection conn = null;

        try {

            String url = "jdbc:sqlserver://Sprint2Semestre.database.windows.net:1433;database=max-solutions;user=admin-max-solutions;password={#Gfgrupo2};encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
            conn = DriverManager.getConnection(url);
        } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "ConexaoDAO" + erro.getMessage());
        }
        return conn;
    }
}
