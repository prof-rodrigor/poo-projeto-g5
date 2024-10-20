package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

public class ValidadorGithub implements ValidadorCampo {

    private int min;
    private int max;

    public ValidadorGithub(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if(valor.length() < min || valor.length() > max){
            return new ResultadoValidacao( "O texto informado deve ter entre " + min + " e " + max + " caracteres");
        }
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("O link do GitHub não pode ser vazio");
        }
        String githubRegex = "^(https?:\\/\\/)?(www\\.)?github\\.com\\/.*$";
        if (!valor.matches(githubRegex)) {
            return new ResultadoValidacao("Informe um link válido do GitHub, como 'github.com/exemplo'");
        }
        return new ResultadoValidacao();
    }
}

