package br.ufpb.dcx.rodrigor.projetos.empresa.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Endereco;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EmpresaService;
import io.javalin.http.Context;

public class EmpresaController {

    public void listarEmpresas(Context ctx) {
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());
        ctx.attribute("empresas", empresaService.listarEmpresas());
        ctx.render("/empresas/lista_empresas.html");
    }

    public void mostrarFormulario(Context ctx) {
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());
        ctx.attribute("empresas", empresaService.listarEmpresas());
        ctx.render("/empresas/form_empresa.html");
    }

    public void adicionarEmpresa(Context ctx) {
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());
        try {
            if ((!ctx.formParam("nome").isEmpty()) || (!ctx.formParam("cidade").isEmpty())) {
                Empresa empresa = new Empresa();
                empresa.setNome(ctx.formParam("nome"));
                empresa.setSite(ctx.formParam("site"));
                empresa.setInstagram(ctx.formParam("instagram"));
                empresa.setLinkedin(ctx.formParam("linkedin"));
                empresa.setGithub(ctx.formParam("github"));
                empresa.setTelefone(ctx.formParam("telefone"));

                Endereco endereco = new Endereco();
                endereco.setCidade(ctx.formParam("cidade"));
                endereco.setEstado(ctx.formParam("estado"));
                endereco.setBairro(ctx.formParam("bairro"));
                endereco.setRua(ctx.formParam("rua"));
                endereco.setNumero(ctx.formParam("numero"));
//            empresaService.adicionarEndereco(endereco);
                empresa.setEndereco(empresaService.adicionarEndereco(endereco));
                empresaService.adicionarEmpresa(empresa);
            }
            ctx.redirect("/empresas");
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("Erro ao adicionar empresa: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void removerEmpresa(Context ctx) {
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());
        String id = ctx.pathParam("id");
        empresaService.removerEmpresa(id);
        ctx.redirect("/empresas");
    }
}
