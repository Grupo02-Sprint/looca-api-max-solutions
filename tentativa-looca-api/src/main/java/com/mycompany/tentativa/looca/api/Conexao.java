/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tentativa.looca.api;

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
    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
       dataSource.setUrl("jdbc:mysql://localhost:3306/maxsolutions"); // trocar o localhost:3306 pelo endereço do banco e o tecflix pelo nome do banco
        
        dataSource.setUsername("root"); //Usuario do banco
        
        dataSource.setPassword("Sccp1910@"); //Senha do banco
    
      this.conexaoDoBanco= new JdbcTemplate(dataSource);
            
    }
    
   public JdbcTemplate getConexaoDoBanco(){
       return conexaoDoBanco;
   }
    
}
