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

        System.out.println(sistema);

        Memoria memoria = looca.getMemoria();

        System.out.println(memoria);

        DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();

        Integer discoCadastrado = 0;
        List<Disco> discos = grupoDeDiscos.getDiscos();
        for (Disco disco : discos) {

            System.out.println(disco.getTamanho());

            if (discoCadastrado < 1) {
                con.update(String.format("INSERT INTO especificacao (fk_maquina,fk_loja,fk_componente,data_ativacao_componente,capacidade) "
                        + "values (%d,%d,%d,'%s',%d)", m.getIdMaquina(), m.getFkEmpresa(), 3, formattedDateTime, disco.getTamanho()/10000000));
                discoCadastrado++;
            }
        }
        Processador processador = looca.getProcessador();

        System.out.println(processador);

        con.update(String.format("INSERT INTO especificacao (fk_maquina,fk_loja,fk_componente,data_ativacao_componente,capacidade) "
                + "values (%d,%d,%d,'%s',%d)", m.getIdMaquina(), m.getFkEmpresa(), 1, formattedDateTime, memoria.getTotal()/10000000));

        con.update(String.format("INSERT INTO especificacao (fk_maquina,fk_loja,fk_componente,data_ativacao_componente,capacidade) "
                + "values (%d,%d,%d,'%s',%d)", m.getIdMaquina(), m.getFkEmpresa(), 2, formattedDateTime, processador.getFrequencia()/10000000));

        ProcessoGrupo grupoDeProcesso = looca.getGrupoDeProcessos();

        List<Processo> processos = grupoDeProcesso.getProcessos();

        for (Processo processo : processos) {
//         conLocal.update(String.format("Insert into processo (pidProcesso,dtHora,usoCpu,usoMemoria) values"
//                   + " ('%d','%s','%s','%s');",processo.getPid(),dataHoraAtual, df.format(processo.getUsoCpu()),df.format(processo.getUsoMemoria()))); //NOI18N
            System.out.println(processo);
            }

        Rede rede = looca.getRede();

        RedeParametros redeParametros = rede.getParametros();

        System.out.println(redeParametros);

        RedeInterfaceGroup gruposDeInterface = rede.getGrupoDeInterfaces();
        List<RedeInterface> interfaces = gruposDeInterface.getInterfaces();
        for (RedeInterface redeInterface : interfaces) {
            con.update(String.format("Insert into rede (bytes_enviados, bytes_recebidos,nome) values (%d,%d,'%s');",
                    redeInterface.getBytesEnviados(), redeInterface.getBytesEnviados(), redeInterface.getNomeExibicao()));
            conLocal.update(String.format("Insert into rede (bytes_enviados, bytes_recebidos,nome) values (%d,%d,'%s');",
                    redeInterface.getBytesEnviados(), redeInterface.getBytesEnviados(), redeInterface.getNomeExibicao()));
            System.out.println(redeInterface);
        }

        JanelaGrupo gruposDeJanela = looca.getGrupoDeJanelas();
        List<Janela> janelas = gruposDeJanela.getJanelas();
        List<Janela> janelasVisiveis = gruposDeJanela.getJanelasVisiveis();

        for (Janela janela : janelas) {
            System.out.println(janela);
        }

        for (Janela janelaVisivel : janelasVisiveis) {
            System.out.println(janelaVisivel);  
        }

    }
}
