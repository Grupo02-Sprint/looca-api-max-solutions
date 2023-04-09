/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.tentativa.looca.api;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.DiscoGrupo;
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

/**
 *
 * @author Cesar
 */
public class TentativaLoocaApi {

    public static void main(String[] args) {
        Looca looca = new Looca();

        Sistema sistema = looca.getSistema();

        sistema.getPermissao();
        sistema.getFabricante();
        sistema.getArquitetura();
        sistema.getInicializado();
        sistema.getSistemaOperacional();
        
        System.out.println(sistema);
        
        Memoria memoria = looca.getMemoria();
        
        System.out.println(memoria);
        
        DiscoGrupo grupoDeDiscos = looca.getGrupoDeDiscos();
        List<Disco> discos = grupoDeDiscos.getDiscos(); 
        for(Disco disco:discos){
            System.out.println(disco);
        }
        
        ProcessoGrupo grupoDeProcesso= looca.getGrupoDeProcessos();
        List<Processo> processos = grupoDeProcesso.getProcessos();
        for(Processo processo : processos){
            System.out.println(processo);
        }
        
        Processador processador = looca.getProcessador();
        System.out.println(processador);
        
        Rede rede = looca.getRede();
        RedeParametros redeParametros = rede.getParametros();
        System.out.println(redeParametros);
        
        RedeInterfaceGroup gruposDeInterface=rede.getGrupoDeInterfaces();
        List<RedeInterface> interfaces = gruposDeInterface.getInterfaces();
        for(RedeInterface redeInterface: interfaces ){
            System.out.println(redeInterface);
        }
        
        
        
    }
}
