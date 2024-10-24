package br.ufpb.dcx.rodrigor.projetos.login;

import br.ufpb.dcx.rodrigor.projetos.Keys;
import io.javalin.http.Context;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginController {
    private static final Logger logger = LogManager.getLogger(LoginController.class);

    // Usuário de exemplo para autenticação
    private final Usuario usuarioExemplo = new Usuario("admin", " Administrador", "admin");

    public void mostrarPaginaLogin(Context ctx) {
        String teste = ctx.queryParam("teste");
        if(teste != null){
            throw new RuntimeException("Erro de teste a partir do /login?teste=1");
        }

        ctx.render("login.html");
    }

    public void processarLogin(Context ctx) {
        verificar(ctx);
        logger.info(ctx);
        ctx.cookie("usuario_autenticado", "true", 3600);
    }
    public static void verificar(Context ctx) {
        UsuarioService usuarioService = ctx.appData(Keys.USUARIO_SERVICE.key());
        String login = ctx.formParam("login");
        String senha = ctx.formParam("senha");
        Usuario usuario = usuarioService.buscarUsuario(login, senha);
        if (usuario != null) {
            logger.info("Usuário autenticado com sucesso");
            ctx.sessionAttribute("usuario", usuario);
            ctx.redirect("/area-interna");
        } else {
            ctx.redirect("/login?erro=1");
        }
    }
    public void logout(Context ctx) {
        // Remove o usuário da sessão
        ctx.removeCookie("usuario_autenticado");
        logger.info("Usuário deslogado com sucesso.");
        ctx.redirect("/login");
    }
}
