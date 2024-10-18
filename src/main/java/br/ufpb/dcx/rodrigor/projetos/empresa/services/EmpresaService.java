package br.ufpb.dcx.rodrigor.projetos.empresa.services;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Endereco;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class EmpresaService extends AbstractService {

    private final MongoCollection<Document> collection;

    private List<Endereco> enderecos = new ArrayList<>();
    private Long ultimoIdEndereco = 1L;

    private EnderecoService enderecoService;

    private Empresa semEmpresa = new Empresa("Não há empresa vinculada ao projeto",
            "semEmpresa.com",
            "instagram.com/semEmpresa",
            "linkedin.com/semEmpresa",
            "github.com/semEmpresa",
            "0000000000000",
            null);

    private static final Logger logger = LogManager.getLogger();

    public EmpresaService(MongoDBConnector mongoDBConnector, EnderecoService enderecoService) {
        super(mongoDBConnector);
        MongoDatabase database = mongoDBConnector.getDatabase("empresas");
        this.collection = database.getCollection("empresas");
        this.enderecoService = enderecoService;

        Document query = new Document("nome", semEmpresa.getNome());
        long count = collection.countDocuments(query);

        if (count == 0) {
            adicionarEmpresa(semEmpresa);
        }
    }
//    public void adicionarEndereco(Endereco endereco){
//        endereco.setId(String.valueOf(ultimoIdEndereco++));
//        enderecos.add(endereco);
//    }

    public List<Empresa> listarEmpresas() {
        List<Empresa> empresas = new ArrayList<>();

        for (Document doc : collection.find()) {
            empresas.add(documentToEmpresa(doc));
        }

        Iterator<Empresa> iterator = empresas.iterator();

        while (iterator.hasNext()) {
            Empresa empresa = iterator.next();

            if (empresa.getNome().equals(semEmpresa.getNome())) {
                iterator.remove();
                break;
            }
        }

        return empresas;
    }
    public List<Empresa> listarEmpresasFormulario() {
        List<Empresa> empresas = new ArrayList<>();
        for (Document doc : collection.find()) {
            empresas.add(documentToEmpresa(doc));
        }
        return empresas;
    }

    public Optional<Empresa> buscarEmpresaPorId(String id) {
        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(doc).map(this::documentToEmpresa);
    }

    public void adicionarEmpresa(Empresa empresa) {
        Document doc = empresaToDocument(empresa);
        collection.insertOne(doc);
        empresa.setId(doc.getObjectId("_id").toString());
    }

    public void atualizarEmpresa(Empresa empresaAtualizada) {
        Document doc = empresaToDocument(empresaAtualizada);
        collection.replaceOne(eq("_id", new ObjectId(empresaAtualizada.getId())), doc);
    }

    public void removerEmpresa(String id) {
        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        String idEndereco = doc.getObjectId("endereco").toHexString();
        enderecoService.removerEndereco(idEndereco);
        collection.deleteOne(eq("_id", new ObjectId(id)));
    }

    public Empresa documentToEmpresa(Document doc) {
        EnderecoService enderecoService = new EnderecoService(mongoDBConnector);
        Empresa empresa = new Empresa();
        empresa.setNome(doc.getString("nome"));
        empresa.setId(doc.getObjectId("_id").toString());
        if ((doc.getObjectId("endereco").toHexString()).equals("5e6d8a3b9d7f6c1a8e9b5d4e")){
            empresa.setEndereco(null);
        } else{
            empresa.setEndereco(enderecoService.buscarEnderecoPorId(doc.getObjectId("endereco").toHexString()).get());
        }
        empresa.setSite(doc.getString("site"));
        empresa.setInstagram(doc.getString("instagram"));
        empresa.setLinkedin(doc.getString("linkedin"));
        empresa.setGithub(doc.getString("github"));
        empresa.setTelefone(doc.getString("telefone"));

        return empresa;
    }


    public Document empresaToDocument(Empresa empresa) {
        Document doc = new Document();

        if (empresa.getId() != null && empresa.getId().length() == 24) {
            doc.put("_id", new ObjectId(empresa.getId()));
        }
        doc.put("nome", empresa.getNome());
        if (empresa.getEndereco() == null){
            doc.put("endereco", new ObjectId("5e6d8a3b9d7f6c1a8e9b5d4e"));
        } else{
            doc.put("endereco", new ObjectId(empresa.getEndereco().getId()));
        }

        doc.put("site", empresa.getSite());
        doc.put("instagram", empresa.getInstagram());
        doc.put("linkedin", empresa.getLinkedin());
        doc.put("github", empresa.getGithub());
        doc.put("telefone", empresa.getTelefone());

        return doc;
    }
}
