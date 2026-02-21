package controller;

import exceptions.ContatoNaoEncontradoException;
import model.Contato;

import java.util.ArrayList;

public class Agenda {
    //Arraylist que armazena os contatos
    private ArrayList<Contato> contatos = new ArrayList<>();

    //Métodos
    public void adicionarContato(String nomeContato, String numeroContato, String emailContato) {
        Contato novoContato;
        novoContato = new Contato(nomeContato, numeroContato, emailContato);
        contatos.add(novoContato);
    }

    public void listarContatos() {
        System.out.println("Sua Lista de Contatos: ");
        for (Contato contato : contatos) {
            contato.exibirContato();
        }
    }

    public void buscarPorNome(String nomeContato) throws ContatoNaoEncontradoException {
        Contato contatoEncontrado = null;

        for (Contato contato : contatos) {
            if (contato.getNome().equalsIgnoreCase(nomeContato)) {
                contatoEncontrado = contato;
                break;
            }
        }
        if (contatoEncontrado != null) {
            contatoEncontrado.exibirContato();
        } else {
            throw new ContatoNaoEncontradoException("Contato não encontrado.");
        }
    }

    public void removerContato(String nomeContato) throws ContatoNaoEncontradoException {
        Contato contatoEncontrado = null;

        for (Contato contato : contatos) {
            if (contato.getNome().equalsIgnoreCase(nomeContato)) {
                contatoEncontrado = contato;
                break;
            }
        }
        if (contatoEncontrado != null) {
            contatos.remove(contatoEncontrado);
        } else {
            throw new ContatoNaoEncontradoException("Esse contato não existe!");
        }
    }
}
