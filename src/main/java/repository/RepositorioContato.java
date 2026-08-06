package repository;

import exceptions.ContatoNaoEncontradoException;
import model.Contato;

import java.util.ArrayList;
import java.util.List;

public class RepositorioContato implements Repositorio<Contato> {
    private final ArrayList<Contato> contatosList = new ArrayList<>();

    // Contador para gerar ids sequenciais (simula o AUTO_INCREMENT do banco)
    private int contatorID = 1;

    @Override
    public void adicionar(Contato contato) {
        contato.setId(contatorID++);
        contatosList.add(contato);
    }


    @Override
    public Contato buscarPorId(int id) throws ContatoNaoEncontradoException {
        for (Contato contato : contatosList) {
            if (contato.getId() == id){
                return contato;
            }
        }
        throw new ContatoNaoEncontradoException("Contato com o ID: " + id + ", não foi encontrado.");
    }

    @Override
    public List<? extends Contato> listarTodos() {
        return new ArrayList<>(contatosList);
    }

    @Override
    public void remover(int id) throws ContatoNaoEncontradoException{
        boolean removido = contatosList.removeIf(contato -> contato.getId() == id);

        if (!removido) {
            throw new ContatoNaoEncontradoException("Contato com o ID: " + id + ", não foi encontrado para remoção.");
        }
    }

    public List<Contato> buscarPorNome(String nome) {
        List<Contato> resultado = new ArrayList<>();
        for (Contato contato : contatosList) {
            if (contato.getNome().toLowerCase().contains(nome.toLowerCase())) {
                resultado.add(contato);
            }
        }
        return resultado;
    }
}
