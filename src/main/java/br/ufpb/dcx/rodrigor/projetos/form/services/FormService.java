package br.ufpb.dcx.rodrigor.projetos.form.services;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EmpresaService;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EnderecoService;
import br.ufpb.dcx.rodrigor.projetos.form.model.*;
import br.ufpb.dcx.rodrigor.projetos.form.model.validadores.*;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FormService extends AbstractService {

    private final Map<String, Formulario> formularios = new LinkedHashMap<>();
    EnderecoService enderecoService = new EnderecoService(mongoDBConnector);
    ParticipanteService participanteService = new ParticipanteService(mongoDBConnector);
    EmpresaService empresaService = new EmpresaService(mongoDBConnector, enderecoService);

    public FormService(MongoDBConnector mongoDBConnector) {
        super(mongoDBConnector);
        iniciarFormularios();
    }

    public Formulario getFormulario(String id){
        return formularios.get(id);
    }

    public void iniciarFormularios(){
        Formulario form = new Formulario("empresa", "Empresa");
        form.setTitulo(new Titulo("Cadastro de Empresa"));
        Secao secaoDadosEmpresa = new Secao("Dados da Empresa");
        secaoDadosEmpresa.addCampo(new CampoTexto("nome", "Nome", new ValidadorTexto(5, 100), true));
        secaoDadosEmpresa.addCampo(new CampoTexto("site", "Site", new ValidadorSite(5, 100), false));
        secaoDadosEmpresa.addCampo(new CampoTexto("instagram", "Instagram", new ValidadorInstagram(5, 100), false));
        secaoDadosEmpresa.addCampo(new CampoTexto("linkedin", "Linkedin", new ValidadorLinkedin(5, 100), false));
        secaoDadosEmpresa.addCampo(new CampoTexto("github", "Github", new ValidadorGithub(5, 100), false));
        secaoDadosEmpresa.addCampo(new CampoTexto("telefone", "Telefone", new ValidadorTelefone(8, 11), true));
        form.addSecao(secaoDadosEmpresa);
        Secao secaoEndereco = new Secao("Endereço");
        secaoEndereco.addCampo(new CampoTexto("cidade", "Cidade", new ValidadorTexto(3, 50), true));
        List<String> opcoesEstado = enderecoService.getEstadosBrasil();
        secaoEndereco.addCampo(new CampoSelecao<>("estado", "Estado", opcoesEstado, new ValidadorEstado(), true));
        secaoEndereco.addCampo(new CampoTexto("bairro", "Bairro", new ValidadorTexto(5, 100), true));
        secaoEndereco.addCampo(new CampoTexto("rua", "Rua", new ValidadorTexto(5, 100), true));
        secaoEndereco.addCampo(new CampoTexto("numero", "Número", new ValidadorTexto(1, 5), true));
        secaoEndereco.addCampo(new CampoTexto("complemento", "Complemento", new ValidadorTexto(5, 100), false));
        form.addSecao(secaoEndereco);
        formularios.put(form.getId(), form);

        Formulario form2 = new Formulario("projeto", "Projeto");
        form.setTitulo(new Titulo("Cadastro de Projeto"));
        Secao secaoDadosProjeto = new Secao("Dados do Projeto");
        secaoDadosProjeto.addCampo(new CampoTexto("nome", "Nome", new ValidadorTexto(5, 50), true));
        secaoDadosProjeto.addCampo(new CampoTexto("descricao", "Descricao", new ValidadorTexto(5, 200), true));
        List<Participante> opcoesCordenador = participanteService.listarProfessores();
        secaoDadosProjeto.addCampo(new CampoSelecao<>("coordenador", "Coordenador", opcoesCordenador, new ValidadorCoordenador(opcoesCordenador), true));
        List<Empresa> opcoesEmpresa = empresaService.listarEmpresasFormulario();
        secaoDadosProjeto.addCampo(new CampoSelecao<>("empresa", "Empresa", opcoesEmpresa, new ValidadorEmpresa(opcoesEmpresa), true));
        form2.addSecao(secaoDadosProjeto);
        Secao secaoDatas = new Secao("Datas");
        secaoDatas.addCampo(new CampoData("dataInicio", "Data de Início", new ValidadorDataInicio(), true));
        secaoDatas.addCampo(new CampoData("dataEncerramento", "Data de Encerramento", new ValidadorDataEncerramento(), true));
        form2.addSecao(secaoDatas);
        formularios.put(form2.getId(), form2);
    }
}
