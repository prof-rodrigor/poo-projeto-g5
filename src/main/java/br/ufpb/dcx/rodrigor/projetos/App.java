package br.ufpb.dcx.rodrigor.projetos;

import br.ufpb.dcx.rodrigor.projetos.db.MongoDBConnector;
import br.ufpb.dcx.rodrigor.projetos.empresa.controllers.EmpresaController;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EmpresaService;
import br.ufpb.dcx.rodrigor.projetos.empresa.services.EnderecoService;
import br.ufpb.dcx.rodrigor.projetos.form.controller.FormController;
import br.ufpb.dcx.rodrigor.projetos.form.services.FormService;
import br.ufpb.dcx.rodrigor.projetos.login.LoginController;
import br.ufpb.dcx.rodrigor.projetos.login.UsuarioService;
import br.ufpb.dcx.rodrigor.projetos.participante.controllers.ParticipanteController;
import br.ufpb.dcx.rodrigor.projetos.participante.services.ParticipanteService;
import br.ufpb.dcx.rodrigor.projetos.projeto.controllers.ProjetoController;
import br.ufpb.dcx.rodrigor.projetos.projeto.services.ProjetoService;
import io.javalin.Javalin;
import io.javalin.config.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;


public class App {
    private static final Logger logger = LogManager.getLogger();

    private static final int PORTA_PADRAO = 8000;
    private static final String PROP_PORTA_SERVIDOR = "porta.servidor";
    private static final String PROP_MONGODB_CONNECTION_STRING = "mongodb.connectionString";

    private final Properties propriedades;
    private MongoDBConnector mongoDBConnector = null;

    public App() {
        this.propriedades = carregarPropriedades();
    }

    public void iniciar() {
        Javalin app = inicializarJavalin();
        configurarPaginasDeErro(app);
        configurarRotas(app);

        // Lidando com exceções não tratadas
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Erro não tratado", e);
            ctx.status(500);
        });
    }
    private void registrarServicos(JavalinConfig config, MongoDBConnector mongoDBConnector) {
        ParticipanteService participanteService = new ParticipanteService(mongoDBConnector);
        EnderecoService enderecoService = new EnderecoService(mongoDBConnector);
        EmpresaService empresaService = new EmpresaService(mongoDBConnector, enderecoService);
        config.appData(Keys.PROJETO_SERVICE.key(), new ProjetoService(mongoDBConnector, participanteService, empresaService));
        config.appData(Keys.PARTICIPANTE_SERVICE.key(), participanteService);
        config.appData(Keys.EMPRESA_SERVICE.key(), empresaService);
        config.appData(Keys.ENDERECO_SERVICE.key(), enderecoService);
        config.appData(Keys.FORM_SERVICE.key(), new FormService(mongoDBConnector));
        config.appData(Keys.USUARIO_SERVICE.key(), new UsuarioService());
    }
    private void configurarPaginasDeErro(Javalin app) {
        app.error(404, ctx -> ctx.render("erro_404.html"));
        app.error(500, ctx -> ctx.render("erro_500.html"));
    }

    private Javalin inicializarJavalin() {
        int porta = obterPortaServidor();

        logger.info("Iniciando aplicação na porta {}", porta);

        Consumer<JavalinConfig> configConsumer = this::configureJavalin;

        // Adicionando configuração de arquivos estáticos
        return Javalin.create(config -> {
            config.staticFiles.add("/public");
            configureJavalin(config); // Manter as outras configurações
        }).start(porta);
    }

    private void configureJavalin(JavalinConfig config) {
        TemplateEngine templateEngine = configurarThymeleaf();

        config.events(event -> {
            event.serverStarting(() -> {
                mongoDBConnector = inicializarMongoDB();
                config.appData(Keys.MONGO_DB.key(), mongoDBConnector);
                registrarServicos(config, mongoDBConnector);
            });
            event.serverStopping(() -> {
                if (mongoDBConnector == null) {
                    logger.error("MongoDBConnector não deveria ser nulo ao parar o servidor");
                } else {
                    mongoDBConnector.close();
                    logger.info("Conexão com o MongoDB encerrada com sucesso");
                }
            });
        });
        config.staticFiles.add(staticFileConfig -> {
            staticFileConfig.directory = "/public";
            staticFileConfig.location = Location.CLASSPATH;
        });
        config.fileRenderer(new JavalinThymeleaf(templateEngine));

    }

    private int obterPortaServidor() {
        if (propriedades.containsKey(PROP_PORTA_SERVIDOR)) {
            try {
                return Integer.parseInt(propriedades.getProperty(PROP_PORTA_SERVIDOR));
            } catch (NumberFormatException e) {
                logger.error("Porta definida no arquivo de propriedades não é um número válido: '{}'", propriedades.getProperty(PROP_PORTA_SERVIDOR));
                System.exit(1);
            }
        } else {
            logger.info("Porta não definida no arquivo de propriedades, utilizando porta padrão {}", PORTA_PADRAO);
        }
        return PORTA_PADRAO;
    }

    private TemplateEngine configurarThymeleaf() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    private MongoDBConnector inicializarMongoDB() {
        String connectionString = propriedades.getProperty(PROP_MONGODB_CONNECTION_STRING);
        logger.info("Lendo string de conexão ao MongoDB a partir do application.properties");
        if (connectionString == null) {
            logger.error("O string de conexão ao MongoDB não foi definido no arquivo /src/main/resources/application.properties");
            logger.error("Defina a propriedade '{}' no arquivo de propriedades", PROP_MONGODB_CONNECTION_STRING);
            System.exit(1);
        }

        logger.info("Conectando ao MongoDB");
        MongoDBConnector db = new MongoDBConnector(connectionString);
        if (db.conectado("config")) {
            logger.info("Conexão com o MongoDB estabelecida com sucesso");
        } else {
            logger.error("Falha ao conectar ao MongoDB");
            System.exit(1);
        }
        return db;
    }

    private void configurarRotas(Javalin app) {
        LoginController loginController = new LoginController();

        // Rotas públicas
        app.get("/", ctx -> ctx.redirect("/login"));
        app.get("/login", loginController::mostrarPaginaLogin);
        app.post("/login", loginController::processarLogin);
        app.get("/logout", loginController::logout);

        app.before(ctx -> {
            String path = ctx.path();
            if (!path.equals("/login") && !path.equals("/")) {
                verificarAutenticacao(ctx);
            }
        });

        // Rotas protegidas
        app.get("/area-interna", ctx -> ctx.render("area_interna.html"));

        ProjetoController projetoController = new ProjetoController();
        app.get("/projetos", projetoController::listarProjetos);
        app.get("/projetos/novo", projetoController::mostrarFormulario);
        app.post("/projetos", projetoController::adicionarProjeto);
        app.get("/projetos/{id}/remover", projetoController::removerProjeto);
        app.get("/projetos/{id}/editar", projetoController::mostrarFormularioEdicao);
        app.post("/projetos/{id}/atualizar", projetoController::atualizarProjeto);

        ParticipanteController participanteController = new ParticipanteController();
        app.get("/participantes", participanteController::listarParticipantes);
        app.get("/participantes/novo", participanteController::mostrarFormularioCadastro);
        app.post("/participantes", participanteController::adicionarParticipante);
        app.get("/participantes/{id}/remover", participanteController::removerParticipante);

        EmpresaController empresaController = new EmpresaController();
        app.get("/empresas", empresaController::listarEmpresas);
        app.get("/empresas/novo", empresaController::mostrarFormulario);
        app.post("/empresas", empresaController::adicionarEmpresa);
        app.get("/empresas/{id}/remover", empresaController::removerEmpresa);
        app.get("/empresas/{id}/editar", empresaController::mostrarFormularioEdicao);
        app.post("/empresas/{id}/atualizar", empresaController::atualizarEmpresa);

        FormController formController = new FormController();
        app.get("/form/{formId}", formController::abrirFormulario);
        app.post("/form/{formId}", formController::validarFormulario);
    }

    private void verificarAutenticacao(Context ctx) {
        logger.info("Verificando autenticação para a rota: {}", ctx.path());
        String autenticado = ctx.cookie("usuario_autenticado");

        if (autenticado == null || !autenticado.equals("true")) {
            logger.warn("Acesso negado. Usuário não autenticado tentando acessar: {}", ctx.path());
            ctx.redirect("/login?erro=2");
        }
    }





    private Properties carregarPropriedades() {
        Properties prop = new Properties();
        try (InputStream input = App.class.getClassLoader().getResourceAsStream("application.properties")) {
            if(input == null){
                logger.error("Arquivo de propriedades /src/main/resources/application.properties não encontrado");
                logger.error("Use o arquivo application.properties.examplo como base para criar o arquivo application.properties");
                System.exit(1);
            }
            prop.load(input);
        } catch (IOException ex) {
            logger.error("Erro ao carregar o arquivo de propriedades /src/main/resources/application.properties", ex);
            System.exit(1);
        }
        return prop;
    }

    public static void main(String[] args) {
        try {
            new App().iniciar();
        } catch (Exception e) {
            logger.error("Erro ao iniciar a aplicação", e);
            System.exit(1);
        }
    }
}