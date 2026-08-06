package repository;

import exceptions.ContatoNaoEncontradoException;

import java.util.List;

public interface Repositorio<T> {
    void adicionar(T item);

    T buscarPorId(int id) throws ContatoNaoEncontradoException;

    List<? extends T> listarTodos();

    void remover(int item) throws ContatoNaoEncontradoException;
}
