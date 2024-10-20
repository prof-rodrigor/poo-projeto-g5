package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

public class ValidadorCategoria implements ValidadorCampo {

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("A categoria não pode ser vazia");
        }
        String categoriasRegex = "^(Extensão|Pesquisa|Integração com empresa|Outro)$";
        if (!valor.matches(categoriasRegex)) {
            return new ResultadoValidacao("Informe uma categoria válida");
        }
        return new ResultadoValidacao();
    }
}

