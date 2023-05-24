/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.tentativa.looca.api;

import com.mycompany.tentativa.looca.api.conexao.ConexaoLocal;
import com.mycompany.tentativa.looca.api.conexao.Conexao;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.janelas.Janela;
import com.github.britooo.looca.api.group.janelas.JanelaGrupo;
import com.github.britooo.looca.api.group.memoria.Memoria;
import com.github.britooo.looca.api.group.processador.Processador;
import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import com.github.britooo.looca.api.group.rede.Rede;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.rede.RedeInterfaceGroup;
import com.github.britooo.looca.api.group.rede.RedeParametros;
import com.github.britooo.looca.api.group.sistema.Sistema;
import com.mycompany.tentativa.looca.api.conexao.Maquina;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.TimerTask;
import javax.swing.Timer;

/**
 *
 * @author Cesar
 */
public class LoocaApi {

    private boolean existeDadosFKMaquina(int fkMaquina, int fkComponente, Connection connection) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "SELECT COUNT(*) FROM especificacao WHERE fk_maquina = ? AND fk_componente = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, fkMaquina);
            preparedStatement.setInt(2, fkComponente);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);

                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Fechar os recursos
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    public void demonstraLooca(Maquina m) {
        LocalDateTime dataHoraAtual = LocalDateTime.now();
        Looca looca = new Looca();
        Conexao conexao = new Conexao();
        ConexaoLocal conexaoLocal = new ConexaoLocal();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        JdbcTemplate conLocal = conexaoLocal.getConexaoDoBancoLocal();
        Sistema sistema = looca.getSistema();
        DecimalFormat df = new DecimalFormat("0.00");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = dataHoraAtual.format(formatter);

        sistema.getPermissao();
        sistema.getFabricante();
        sistema.getArquitetura();
        sistema.getInicializado();
        sistema.getSistemaOperacional();

        Memoria memoria = looca.getMemoria();

        DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
        List<Disco> discos = grupoDeDiscos.getDiscos();

        Processador processador = looca.getProcessador();

        ProcessoGrupo grupoDeProcesso = looca.getGrupoDeProcessos();
        List<Processo> processos = grupoDeProcesso.getProcessos();

        Rede rede = looca.getRede();
        RedeParametros redeParametros = rede.getParametros();
        RedeInterfaceGroup gruposDeInterface = rede.getGrupoDeInterfaces();
        List<RedeInterface> interfaces = gruposDeInterface.getInterfaces();

        JanelaGrupo gruposDeJanela = looca.getGrupoDeJanelas();
        List<Janela> janelas = gruposDeJanela.getJanelas();
        List<Janela> janelasVisiveis = gruposDeJanela.getJanelasVisiveis();
        //System.out.println(sistema);
        // System.out.println(memoria);

        for (Disco disco : discos) {
            Double tamanhoDisco = disco.getTamanho().doubleValue() / (1024 * 1024 * 1024);
            // Verificar se já existem dados da fk_maquina na tabela especificacao
            if (!existeDadosFKMaquina(m.getIdMaquina(), 3, conexao.conectaBD())) {
                con.update("INSERT INTO especificacao (fk_maquina,fk_loja,fk_componente,data_ativacao_componente,capacidade) "
                        + "values (?,?,?,?,?)", m.getIdMaquina(), m.getFkEmpresa(), formattedDateTime, tamanhoDisco);
            } else {
                con.update("update especificacao set capacidade= ? where fk_maquina = ? and fk_componente = ?",
                        tamanhoDisco, m.getIdMaquina(), 3);
            }
        }

        Double tamanhoMemoria = memoria.getTotal().doubleValue() / (1024 * 1024 * 1024);
        if (!existeDadosFKMaquina(m.getIdMaquina(), 1, conexao.conectaBD())) {
            con.update("INSERT INTO especificacao (fk_maquina, fk_loja, fk_componente, data_ativacao_componente, capacidade) "
                    + "VALUES (?, ?, ?, ?, ?)",
                    m.getIdMaquina(), m.getFkEmpresa(), 1, formattedDateTime, tamanhoMemoria);
        } else {
            con.update("UPDATE especificacao SET capacidade = ? WHERE fk_maquina = ? AND fk_componente = ?",
                    tamanhoMemoria, m.getIdMaquina(), 1);
        }

        Double frequenciaGigaHertz = processador.getFrequencia() / 1000000000.0;
        if (!existeDadosFKMaquina(m.getIdMaquina(), 2, conexao.conectaBD())) {
            con.update("INSERT INTO especificacao (fk_maquina, fk_loja, fk_componente, data_ativacao_componente, capacidade) "
                    + "VALUES (?, ?, ?, ?, ?)",
                    m.getIdMaquina() / 100000000, m.getFkEmpresa(), 2, formattedDateTime, frequenciaGigaHertz);
        } else {
            con.update("UPDATE especificacao SET capacidade = ? WHERE fk_maquina = ? AND fk_componente = ?",
                    frequenciaGigaHertz, m.getIdMaquina(), 2);
        }

        for (Processo processo : processos) {
//         conLocal.update("Insert into processo (pidProcesso,dtHora,usoCpu,usoMemoria) values"
//                   + " (?,?,?,?);",
//                 processo.getPid(),
//                 dataHoraAtual,
//                 processo.getUsoCpu(),
//                 processo.getUsoMemoria()); //NOI18N
            //  System.out.println(processo);
        }

        //System.out.println(redeParametros);
        for (RedeInterface redeInterface : interfaces) {
            con.update(String.format("Insert into rede (bytes_enviados, bytes_recebidos,nome) values (%d,%d,'%s');",
                    redeInterface.getBytesEnviados(),
                    redeInterface.getBytesEnviados(),
                    redeInterface.getNomeExibicao()));
//            conLocal.update(String.format("Insert into rede (bytes_enviados, bytes_recebidos,nome) values (%d,%d,'%s');",
//                    redeInterface.getBytesEnviados(), 
//                    redeInterface.getBytesEnviados(),
//                    redeInterface.getNomeExibicao()));
            //System.out.println(redeInterface);
        }

        for (Janela janela : janelas) {
            System.out.println(janela);
        }

        for (Janela janelaVisivel : janelasVisiveis) {
            System.out.println(janelaVisivel);
        }

        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                //Porcentagem de uso da memória
                Double porcentagemUsoMemoria = (memoria.getEmUso().doubleValue() / memoria.getTotal().doubleValue()) * 100.0;
                //Definindo novamente o localDateTime
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dataHoraAtual.format(formatter);
                con.update("insert into metrica "
                        + "(captura,dt_hora_captura,fk_maquina,fk_loja,fk_componente,fk_unidade_medida)"
                        + "values(?,?,?,?,?,?)",
                        porcentagemUsoMemoria,
                        formattedDateTime,
                        m.getIdMaquina(),
                        m.getFkEmpresa(),
                        1,
                        4);

                con.update("insert into metrica "
                        + "(captura,dt_hora_captura,fk_maquina,fk_loja,fk_componente,fk_unidade_medida)"
                        + "values(?,?,?,?,?,?)",
                        processador.getUso(),
                        formattedDateTime,
                        m.getIdMaquina(),
                        m.getFkEmpresa(),
                        2,
                        4);
                for (Disco disco : discos) {
                    con.update("insert into metrica "
                            + "(captura,dt_hora_captura,fk_maquina,fk_loja,fk_componente,fk_unidade_medida)"
                            + "values(?,?,?,?,?,?)",
                            disco.getTempoDeTransferencia(),
                            formattedDateTime,
                            m.getIdMaquina(),
                            m.getFkEmpresa(),
                            3,
                            4);
                    System.out.println("Tempo de transferência: " + disco.getTempoDeTransferencia());
                }
//                if (porcentagemUsoMemoria > 80.0) {
//                    for (Processo processo : processos) {
//                        int pid = processo.getPid();
//                        if (pid > 1000) {
//                            try {
//                                // Converte o PID para uma string
//                                String pidString = Integer.toString(pid);
//
//                                // Executa o comando "kill" para encerrar o processo pelo seu PID
//                                ProcessBuilder processBuilder = new ProcessBuilder("kill -9", pidString);
//                                Process process = processBuilder.start();
//
//                                // Verifica o código de saída do comando
//                                int exitCode = process.waitFor();
//
//                                if (exitCode == 0) {
//                                    System.out.println("Processo com PID " + pid + " encerrado com sucesso.");
//                                } else {
//                                    System.out.println("Ocorreu um erro ao tentar encerrar o processo com PID " + pid);
//                                }
//                            } catch (IOException | InterruptedException e) {
//                                System.out.println("Ocorreu uma exceção ao tentar encerrar o processo com PID " + pid);
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
                System.out.println("Porcentagem memoria: " + porcentagemUsoMemoria);
                System.out.println("Porcentagem uso CPU: " + processador.getUso());
            }
        },

                 0, 5000);
    }
}
