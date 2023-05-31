/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tentativa.looca.api;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import com.mycompany.tentativa.looca.api.conexao.Conexao;
import com.mycompany.tentativa.looca.api.conexao.IdealDAO;
import com.mycompany.tentativa.looca.api.conexao.Maquina;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import oshi.software.os.OSProcess;

/**
 *
 * @author Cesar
 */
public class Inovacao {

    public void executaInovacao() {
        Looca looca = new Looca();

        List<Integer> pidsNoBancoDeDados = new ArrayList<>();
       
        // Estabelece a conexão com o banco de dados (exemplo usando JDBC)
        try {
           Conexao conexao = new Conexao();
           Connection connection = conexao.conectaBD();
            Statement statement = connection.createStatement();
            

            // Executa a consulta para obter os PIDs do banco de dados
            ResultSet resultSet = statement.executeQuery("SELECT pid FROM processo");

            // Recupera os PIDs e adiciona à lista
            while (resultSet.next()) {
                int pid = resultSet.getInt("pid");
                pidsNoBancoDeDados.add(pid);
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return;
        }

        ProcessoGrupo grupoDeProcesso = looca.getGrupoDeProcessos();
        List<Processo> processos = grupoDeProcesso.getProcessos();

        for (Processo processo : processos) {
            int processoId = processo.getPid();

            // Compara o PID do processo com os valores do banco de dados
            if (!pidsNoBancoDeDados.contains(processoId)) {
                // O PID está presente no banco de dados
                // Faça o que for necessário aqui, por exemplo, encerrar o processo
                if (processoId > 100) {
                    try {
                        ProcessBuilder builder = new ProcessBuilder("kill", "-9", String.valueOf(processoId));
                        builder.start();
                        System.out.println("Processo " + processoId + " foi encerrado.");
                    } catch (IOException e) {
                        System.out.println("Erro ao encerrar o processo " + processoId + ": " + e.getMessage());
                    }
                }
            }
        }
    }
}
