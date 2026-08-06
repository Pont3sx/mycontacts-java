package dao;

import exceptions.ContatoNaoEncontradoException;
import model.Contato;
import model.ContatoComercial;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContatoDAO {
    private final Connection conexaoInjetada;

    public ContatoDAO() {
        this.conexaoInjetada = null;
    }

    public ContatoDAO(Connection conexao) {
        this.conexaoInjetada = conexao;
    }

    private Connection obterConexao() throws SQLException {
        return conexaoInjetada != null ? conexaoInjetada : Conexao.getConexao();
    }

    public void inserir(Contato contato) throws SQLException {
        String sql = "INSERT INTO contatos (nome, telefone, email, empresa) VALUES (?, ?, ?, ?)";

        try (Connection conexao = obterConexao();
            PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());

            if (contato instanceof ContatoComercial contatoComercial) {
                stmt.setString(4, contatoComercial.getEmpresa());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            stmt.executeUpdate();

            try (ResultSet chaves = stmt.getGeneratedKeys()) {
                if (chaves.next()) {
                    contato.setId(chaves.getInt(1));
                }
            }
        }
    }

    public List<Contato> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM contatos ORDER BY nome";
        List<Contato> contatoList = new ArrayList<>();

        try (Connection connection = obterConexao();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                contatoList.add(mapearContato(resultSet));
            }
            return contatoList;
        }
    }

    public List<Contato> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM contatos WHERE nome LIKE ? ORDER BY nome";
        List<Contato> contatoList = new ArrayList<>();

        try (Connection connection = obterConexao();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + nome + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    contatoList.add(mapearContato(resultSet));
                }
            }
        }
        return contatoList;
    }

    public Contato buscarPorId(int id) throws SQLException, ContatoNaoEncontradoException {
        String sql = "SELECT * FROM contatos WHERE id = ?";

        try (Connection connection = obterConexao();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearContato(resultSet);
                }
            }
        }
        throw new ContatoNaoEncontradoException("Contato com o id: " + id + " não encontrado no banco!");
    }

    public void atualizar(Contato contato) throws SQLException, ContatoNaoEncontradoException {
        String sql = "UPDATE contatos SET nome = ?, telefone = ?, email = ?, empresa = ? WHERE id = ?";

        try (Connection connection = obterConexao();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, contato.getNome());
            statement.setString(2, contato.getTelefone());
            statement.setString(3, contato.getEmail());

            if (contato instanceof ContatoComercial contatoComercial) {
                statement.setString(4, contatoComercial.getEmpresa());
            } else {
                statement.setNull(4, Types.VARCHAR);
            }

            statement.setInt(5, contato.getId());

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new ContatoNaoEncontradoException("Nenhum contato com o id:" + contato.getId() + " para atualizar." );
            }
        }
    }

    public void deletar(int id) throws SQLException, ContatoNaoEncontradoException {
        String sql = ("DELETE FROM contatos WHERE id = ?");

        try (Connection connection = obterConexao();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new ContatoNaoEncontradoException("Contato com o id:" + id + " não encontrado para exclusão." );
            }
        }
    }

    private Contato mapearContato(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String nome = resultSet.getString("nome");
        String telefone = resultSet.getString("telefone");
        String email = resultSet.getString("email");
        String empresa = resultSet.getString("empresa");

        Contato contato;
        if (empresa != null && !empresa.isBlank()) {
            contato = new ContatoComercial(nome, telefone, email, empresa);
        } else {
            contato = new Contato(nome, telefone, email);
        }
        contato.setId(id);
        return contato;
    }
}