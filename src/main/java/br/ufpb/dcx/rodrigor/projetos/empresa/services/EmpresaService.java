package br.ufpb.dcx.rodrigor.projetos.empresa.services;

import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Endereco;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import br.ufpb.dcx.rodrigor.projetos.projeto.model.Projeto;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class EmpresaService {
    private List<Empresa> empresas = new ArrayList<>();
    private Long ultimoId = 1L;

    public EmpresaService() {
        Endereco e1 = new Endereco("João Pessoa", "Paraíba", "Ipês", "Rua Empresário Clóvis Rolim", "Bloco A, Sala 3001");
        empresas.add(new Empresa(ultimoId++,
                "Phoebus Tecnologia",
                "phoebus.com.br",
                "@phoebustecnologia",
                "linkedin.com/phoebustecnologia",
                "github.com/phoebustech",
                "(83) 9 1234-5678",
                e1));

        Endereco e2 = new Endereco("João Pessoa", "Paraíba", "Jaguaribe", "Av. João da Mata", "200");
        empresas.add(new Empresa(ultimoId++,
                "CODATA",
                "codata.pb.gov.br",
                "@codatapbgov",
                "linkedin.com/codatapbgov",
                "github.com/codatapbgov",
                "(83) 3208-4450",
                e2));
    }

    public List<Empresa> listarEmpresas() {
        return empresas;
    }

    public void adicionarEmpresa(Empresa empresa) {
        empresa.setId(ultimoId++);
        empresas.add(empresa);
    }

    public void atualizarEmpresa(Empresa empresaAtualizada) {
        empresas.replaceAll(empresa -> empresa.getId().equals(empresaAtualizada.getId()) ? empresaAtualizada : empresa);
    }

    public void removerEmpresa(Long id) {empresas.removeIf(empresa -> empresa.getId().equals(id));}
    }
