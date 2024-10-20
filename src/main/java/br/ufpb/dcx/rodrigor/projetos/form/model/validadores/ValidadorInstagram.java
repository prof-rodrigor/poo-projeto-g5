package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

public class ValidadorInstagram implements ValidadorCampo {

    private int min;
    private int max;

    public ValidadorInstagram(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if(valor.length() < min || valor.length() > max){
            return new ResultadoValidacao( "O texto informado deve ter entre " + min + " e " + max + " caracteres");
        }
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("O link do Instagram não pode ser vazio");
        }
        String instagramRegex = "^(https?:\\/\\/)?(www\\.)?instagram\\.com\\/.*$|^@\\w+$";
        if (!valor.matches(instagramRegex)) {
            return new ResultadoValidacao("Informe um link válido do Instagram, como 'instagram.com/exemplo' ou '@exemplo'");
        }
        return new ResultadoValidacao();
    }
}

