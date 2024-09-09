package br.ufpb.dcx.rodrigor.projetos.projeto.model;

import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.participante.model.CategoriaParticipante;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;

import java.time.LocalDate;
import java.util.List;

public class Projeto {
    private String id;
    private String nome;
    private String descricao;
    private Categoria categoria;
    private List<Participante> participantes;
    private Participante coordenador;
    private Empresa empresa;
    private LocalDate dataInicio;
    private LocalDate dataEncerramento;

    public Projeto(String id, String nome, String descricao, Categoria categoria, List<Participante> participantes, Participante coordenador, Empresa empresa, LocalDate dataInicio, LocalDate dataEncerramento) {
        testaEntrada(nome);
        testaEntrada(descricao);
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.categoria = categoria;
        this.participantes = participantes;
        this.coordenador = coordenador;
        this.empresa = empresa;
        this.dataInicio = dataInicio;
        this.dataEncerramento = dataEncerramento;
    }

    public Projeto() {
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public void testaEntrada(String x){
        if (x == null || x.isEmpty()){ throw new IllegalArgumentException("É obrigatório escrever algo neste campo.");}
    }

    public void testaNumero(String x) {
        for (int i = 0; i < x.length(); i++) {
            char c = x.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("O número informado contém valores que não são números.");
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Participante getCoordenador() {
        return coordenador;
    }

    public void setCoordenador(Participante coordenador) {
        if(!coordenador.getCategoria().equals(CategoriaParticipante.PROFESSOR)) {
            throw new IllegalArgumentException("O coordenador deve ser um professor: "+coordenador);
        }
        this.coordenador = coordenador;
    }

    public void setNome(String nome) {
        testaEntrada(nome);
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        testaEntrada(descricao);
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(LocalDate dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }
}