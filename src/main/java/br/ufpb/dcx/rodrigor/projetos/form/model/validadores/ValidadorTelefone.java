package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

public class ValidadorTelefone implements ValidadorCampo {

    private int min;
    private int max;

    public ValidadorTelefone(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        // Verifica se o comprimento do número de telefone está dentro do limite
        if (valor.length() < min || valor.length() > max) {
            return new ResultadoValidacao("O número de telefone deve ter entre " + min + " e " + max + " caracteres");
        }

        // Verifica se o valor contém apenas números
        if (!valor.matches("\\d+")) {
            return new ResultadoValidacao("O número de telefone deve conter apenas dígitos");
        }

        return new ResultadoValidacao();
    }
}

