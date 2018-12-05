/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.md.restapi.resource;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.HeaderParam;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.edu.utfpr.md.restapi.dao.DocumentDAO;
import br.edu.utfpr.md.restapi.dao.PessoaDAO;
import br.edu.utfpr.md.restapi.model.Document;
import br.edu.utfpr.md.restapi.model.Pessoa;
import br.edu.utfpr.md.restapi.security.Autenticado;
import br.edu.utfpr.md.restapi.security.JWTUtil;
import com.auth0.jwt.JWTVerifyException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@Path("/document")
public class DocumentResource {

    private HttpServletRequest request;

    @Inject
    private DocumentDAO documentoDAO;

    @Inject
    private PessoaDAO pdao;

    @Inject
    private Result result;

    @Post("")
    @Consumes("application/json")
    public void save(Document documento) {

        try {
            documentoDAO.save(documento);
            result.use(Results.json())
                    .withoutRoot()
                    .from(documento)
                    .serialize();
        } catch (Exception ex) {
            result.use(Results.http()).setStatusCode(400);
            ex.printStackTrace();
        }
    }

    @Put("")
    @Consumes("application/json")
    public void update(Document documento, @HeaderParam("Authorization") String token) {
        Pessoa p = GetPessoa(token);
        if (documento.getIdpessoa() == p.getId()) {
            documentoDAO.update(documento);
            result.use(Results.json())
                    .withoutRoot()
                    .from(documento)
                    .serialize();
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Credenciais inválidas", "msg").serialize();
        }

    }

    @Delete("/{id}")
    public void delete(int id, @HeaderParam("Authorization") String token) {
        Document d = documentoDAO.getById(id);
        Pessoa p = GetPessoa(token);
        if (d.getIdpessoa() == p.getId()) {
            if (d == null) {
                result.use(Results.status()).notFound();
            } else {
                documentoDAO.delete(d);
                // result.use(Results.status()).ok(); ou
                result.use(Results.nothing());
            }
        }else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Credenciais inválidas", "msg").serialize();
        }

    }

    @Get("/{id}")
    public void getOne(int id) {
        Document documento = documentoDAO.getById(id);
        result.use(Results.json())
                .withoutRoot()
                .from(documento)
                .serialize();
    }

    @Autenticado
    @Get(value = {"", "/"})
    public void getAll() {
        List<Document> list = documentoDAO.findAll();

        result.use(Results.json())
                .withoutRoot()
                .from(list)
                .serialize();
    }

    @Autenticado
    @Get(value = {"/document/person/{id}"})
    public void getAllByUser(int id) {
        List<Document> list = documentoDAO.findAllbyUser(id);

        result.use(Results.json())
                .withoutRoot()
                .from(list)
                .serialize();
    }

    @Autenticado
    @Get(value = {"/document/person/{id}"})
    public void getAllByCategory(int id) {
        List<Document> list = documentoDAO.findAllbyCategory(id);

        result.use(Results.json())
                .withoutRoot()
                .from(list)
                .serialize();
    }

    private Pessoa GetPessoa(String token) {
        Pessoa u = new Pessoa();

        Map<String, Object> claims;
        try {
            claims = JWTUtil.decode(token);
            System.out.println(claims);

            Integer userId = (Integer) claims.get("user");

            if (userId == 0) {
                result.use(Results.http()).setStatusCode(401);
                result.use(Results.json())
                        .from("Credenciais inválidas", "msg").serialize();
            } else {
                System.out.println(userId);
                return pdao.getById(userId);
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | IllegalStateException | SignatureException | IOException
                | JWTVerifyException e) {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json()).from(e.getMessage(), "msg").serialize();
        }
        return u;
    }

}
