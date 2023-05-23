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
        DecimalFormat df = new DecimalFormat("0.00s");
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

        Integer discoCadastrado = 0;

        for (Disco disco : discos) {
            // Verificar se já existem dados da fk_maquina na tabela especificacao
            if (!existeDadosFKMaquina(m.getIdMaquina(), 3, conexao.conectaBD())) {
                con.update(String.format("INSERT INTO especificacao (fk_maquina,fk_loja,fk_componente,data_ativacao_componente,capacidade) "
                        + "values (%d,%d,3,'%s',%d)", m.getIdMaquina(), m.getFkEmpresa(), formattedDateTime, disco.getTamanho() / 10000000));
            } else {
                con.update(String.format("update especificacao set capacidade= %d where fk_maquina = %d and fk_componente = 3",
                        disco.getTamanho() / 10000000, m.getIdMaquina()));
            }
        }

        if (!existeDadosFKMaquina(m.getIdMaquina(), 1, conexao.conectaBD())) {
            con.update("INSERT INTO especificacao (fk_maquina, fk_loja, fk_componente, data_ativacao_componente, capacidade) "
                    + "VALUES (?, ?, ?, ?, ?)",
                    m.getIdMaquina(), m.getFkEmpresa(), 1, formattedDateTime, memoria.getTotal() / 10000000);
        } else {
            con.update("UPDATE especificacao SET capacidade = ? WHERE fk_maquina = ? AND fk_componente = ?",
                    memoria.getTotal() / 10000000, m.getIdMaquina(), 1);
        }

        if (!existeDadosFKMaquina(m.getIdMaquina(), 2, conexao.conectaBD())) {
            con.update("INSERT INTO especificacao (fk_maquina, fk_loja, fk_componente, data_ativacao_componente, capacidade) "
                    + "VALUES (?, ?, ?, ?, ?)",
                    m.getIdMaquina(), m.getFkEmpresa(), 2, formattedDateTime, memoria.getTotal() / 10000000);
        } else {
            con.update("UPDATE especificacao SET capacidade = ? WHERE fk_maquina = ? AND fk_componente = ?",
                    memoria.getTotal() / 10000000, m.getIdMaquina(), 2);
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
//             con.update(String.format("insert into metrica "
//                     + "(captura,dt_hora_captura,fk_maquina,fk_loja,fk_componente,fk_unidade_medida)"
//                     + "values(%d,'%s',%d,%d,%d,%d)"));
                System.out.println(memoria.getDisponivel() / 1000000);
            }
        }, 0, 5000);
    }
}
