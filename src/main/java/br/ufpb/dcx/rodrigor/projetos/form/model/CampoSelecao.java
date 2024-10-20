package br.ufpb.dcx.rodrigor.projetos.form.model;

import br.ufpb.dcx.rodrigor.projetos.form.model.validadores.ValidadorCampo;

import java.util.List;

public class CampoSelecao<T> extends Campo {

    private List<T> opcoes;
    private ValidadorCampo validador;
    private String tipo = "select";

    public CampoSelecao(String id, String label, List<T> opcoes, ValidadorCampo validador, boolean obrigatorio) {
        super(id, label, validador, obrigatorio);
        this.opcoes = opcoes;
        this.validador = validador;
    }

    @Override
    public String getTipo() {
        return tipo;
    }

    public List<T> getOpcoes() {
        return opcoes;
    }

    public void setOpcoes(List<T> opcoes) {
        this.opcoes = opcoes;
    }

    public String getTipoDasOpcoes() {
        if (opcoes != null && !opcoes.isEmpty()) {
            return opcoes.get(0).getClass().getSimpleName();
        }
        return "Desconhecido";
    }
}


