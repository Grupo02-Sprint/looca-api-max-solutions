/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.tentativa.looca.api;

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
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Cesar
 */
public class LoocaApi {
   public void demonstraLooca(){
       
        Looca looca = new Looca();
        Conexao conexao = new Conexao();
        JdbcTemplate con = conexao.getConexaoDoBanco();
        Sistema sistema = looca.getSistema();

        sistema.getPermissao();
        sistema.getFabricante();
        sistema.getArquitetura();
        sistema.getInicializado();
        sistema.getSistemaOperacional();

        System.out.println(sistema);

        con.update(String.format("insert into maquina (permissao,fabricante,arquitetura,inicializacao,sistema_operacional)"
                + " values ('%s','%s','%s','%s','%s')",
                sistema.getPermissao().toString(),
                sistema.getFabricante(),
                sistema.getArquitetura().toString(),
                sistema.getInicializado().toString(),
                sistema.getSistemaOperacional()
                )
        );

        Memoria memoria = looca.getMemoria();

        System.out.println(memoria);

        DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();

        List<Disco> discos = grupoDeDiscos.getDiscos();
        for (Disco disco : discos) {
            System.out.println(disco);
        }

        ProcessoGrupo grupoDeProcesso = looca.getGrupoDeProcessos();

        List<Processo> processos = grupoDeProcesso.getProcessos();

        for (Processo processo : processos) {
            System.out.println(processo);
        }

        Processador processador = looca.getProcessador();

        System.out.println(processador);

        Rede rede = looca.getRede();

        RedeParametros redeParametros = rede.getParametros();

        System.out.println(redeParametros);

        RedeInterfaceGroup gruposDeInterface = rede.getGrupoDeInterfaces();
        List<RedeInterface> interfaces = gruposDeInterface.getInterfaces();
        for (RedeInterface redeInterface : interfaces) {
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

