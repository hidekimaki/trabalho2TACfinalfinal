package br.edu.utfpr.md.restapi.dao;

import br.edu.utfpr.md.restapi.model.Pessoa;
import java.util.Iterator;
import java.util.List;
import javax.enterprise.context.RequestScoped;

@RequestScoped
public class PessoaDAO extends GenericDAO<Integer, Pessoa> {

    public PessoaDAO() {
        super();
    }

    public Pessoa getuser(String email, String senha) {
        return (Pessoa) entityManager.createQuery(("SELECT e FROM tb_pessoa e WHERE e.email LIKE :email and e.senha LIKE :senha"))
                .setParameter("senha", senha)
                .setParameter("email", email);
    }

    //gambiarra teste
    public Pessoa finduser(String email, String senha) {
        List<Pessoa> listapessoas ;
        listapessoas = this.findAll();
        //listapessoas = entityManager.createQuery(("SELECT e FROM tb_document e")).getResultList();
        for(Pessoa ps : listapessoas){
            if((ps.getEmail().equals(email)) && (ps.getSenha().equals(senha))){
                return ps;
            }
	}
        return null;
    }

}
