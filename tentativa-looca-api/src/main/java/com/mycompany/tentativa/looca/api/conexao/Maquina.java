/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tentativa.looca.api.conexao;

/**
 *
 * @author Cesar
 */
public class Maquina {
    private Integer idMaquina;
    private String patrimonio;
    private String senha;
    private Integer fkEmpresa;
    private Integer fkUsuario;

    public Maquina(Integer idMaquina, String patrimonio, String senha, Integer fkEmpresa, Integer fkUsuario) {
        this.idMaquina = idMaquina;
        this.patrimonio = patrimonio;
        this.senha = senha;
        this.fkEmpresa = fkEmpresa;
        this.fkUsuario = fkUsuario;
    }

    public Maquina(String patrimonio, String senha) {
        this.patrimonio = patrimonio;
        this.senha = senha;
    }

    public Integer getIdMaquina() {
        return idMaquina;
    }

    public void setIdMaquina(Integer idMaquina) {
        this.idMaquina = idMaquina;
    }

    public String getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(String patrimonio) {
        this.patrimonio = patrimonio;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }
 
    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    } 
}
