package br.ufpb.dcx.rodrigor.projetos.empresa.controllers;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Endereco;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EmpresaService;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EnderecoService;
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

    public void mostrarFormularioEdicao(Context ctx) {
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());

        String id = ctx.pathParam("id");
        Empresa empresa = empresaService.buscarEmpresaPorId(id).get();

        if(empresa != null) {
            ctx.attribute("empresa", empresa);
            ctx.render("/empresas/form_empresa.html");
        } else {
            System.out.println("Empresa com ID " + id + " não foi encontrado.");
            ctx.status(404).result("Empresa não encontrada");
        }
    }

    public void atualizarEmpresa(Context ctx) {
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());
        Empresa empresa = extrairEmpresa(ctx);
        String id = ctx.pathParam("id");
        empresa.setId(id);
        empresaService.atualizarEmpresa(empresa);
        ctx.redirect("/empresas");
    }

    public void adicionarEmpresa(Context ctx) {
        Empresa empresa = extrairEmpresa(ctx);
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());
        if(empresa != null) empresaService.adicionarEmpresa(empresa);
        ctx.redirect("/empresas");
    }

    public void removerEmpresa(Context ctx) {
        EmpresaService empresaService = ctx.appData(Keys.EMPRESA_SERVICE.key());
        String id = ctx.pathParam("id");
        empresaService.removerEmpresa(id);
        ctx.redirect("/empresas");
    }

    public static Empresa extrairEmpresa(Context ctx) {
        EnderecoService enderecoService = ctx.appData(Keys.ENDERECO_SERVICE.key());
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
            endereco.setComplemento(ctx.pathParam("complemento"));
            empresa.setEndereco(enderecoService.buscarEnderecoPorId(enderecoService.adicionarEndereco(endereco)).get());

            return empresa;
        }
        return null;
    }
}
