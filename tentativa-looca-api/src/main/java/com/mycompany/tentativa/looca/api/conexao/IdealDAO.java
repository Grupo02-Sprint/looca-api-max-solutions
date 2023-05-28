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
public class IdealDAO {

    private Connection con;

    // Construtor que recebe a conexão com o banco de dados
    public IdealDAO() {
        this.con =new Conexao().conectaBD();
    }

    public Double getLimiteToleravel(int fkLoja, int fkComponente) throws SQLException {
        Double limiteToleravel = null;

        String query = "SELECT limite_toleravel FROM ideal WHERE fk_loja = ? AND fk_componente = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, fkLoja);
        statement.setInt(2, fkComponente);
        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            limiteToleravel = resultSet.getDouble("limite_toleravel");
        }

        return limiteToleravel;
    }
}
