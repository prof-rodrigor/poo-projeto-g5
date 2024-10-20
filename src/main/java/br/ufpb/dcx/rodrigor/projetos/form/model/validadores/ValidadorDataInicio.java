package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

import java.time.LocalDate;

public class ValidadorDataInicio implements ValidadorCampo {

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("A data de início não pode ser vazia");
        }

        try {
            LocalDate.parse(valor);  // Verifica se a data está no formato correto
        } catch (Exception e) {
            return new ResultadoValidacao("Formato de data de início inválido");
        }

        return new ResultadoValidacao();  // Validação bem-sucedida
    }
}

