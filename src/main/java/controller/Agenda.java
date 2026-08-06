package controller;

import dao.ContatoDAO;
import exceptions.ContatoNaoEncontradoException;
import model.Contato;
import model.ContatoComercial;
import utils.ValidadorEmail;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Agenda {
    private final ContatoDAO dao = new ContatoDAO();

    //Métodos
    public void adicionarContato(String nomeContato, String telefoneContato, String emailContato) throws SQLException, IllegalArgumentException {
        if (nomeContato == null || nomeContato.isBlank()) {
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        }
        // >> NOVO NA V2: validação de e-mail usando ValidadorEmail (utils/)
        if (!ValidadorEmail.emailValido(emailContato)) {
            throw new IllegalArgumentException("E-mail inválido: " + emailContato);
        }

        Contato novo = new Contato(nomeContato, telefoneContato, emailContato);
        dao.inserir(novo);
    }

    public void adicionarContatoComercial(String nomeContatoComercial, String telefoneContatoComercial, String emailContatoComercial, String empresa) throws SQLException, IllegalArgumentException {
        if (nomeContatoComercial == null || nomeContatoComercial.isBlank()) {
            throw new IllegalArgumentException("O nome não pode ser vazio.");
        }
        if (!ValidadorEmail.emailValido(emailContatoComercial)) {
            throw new IllegalArgumentException("E-mail inválido: " + emailContatoComercial);
        }

        ContatoComercial novo = new ContatoComercial(nomeContatoComercial, telefoneContatoComercial, emailContatoComercial, empresa);
        dao.inserir(novo);
    }

    public List<Contato> listarContatos() throws SQLException {
        return dao.buscarTodos();
    }

    public List<Contato> buscarPorNome(String nome) throws SQLException {
        return dao.buscarPorNome(nome);
    }

    public void removerContato(int id) throws SQLException, ContatoNaoEncontradoException {
        dao.deletar(id);
    }

    public void atualizarContato(Contato contato) throws SQLException, ContatoNaoEncontradoException {
        if (!ValidadorEmail.emailValido(contato.getEmail())) {
            throw new IllegalArgumentException("E-mail inválido: " + contato.getEmail());
        }
        dao.atualizar(contato);
    }
}
