/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.md.restapi.resource;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.view.Results;
import br.edu.utfpr.md.restapi.dao.KeywordDAO;
import br.edu.utfpr.md.restapi.model.Keyword;
import br.edu.utfpr.md.restapi.security.Autenticado;
import java.util.List;
import javax.inject.Inject;

@Controller
@Path("/tag")
public class TagResource {
    @Inject
    private KeywordDAO tagdao;

    @Inject
    private Result result;
    
    @Autenticado
    @Get(value = {"", "/"})
    public void getAll() {
        List<Keyword> list = tagdao.findAll();

        result.use(Results.json())
                .withoutRoot()
                .from(list)
                .serialize();
    }
    
    @Autenticado
    @Get(value = {"/{name}"})
    public void getbyName(String name) {
        Keyword list = tagdao.findAllbyName(name);

        result.use(Results.json())
                .withoutRoot()
                .from(list)
                .serialize();
    }
}
