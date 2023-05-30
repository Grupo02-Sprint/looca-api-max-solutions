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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

//        Memoria memoria = looca.getMemoria();
//        Double porcentagemUsoMemoria = (memoria.getEmUso().doubleValue() / memoria.getTotal().doubleValue()) * 100.0;
//        LocalDateTime dataHoraAtual = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedDateTime = dataHoraAtual.format(formatter);
        ProcessoGrupo grupoDeProcesso = looca.getGrupoDeProcessos();
        List<Processo> processos = grupoDeProcesso.getProcessos();

        for (Processo processo : processos) {
            int processoId = processo.getPid();

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
