package br.ufpb.dcx.rodrigor.projetos.form.model;

import br.ufpb.dcx.rodrigor.projetos.form.model.validadores.ValidadorCampo;

public class CampoTexto extends Campo {
    private String tipo = "text";

    public CampoTexto(String id, String label, ValidadorCampo validador, boolean obrigatorio) {
        super(id, label, validador, obrigatorio);
    }

    @Override
    public String getTipo() {
        return tipo;
    }
}


