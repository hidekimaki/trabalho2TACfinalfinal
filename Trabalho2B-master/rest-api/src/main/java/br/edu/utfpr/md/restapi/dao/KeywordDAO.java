/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.md.restapi.dao;

import br.edu.utfpr.md.restapi.dao.GenericDAO;
import br.edu.utfpr.md.restapi.model.Document;
import br.edu.utfpr.md.restapi.model.Keyword;
import br.edu.utfpr.md.restapi.model.Pessoa;
import java.util.List;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class KeywordDAO extends GenericDAO<Integer,Keyword> {
    public KeywordDAO() {
        super();
    }
       public Keyword findAllbyName(String name) {
        List<Keyword> listatag;
        listatag = this.findAll();
        for(Keyword ky : listatag){
            if((ky.getName().equals(name))){
                return ky;
            }
	}
        return null;
    }
   
}
