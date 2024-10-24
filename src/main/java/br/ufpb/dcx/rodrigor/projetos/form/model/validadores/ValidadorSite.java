package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

public class ValidadorSite implements ValidadorCampo {

    private int min;
    private int max;

    public ValidadorSite(int min, int max){
        this.min = min;
        this.max = max;
    }

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if(valor.length() < min || valor.length() > max){
            return new ResultadoValidacao( "O texto informado deve ter entre " + min + " e " + max + " caracteres");
        }
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("O link do site não pode ser vazio");
        }
        String siteRegex = "^(https?:\\/\\/)?([\\w.-]+)\\.([a-z\\.]{2,6})([\\/\\w\\.-]*)*\\/?$";
        if (!valor.matches(siteRegex)) {
            return new ResultadoValidacao("Informe um site válido, como 'meusite.com' ou 'https://meusite.com'");
        }
        return new ResultadoValidacao();
    }
}

