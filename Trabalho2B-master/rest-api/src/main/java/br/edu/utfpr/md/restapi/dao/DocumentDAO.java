/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.md.restapi.dao;

import br.edu.utfpr.md.restapi.dao.GenericDAO;
import br.edu.utfpr.md.restapi.model.Document;
import java.util.List;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class DocumentDAO extends GenericDAO<Integer,Document> {
    public DocumentDAO() {
        super();
    }
    
   public List<Document> findAllbyUser(int iduser) {
        return entityManager.createQuery(("SELECT e FROM tb_document e  WHERE idpessoa = " + Integer.toString(iduser) )).getResultList();
    }
   
       
   public List<Document> findAllbyCategory(int idcat) {
        return entityManager.createQuery(("SELECT e FROM tb_document e  WHERE idcategoria = " + Integer.toString(idcat) )).getResultList();
    }
}
