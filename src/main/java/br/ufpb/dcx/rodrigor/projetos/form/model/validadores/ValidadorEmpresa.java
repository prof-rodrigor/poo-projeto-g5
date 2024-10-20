package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;

import java.util.ArrayList;
import java.util.List;

public class ValidadorEmpresa implements ValidadorCampo {

    private List<String> idsEmpresasValidas = new ArrayList<>();

    public ValidadorEmpresa(List<Empresa> empresas) {
        for (Empresa empresa : empresas) {
            idsEmpresasValidas.add(empresa.getId());
        }
    }

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("Selecione uma empresa válida");
        }
        if (!idsEmpresasValidas.contains(valor)) {
            return new ResultadoValidacao("Selecione uma empresa válida da lista");
        }
        return new ResultadoValidacao();
    }
}

