package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

public class ValidadorLinkedin implements ValidadorCampo {

    private int min;
    private int max;

    public ValidadorLinkedin(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if(valor.length() < min || valor.length() > max){
            return new ResultadoValidacao( "O texto informado deve ter entre " + min + " e " + max + " caracteres");
        }
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("O link do LinkedIn não pode ser vazio");
        }
        String linkedinRegex = "^(https?:\\/\\/)?(www\\.)?linkedin\\.com\\/.*$";
        if (!valor.matches(linkedinRegex)) {
            return new ResultadoValidacao("Informe um link válido do LinkedIn, como 'linkedin.com/in/exemplo'");
        }
        return new ResultadoValidacao();
    }
}


