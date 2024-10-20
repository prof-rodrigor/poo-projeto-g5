package br.ufpb.dcx.rodrigor.projetos.form.model;

import br.ufpb.dcx.rodrigor.projetos.form.model.validadores.ResultadoValidacao;
import br.ufpb.dcx.rodrigor.projetos.form.model.validadores.ValidadorCampo;

public abstract class Campo {

    private String id;
    private String label;
    private String valor;
    private ValidadorCampo validador;
    private boolean obrigatorio;

    public Campo(String id, String label, ValidadorCampo validador, boolean obrigatorio) {
        this.id = id;
        this.label = label;
        this.validador = validador;
        this.obrigatorio = obrigatorio;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public ResultadoValidacao validar() {
        if (this.obrigatorio && (this.valor == null || this.valor.isEmpty())) {
            return new ResultadoValidacao("Campo obrigatório");
        }
        return validador.validarCampo(this.valor);
    }

    public abstract String getTipo();
}

