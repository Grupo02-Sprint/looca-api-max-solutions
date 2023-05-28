/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tentativa.looca.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cesar
 */
public class Log {
     public void login(ResultSet usuario,Boolean rdusuario) {

        File login = new File("log.txt");

        //throws IOException e try e catch
        if (!login.exists()) {
            try {
                login.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(TelaDeLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        String frase = "Login realizado!";

        if (!rdusuario) {
            frase = "O login falhou!";
        }
        LocalDateTime dataHora = LocalDateTime.now();
        
        List<String> lista = new ArrayList<>();
        lista.add(
                dataHora.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss ")) + frase);

       
        try {
            Files.write(Paths.get(login.getPath()), lista, StandardOpenOption.APPEND);
        } catch (IOException ex) {
            Logger.getLogger(TelaDeLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
