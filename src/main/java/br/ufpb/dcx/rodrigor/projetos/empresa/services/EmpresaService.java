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

    private Empresa semEmpresa = new Empresa("Não há empresa vinculada ao projeto",
            "semEmpresa.com",
            "instagram.com/semEmpresa",
            "linkedin.com/semEmpresa",
            "github.com/semEmpresa",
            "0000000000000",
            null);

    private static final Logger logger = LogManager.getLogger();

    public EmpresaService(MongoDBConnector mongoDBConnector) {
        super(mongoDBConnector);
        MongoDatabase database = mongoDBConnector.getDatabase("empresas");
        this.collection = database.getCollection("empresas");

        Document query = new Document("nome", semEmpresa.getNome());
        long count = collection.countDocuments(query);

        if (count == 0) {
            adicionarEmpresa(semEmpresa);
        }
    }
    public void adicionarEndereco(Endereco endereco){
        endereco.setId(String.valueOf(ultimoIdEndereco));
        enderecos.add(endereco);
        ultimoIdEndereco++;
    }
    public Endereco buscarEnderecoPorId(String id){
        for (Endereco endereco : enderecos){
            if (endereco.getId() == id) return endereco;
        }
        return null;
    }



    public List<Empresa> listarEmpresas() {
        List<Empresa> empresas = new ArrayList<>();

        // Populando a lista de empresas a partir do banco de dados
        for (Document doc : collection.find()) {
            empresas.add(documentToEmpresa(doc));
        }

        // Usando iterador para evitar problemas ao remover durante a iteração
        Iterator<Empresa> iterator = empresas.iterator();

        while (iterator.hasNext()) {
            Empresa empresa = iterator.next();

            // Verifica se o nome da empresa é o mesmo que o de "semEmpresa"
            if (empresa.getNome().equals(semEmpresa.getNome())) {
                iterator.remove();  // Remove a empresa usando o iterador
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
        collection.deleteOne(eq("_id", new ObjectId(id)));
    }

    public Empresa documentToEmpresa(Document doc) {
        Empresa empresa = new Empresa();
        empresa.setNome(doc.getString("nome"));
        empresa.setId(doc.getObjectId("_id").toString());
        empresa.setEndereco(buscarEnderecoPorId(doc.getString("endereco")));
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
            doc.put("endereco", String.valueOf(0));
        } else{
            doc.put("endereco", empresa.getEndereco().getId());
        }

        doc.put("site", empresa.getSite());
        doc.put("instagram", empresa.getInstagram());
        doc.put("linkedin", empresa.getLinkedin());
        doc.put("github", empresa.getGithub());
        doc.put("telefone", empresa.getTelefone());

        return doc;
    }
}
