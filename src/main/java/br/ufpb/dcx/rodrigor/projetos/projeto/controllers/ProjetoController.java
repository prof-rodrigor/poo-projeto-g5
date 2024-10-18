package br.ufpb.dcx.rodrigor.projetos.projeto.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EmpresaService;
import br.ufpb.dcx.rodrigor.projetos.participante.model.CategoriaParticipante;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import br.ufpb.dcx.rodrigor.projetos.projeto.model.Categoria;
import br.ufpb.dcx.rodrigor.projetos.projeto.model.Projeto;
import br.ufpb.dcx.rodrigor.projetos.projeto.services.ProjetoService;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.Objects;

public class ProjetoController {

    public void listarProjetos(Context ctx) {
        ProjetoService projetoService = ctx.appData(Keys.PROJETO_SERVICE.key());
        ctx.attribute("projetos", projetoService.listarProjetos());
        ctx.render("/projetos/lista_projetos.html");
    }

    public void mostrarFormulario(Context ctx) {
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        ctx.attribute("professores", participanteService.listarProfessores());
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());
        ctx.attribute("empresas", empresaService.listarEmpresasFormulario());
        ctx.render("/projetos/form_projeto.html");
    }

    public void mostrarFormularioEdicao(Context ctx) {
        ProjetoService projetoService = ctx.appData(Keys.PROJETO_SERVICE.key());

        String id = ctx.pathParam("id");
        Projeto projeto = projetoService.buscarProjetoPorId(id).get();

        if (projeto != null) {
            ctx.attribute("projeto", projeto);
            ctx.render("/projetos/form_projeto.html");
        } else {
            System.out.println("Projeto com ID " + id + " não foi encontrado.");
            ctx.status(404).result("Projeto não encontrado");
        }
    }

    public void adicionarProjeto(Context ctx) {
        Projeto projeto = extrairProjeto(ctx);
        ProjetoService projetoService = ctx.appData(Keys.PROJETO_SERVICE.key());
        if (projeto != null) projetoService.adicionarProjeto(projeto);
        ctx.redirect("/projetos");
    }

    public void removerProjeto(Context ctx) {
        ProjetoService projetoService = ctx.appData(Keys.PROJETO_SERVICE.key());
        String id = ctx.pathParam("id");
        projetoService.removerProjeto(id);
        ctx.redirect("/projetos");
    }

    public void atualizarProjeto(Context ctx){
        ProjetoService projetoService = ctx.appData(Keys.PROJETO_SERVICE.key());
        Projeto projeto = extrairProjeto(ctx);
        String id = ctx.pathParam("id");
        projeto.setId(id);
        projetoService.atualizarProjeto(projeto);
        ctx.redirect("/projetos");
    }

    public static Projeto extrairProjeto(Context ctx) {
        ParticipanteService participanteService = ctx.appData(Keys.PARTICIPANTE_SERVICE.key());
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());

        if ((!ctx.formParam("nome").isEmpty()) || (!ctx.formParam("descricao").isEmpty())) {
            Projeto projeto = new Projeto();
            projeto.setNome(ctx.formParam("nome"));
            projeto.setDescricao(ctx.formParam("descricao"));
            if (Objects.equals(ctx.formParam("categoria"), "Extensão")) projeto.setCategoria(Categoria.PE);
            if (Objects.equals(ctx.formParam("categoria"), "Pesquisa")) projeto.setCategoria(Categoria.PP);
            if (Objects.equals(ctx.formParam("categoria"), "Integração")) projeto.setCategoria(Categoria.PIE);
            if (Objects.equals(ctx.formParam("categoria"), "Outro")) projeto.setCategoria(Categoria.Other);
            projeto.setDataInicio(LocalDate.parse(ctx.formParam("dataInicio")));
            projeto.setDataEncerramento(LocalDate.parse(ctx.formParam("dataEncerramento")));

            String coordenadorId = ctx.formParam("coordenador");
            Participante coordenador = participanteService.buscarParticipantePorId(coordenadorId)
                    .orElseThrow(() -> new IllegalArgumentException("Coordenador não encontrado"));

            if (coordenador.getCategoria() != CategoriaParticipante.PROFESSOR) {
                throw new IllegalArgumentException("Somente professores podem ser coordenadores.");
            }

            String empresaId = ctx.formParam("empresa");
            Empresa empresa = new Empresa();
            if (empresaId.isEmpty()) {

            } else {
                empresa = empresaService.buscarEmpresaPorId(empresaId).get();
            }

            projeto.setEmpresa(empresa);
            projeto.setCoordenador(coordenador);
            return projeto;
        }
        return null;
    }
}