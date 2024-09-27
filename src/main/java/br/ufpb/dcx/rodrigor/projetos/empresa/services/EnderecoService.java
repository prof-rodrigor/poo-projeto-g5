package br.ufpb.dcx.rodrigor.projetos.empresa.services;

import br.ufpb.dcx.rodrigor.projetos.AbstractService;
import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Empresa;
import br.ufpb.dcx.rodrigor.projetos.empresa.model.Endereco;
import br.ufpb.dcx.rodrigor.projetos.participante.model.Participante;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
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

public class EnderecoService extends AbstractService {

    private final MongoCollection<Document> collection;


    public EnderecoService(MongoDBConnector mongoDBConnector) {
        super(mongoDBConnector);
        MongoDatabase database = mongoDBConnector.getDatabase("empresas");
        this.collection = database.getCollection("enderecos");
    }
    //    public void adicionarEndereco(Endereco endereco){
//        endereco.setId(String.valueOf(ultimoIdEndereco++));
//        enderecos.add(endereco);
//    }

    public List<Endereco> listarEnderecos() {
        List<Endereco> enderecos = new ArrayList<>();
        for (Document doc : collection.find()) {
            enderecos.add(documentToEndereco(doc));
        }
        return enderecos;
    }
    public Optional<Endereco> buscarEnderecoPorId(String id) {
        Document doc = collection.find(eq("_id", new ObjectId(id))).first();
        return Optional.ofNullable(doc).map(EnderecoService::documentToEndereco);
    }

    public String adicionarEndereco(Endereco endereco) {
        Document doc = enderecoToDocument(endereco);
        collection.insertOne(doc);
        String id = doc.getObjectId("_id").toString();
        endereco.setId(id);
        return id;
    }

    public void atualizarEndereco(Endereco enderecoAtualizado) {
        Document doc = enderecoToDocument(enderecoAtualizado);
        collection.replaceOne(eq("_id", new ObjectId(enderecoAtualizado.getId())), doc);
    }

    public static Endereco documentToEndereco(Document doc) {
        Endereco endereco = new Endereco();
        endereco.setNumero(doc.getString("numero"));
        endereco.setBairro(doc.getString("bairro"));
        endereco.setCidade(doc.getString("cidade"));
        endereco.setEstado(doc.getString("estado"));
        endereco.setComplemento(doc.getString("complemento"));
        endereco.setRua(doc.getString("rua"));
        endereco.setId(doc.getObjectId("_id").toHexString());

        return endereco;
    }


    public Document enderecoToDocument(Endereco endereco) {
        Document doc = new Document();

        if (endereco.getId() != null && endereco.getId().length() == 24) {
            doc.put("_id", new ObjectId(endereco.getId()));
        }
        doc.put("numero", endereco.getNumero());
        doc.put("bairro", endereco.getBairro());
        doc.put("cidade", endereco.getCidade());
        doc.put("complemento", endereco.getComplemento());
        doc.put("estado", endereco.getEstado());
        doc.put("rua", endereco.getRua());

        return doc;
    }
}
