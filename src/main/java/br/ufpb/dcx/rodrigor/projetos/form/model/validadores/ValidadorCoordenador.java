package br.ufpb.dcx.rodrigor.projetos.form.model.validadores;

import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class ValidadorCoordenador implements ValidadorCampo {

    private List<ObjectId> idsProfessoresValidos = new ArrayList<>();

    public ValidadorCoordenador(List<Participante> professores) {
        for (Participante professor : professores) {
            idsProfessoresValidos.add(professor.getId());
        }
    }

    @Override
    public ResultadoValidacao validarCampo(String valor) {
        if (valor == null || valor.isEmpty()) {
            return new ResultadoValidacao("Selecione um coordenador válido");
        }
        try {
            ObjectId idProfessor = new ObjectId(valor); // Converte o valor para ObjectId
            if (!idsProfessoresValidos.contains(idProfessor)) {
                return new ResultadoValidacao("Selecione um coordenador válido da lista");
            }
        } catch (IllegalArgumentException e) {
            return new ResultadoValidacao("ID de coordenador inválido");
        }
        return new ResultadoValidacao();
    }
}



