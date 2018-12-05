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
import br.edu.utfpr.md.restapi.dao.CategoryDAO;
import br.edu.utfpr.md.restapi.dao.PessoaDAO;
import br.edu.utfpr.md.restapi.model.Category;
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
import javax.inject.Inject;

@Controller
@Path("/category")
public class CategoryResource {

    @Inject
    private CategoryDAO catdao;

    @Inject
    private PessoaDAO pdao;

    @Inject
    private Result result;

    @Post("")
    @Consumes("application/json")
    public void save(Category cat, @HeaderParam("Authorization") String token) {
        Pessoa p = GetPessoa(token);
        if (p.getRank() == 2) {
            try {
                catdao.save(cat);
                result.use(Results.json())
                        .withoutRoot()
                        .from(cat)
                        .serialize();
            } catch (Exception ex) {
                result.use(Results.http()).setStatusCode(400);
                ex.printStackTrace();
            }
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Credenciais inválidas", "msg").serialize();
        }

    }

    @Put("")
    @Consumes("application/json")
    public void update(Category cat, @HeaderParam("Authorization") String token) {

        Pessoa p = GetPessoa(token);
        if (p.getRank() == 2) {
            catdao.update(cat);
            result.use(Results.json())
                    .withoutRoot()
                    .from(cat)
                    .serialize();
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Credenciais inválidas", "msg").serialize();
        }
    }

    @Delete("/{id}")
    public void delete(int id, @HeaderParam("Authorization") String token) {
        Category cat = catdao.getById(id);
        Pessoa p = GetPessoa(token);
        if (p.getRank() == 2) {
            if (cat == null) {
                result.use(Results.status()).notFound();
            } else {
                catdao.delete(cat);
                // result.use(Results.status()).ok(); ou
                result.use(Results.nothing());
            }
        } else {
            result.use(Results.http()).setStatusCode(401);
            result.use(Results.json())
                    .from("Credenciais inválidas", "msg").serialize();
        }
    }

    @Get("/{id}")
    public void getOne(int id) {
        Category cat = catdao.getById(id);
        result.use(Results.json())
                .withoutRoot()
                .from(cat)
                .serialize();
    }

    @Autenticado
    @Get(value = {"", "/"})
    public void getAll() {
        List<Category> list = catdao.findAll();

        result.use(Results.json())
                .withoutRoot()
                .from(list)
                .serialize();
    }

    @Autenticado
    @Get(value = {"/person/{id}"})
    public void getAllByUser(int id) {
        List<Category> list = catdao.findAllbyUser(id);

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
