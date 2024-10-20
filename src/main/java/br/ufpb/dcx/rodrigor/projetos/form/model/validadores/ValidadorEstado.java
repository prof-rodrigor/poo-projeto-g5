package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

public class ValidadorEstado implements ValidadorCampo {

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("O estado não pode ser vazio");
        }
        // Lista de siglas válidas dos estados brasileiros
        String estadosRegex = "^(AC|AL|AP|AM|BA|CE|DF|ES|GO|MA|MT|MS|MG|PA|PB|PR|PE|PI|RJ|RN|RS|RO|RR|SC|SP|SE|TO)$";
        if (!valor.matches(estadosRegex)) {
            return new ResultadoValidacao("Informe um estado válido");
        }
        return new ResultadoValidacao();
    }
}
