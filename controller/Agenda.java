package controller;

import exceptions.ContatoNaoEncontradoException;
import model.Contato;
import model.ContatoComercial;

import java.util.ArrayList;

public class Agenda {
    //Arraylist que armazena os contatos
    private ArrayList<Contato> listaContatos = new ArrayList<>();

    //Métodos
    //Cria um novo contato e adiciona a lista
    public void adicionarContato(String nomeContato, String numeroContato, String emailContato) {
        Contato novoContato;
        novoContato = new Contato(nomeContato, numeroContato, emailContato);
        listaContatos.add(novoContato);
    }

    //Cria um novo contato comercial e adiciona a lista
    public void adicionarContatoComercial(String nomeContatoComercial, String numeroContatoComercial, String emailContatoComercial, String empresa) {
        ContatoComercial novoContatoComercial;
        novoContatoComercial = new ContatoComercial(nomeContatoComercial, numeroContatoComercial, emailContatoComercial, empresa);
        listaContatos.add(novoContatoComercial);

    }

    //Lista todos os contatos existentes
    public void listarContatos() {
        System.out.println("Sua Lista de Contatos: ");

        if (listaContatos.isEmpty()) {
            System.out.println("Nenhum contato cadastrado.");
            return;
        }

        for (Contato contato : listaContatos) {
            contato.exibirContato();
        }
    }

    //Busca um contato / ler um contato atravez do nome e exibe
    public void buscarPorNome(String nomeContato) throws ContatoNaoEncontradoException {
        Contato contatoEncontrado = null;

        for (Contato contato : listaContatos) {
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

    //Busca um contato / remove um contato
    public void removerContato(String nomeContato) throws ContatoNaoEncontradoException {
        Contato contatoEncontrado = null;

        for (Contato contato : listaContatos) {
            if (contato.getNome().equalsIgnoreCase(nomeContato)) {
                contatoEncontrado = contato;
                break;
            }
        }
        if (contatoEncontrado != null) {
            listaContatos.remove(contatoEncontrado);
        } else {
            throw new ContatoNaoEncontradoException("Esse contato não existe!");
        }
    }
}
