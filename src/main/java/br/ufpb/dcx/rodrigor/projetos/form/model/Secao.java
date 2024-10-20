package br.ufpb.dcx.rodrigor.projetos.form.model;

import java.util.ArrayList;
import java.util.List;

public class Secao {
    private List<Campo> campos = new ArrayList<>();
    private String nome;

    public Secao(String nome) {
        this.nome = nome;
    }

    public void addCampo(Campo campo){
        campos.add(campo);
    }

    public List<Campo> getCampos() {
        return campos;
    }

    public void setCampos(List<Campo> campos) {
        this.campos = campos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
