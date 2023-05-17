/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tentativa.looca.api;

import com.github.britooo.looca.api.group.processos.Processo;
import com.github.britooo.looca.api.group.processos.ProcessoGrupo;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author yago
 */
public class TesteInovacao {
    
    
        ProcessoGrupo grupoDeProcesso = looca.getGrupoDeProcessos();

        List<Processo> processos = grupoDeProcesso.getProcessos();

        for (Processo processo : processos) {
        processo.add(processo);
        

        //    System.out.println(processo);
        }
    
    List<String> processosImportantes = new ArrayList<>();
    
}
