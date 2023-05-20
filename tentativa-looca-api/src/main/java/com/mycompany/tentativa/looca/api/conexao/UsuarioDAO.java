/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tentativa.looca.api.conexao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Cesar
 */
public class UsuarioDAO {
    Connection con;
    
    public ResultSet usuarioEntra(Maquina maquina){
        con = new Conexao().conectaBD();
        
        try {
            
            String sql = "select * from maquina join loja on id_loja = fk_loja where patrimonio = ? and senha = ?";
            
            PreparedStatement pprStmnt = con.prepareStatement(sql);
            pprStmnt.setString(1, maquina.getPatrimonio());
            pprStmnt.setString(2, maquina.getSenha());
            
            ResultSet result = pprStmnt.executeQuery();
            return result;
            
    } catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "UsuarioDAO:" + erro);
            return null;
        }
    }
}
