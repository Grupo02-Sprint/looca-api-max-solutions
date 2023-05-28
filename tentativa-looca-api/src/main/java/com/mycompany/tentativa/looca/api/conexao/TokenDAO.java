/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tentativa.looca.api.conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Cesar
 */
public class TokenDAO {
    private Connection con;

    // Construtor que recebe a conexão com o banco de dados
    public TokenDAO() {
        this.con =new Conexao().conectaBD();
    }

    public String getToken() throws SQLException {
        String token = null;

        String query = "SELECT token FROM tokenSlack WHERE id = 1";
        PreparedStatement statement = con.prepareStatement(query);
        
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            token = resultSet.getString("token");
        }

        return token;
    }
}
