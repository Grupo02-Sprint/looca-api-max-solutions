/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.tentativa.looca.api;

import com.mycompany.tentativa.looca.api.conexao.ConexaoLocal;
import com.mycompany.tentativa.looca.api.conexao.Conexao;
import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
import com.github.britooo.looca.api.group.discos.Volume;
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
import com.mycompany.tentativa.looca.api.conexao.IdealDAO;
import com.mycompany.tentativa.looca.api.conexao.Maquina;
import com.mycompany.tentativa.looca.api.conexao.TokenDAO;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;
import com.slack.api.Slack;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;

/**
 *
 * @author Cesar
 */
public class LoocaApi {

    Inovacao inovacao = new Inovacao();
    IntegracaoSlack integraSlack = new IntegracaoSlack();

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
        List<Volume> discos = grupoDeDiscos.getVolumes();

        Processador processador = looca.getProcessador();

        ProcessoGrupo grupoDeProcesso = looca.getGrupoDeProcessos();
        List<Processo> processos = grupoDeProcesso.getProcessos();

        Rede rede = looca.getRede();
        RedeParametros redeParametros = rede.getParametros();
        RedeInterfaceGroup gruposDeInterface = rede.getGrupoDeInterfaces();
        List<RedeInterface> interfaces = gruposDeInterface.getInterfaces();

        for (Volume disco : discos) {
            Double tamanhoDisco = disco.getTotal().doubleValue() / (1024.0 * 1024.0 * 1024.0);
            // Verificar se já existem dados da fk_maquina na tabela especificacao
            if (!existeDadosFKMaquina(m.getIdMaquina(), 3, conexao.conectaBD())) {
                con.update("INSERT INTO especificacao (fk_maquina,fk_loja,fk_componente,data_ativacao_componente,capacidade) "
                        + "values (?,?,?,?,?)", m.getIdMaquina(), m.getFkEmpresa(), 3, formattedDateTime, tamanhoDisco);
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
                    m.getIdMaquina(), m.getFkEmpresa(), 2, formattedDateTime, frequenciaGigaHertz);
        } else {
            con.update("UPDATE especificacao SET capacidade = ? WHERE fk_maquina = ? AND fk_componente = ?",
                    frequenciaGigaHertz, m.getIdMaquina(), 2);
        }

        try {
            Connection connection = conexao.conectaBD();
            connection.setAutoCommit(false);

            String insertQuery = "INSERT INTO processo VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery);

            for (Processo processo : processos) {
                int pid = processo.getPid();

                String selectQuery = "SELECT COUNT(*) FROM processo WHERE pid = ?";
                PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
                selectStatement.setInt(1, pid);

                ResultSet resultSet = selectStatement.executeQuery();
                resultSet.next();
                int count = resultSet.getInt(1);

                if (count == 0) {
                    insertStatement.setInt(1, processo.getPid());
                    insertStatement.setDouble(2, processo.getUsoCpu());
                    insertStatement.setDouble(3, processo.getUsoMemoria());
                    insertStatement.setInt(4, m.getIdMaquina());
                    insertStatement.setInt(5, m.getFkEmpresa());
                    insertStatement.setString(6, dataHoraAtual.format(formatter));
                    insertStatement.setString(7, processo.getNome());
                    insertStatement.addBatch();
                } 

                resultSet.close();
                selectStatement.close();
            }

            insertStatement.executeBatch();

            connection.commit();

            insertStatement.close();
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoocaApi.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        System.out.println("Iniciando monitoramento...");
        
        new java.util.Timer().scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                //Porcentagem de uso da memória
                Double porcentagemUsoMemoria = (memoria.getEmUso().doubleValue() / memoria.getTotal().doubleValue()) * 100.0;
                //Definindo novamente o localDateTime
                LocalDateTime dataHoraAtual = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = dataHoraAtual.format(formatter);
                //Métricas de memória tanto local quanto na Azure
                con.update("insert into metrica "
                        + "(captura,dt_hora_captura,fk_maquina,fk_loja,fk_componente,fk_unidade_medida)"
                        + "values(?,?,?,?,?,?)",
                        porcentagemUsoMemoria,
                        formattedDateTime,
                        m.getIdMaquina(),
                        m.getFkEmpresa(),
                        1,
                        4);
                conLocal.update("insert into metrica "
                        + "(captura,dt_hora_captura,fk_componente,fk_unidade_medida)"
                        + "values(?,?,?,?)",
                        porcentagemUsoMemoria,
                        formattedDateTime,
                        1,
                        4);
                Double limiteToleravelMemoria = 0.0;
                Double limiteToleravelProcessador = 0.0;
                Double limiteToleravelDisco = 0.0;

                try {
                    IdealDAO ideal = new IdealDAO();

                    limiteToleravelMemoria = ideal.getLimiteToleravel(m.getFkEmpresa(), 1);

                    //Processador métricas
                } catch (SQLException ex) {
                    Logger.getLogger(LoocaApi.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (porcentagemUsoMemoria > limiteToleravelMemoria) {
                    integraSlack.receberMensagem(porcentagemUsoMemoria, m.getIdMaquina(), "memória");
                    inovacao.executaInovacao();
                }
               
                //Processador métricas
                con.update("insert into metrica "
                        + "(captura,dt_hora_captura,fk_maquina,fk_loja,fk_componente,fk_unidade_medida)"
                        + "values(?,?,?,?,?,?)",
                        processador.getUso(),
                        formattedDateTime,
                        m.getIdMaquina(),
                        m.getFkEmpresa(),
                        2,
                        4);
                conLocal.update("insert into metrica "
                        + "(captura,dt_hora_captura,fk_componente,fk_unidade_medida)"
                        + "values(?,?,?,?)",
                        processador.getUso(),
                        formattedDateTime,
                        2,
                        4);

                try {
                    IdealDAO ideal = new IdealDAO();

                    limiteToleravelProcessador = ideal.getLimiteToleravel(m.getFkEmpresa(), 2);

                    //Processador métricas
                } catch (SQLException ex) {
                    Logger.getLogger(LoocaApi.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(limiteToleravelProcessador);
                if (processador.getUso() > limiteToleravelProcessador) {
                    integraSlack.receberMensagem(processador.getUso(), m.getIdMaquina(), "processador");
                }
                
                //Disco métricas

                for (Volume disco : discos) {
//                    Double porcentagemUsoDisco = (disco.getDisponivel().doubleValue() / disco.getTotal().doubleValue()) * 100.0;
                    Double porcentagemUsoDisco = 40.0;
                    con.update("insert into metrica "
                            + "(captura,dt_hora_captura,fk_maquina,fk_loja,fk_componente,fk_unidade_medida)"
                            + "values(?,?,?,?,?,?)",
                            porcentagemUsoDisco.floatValue(),
                            formattedDateTime,
                            m.getIdMaquina(),
                            m.getFkEmpresa(),
                            3,
                            4);
                    conLocal.update("insert into metrica "
                            + "(captura,dt_hora_captura,fk_componente,fk_unidade_medida)"
                            + "values(?,?,?,?)",
                            porcentagemUsoDisco,
                            formattedDateTime,
                            3,
                            3);

                    try {
                        IdealDAO ideal = new IdealDAO();

                        limiteToleravelDisco = ideal.getLimiteToleravel(m.getFkEmpresa(), 3);

                        //Processador métricas
                    } catch (SQLException ex) {
                        Logger.getLogger(LoocaApi.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (porcentagemUsoDisco > limiteToleravelDisco) {
                        integraSlack.receberMensagem(porcentagemUsoDisco, m.getIdMaquina(), "disco");
                    }
                }
                for (RedeInterface redeInterface : interfaces) {
                    Double mbUtilizadosEnviados = redeInterface.getBytesEnviados() / (1024.0 * 1024.0);
                    Double mbUtilizadosRecebidos = redeInterface.getBytesRecebidos() / (1024.0 * 1024.0);
                    con.update("Insert into rede (bytes_enviados, bytes_recebidos,fk_maquina, dt_hora) values (?,?,?,?);",
                            mbUtilizadosEnviados,
                            mbUtilizadosRecebidos,
                            m.getIdMaquina(),
                            dataHoraAtual);
                    conLocal.update("Insert into rede (bytes_enviados, bytes_recebidos,nome) values (?,?,?);",
                            mbUtilizadosEnviados,
                            mbUtilizadosRecebidos,
                            redeInterface.getNomeExibicao());
                }
            }
        },
                0, 10000);
    }
}
