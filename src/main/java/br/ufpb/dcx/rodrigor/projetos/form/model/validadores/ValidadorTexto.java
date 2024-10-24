package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

public class ValidadorTexto implements ValidadorCampo {

    private int min;
    private int max;

    public ValidadorTexto(int min, int max){
        this.min = min;
        this.max = max;
    }

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if(valor.length() < min || valor.length() > max){
            return new ResultadoValidacao( "O texto informado deve ter entre " + min + " e " + max + " caracteres");
        }
        return new ResultadoValidacao();
    }
}
