package br.ufpb.dcx.rodrigor.projetos.form.model;

import br.ufpb.dcx.rodrigor.projetos.form.model.persistencia.PersistenciaFormulario;

import java.util.ArrayList;
import java.util.List;

public class Formulario {

    private List<Secao> secoes = new ArrayList<>();
    private String nome;
    private Titulo titulo;
    private final String id;
    private PersistenciaFormulario persistencia;

    public Formulario( String id, String nome){
        this.nome = nome;
        this.id = id;
    }

    public void addSecao(Secao secao){
        secoes.add(secao);
    }

    public List<Secao> getSecoes(){
        return secoes;
    }

    public String getId() {
        return id;
    }

    public String getNome(){
        return nome;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setPersistencia(PersistenciaFormulario persistencia) {
        this.persistencia = persistencia;
    }

    public boolean isEditando() {
        return this.id != null;
    }

    @Override
    public String toString() {
        return "Formulario{" +
                "campos=" + secoes +
                ", nome='" + nome + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public void persistir() {
        if(persistencia != null){
            persistencia.persistir(this);
        }

    }
}
