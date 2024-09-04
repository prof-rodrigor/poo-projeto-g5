package br.ufpb.dcx.rodrigor.projetos.empresa.controllers;

import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Endereco;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EmpresaService;
import io.javalin.http.Context;

public class EmpresaController {
    private static final EmpresaService empresaService = new EmpresaService();

    public void listarEmpresas(Context ctx) {
        ctx.attribute("empresas", empresaService.listarEmpresas());
        ctx.render("/empresas/lista_empresas.html");
    }

    public void adicionarEmpresa(Context ctx) {
        try {
            Empresa empresa = new Empresa();
            empresa.setNome(ctx.formParam("nome"));
            empresa.setSite(ctx.formParam("site"));
            empresa.setInstagram(ctx.formParam("instagram"));
            empresa.setLinkedin(ctx.formParam("linkedin"));
            empresa.setGithub(ctx.formParam("github"));
            empresa.setTelefone(ctx.formParam("telefone"));

            Endereco endereco = new Endereco();
            endereco.setCidade(ctx.formParam("endereco.cidade"));
            endereco.setEstado(ctx.formParam("endereco.estado"));
            endereco.setBairro(ctx.formParam("endereco.bairro"));
            endereco.setRua(ctx.formParam("endereco.rua"));
            endereco.setNumero(ctx.formParam("endereco.numero"));
            empresa.setEndereco(endereco);

            empresaService.adicionarEmpresa(empresa);
            ctx.render("/empresas");
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("Erro ao adicionar empresa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removerEmpresa(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        empresaService.removerEmpresa(id);
        ctx.render("/empresas");
    }
}
