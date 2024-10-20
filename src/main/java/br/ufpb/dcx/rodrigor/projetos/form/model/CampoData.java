package br.ufpb.dcx.rodrigor.projetos.form.model;

import br.ufpb.dcx.rodrigor.projetos.form.model.validadores.ValidadorCampo;

public class CampoData extends Campo {
    private String tipo = "date";

    public CampoData(String id, String label, ValidadorCampo validador, boolean obrigatorio) {
        super(id, label, validador, obrigatorio);
    }

    @Override
    public String getTipo() {
        return tipo;
    }
}

