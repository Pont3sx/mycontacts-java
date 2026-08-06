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

    private boolean usaConexaoInjetada() {
        return conexaoInjetada != null;
    }

    public void inserir(Contato contato) throws SQLException {
        String sql = "INSERT INTO contatos (nome, telefone, email, empresa) VALUES (?, ?, ?, ?)";

        Connection conexao = obterConexao();

        try {
            try (PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

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
        } finally {
            if (!usaConexaoInjetada()) {
                conexao.close();
            }
        }
    }

    public List<Contato> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM contatos ORDER BY nome";

        List<Contato> contatos = new ArrayList<>();

        Connection conexao = obterConexao();

        try {
            try (PreparedStatement stmt = conexao.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    contatos.add(mapearContato(rs));
                }
            }

            return contatos;

        } finally {
            if (!usaConexaoInjetada()) {
                conexao.close();
            }
        }
    }

    public List<Contato> buscarPorNome(String nome) throws SQLException {
        String sql = "SELECT * FROM contatos WHERE nome LIKE ? ORDER BY nome";

        List<Contato> contatos = new ArrayList<>();

        Connection conexao = obterConexao();

        try {
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setString(1, "%" + nome + "%");

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        contatos.add(mapearContato(rs));
                    }
                }
            }

            return contatos;

        } finally {
            if (!usaConexaoInjetada()) {
                conexao.close();
            }
        }
    }

    public Contato buscarPorId(int id) throws SQLException, ContatoNaoEncontradoException {
        String sql = "SELECT * FROM contatos WHERE id = ?";

        Connection conexao = obterConexao();

        try {
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return mapearContato(rs);
                    }
                }
            }

        } finally {
            if (!usaConexaoInjetada()) {
                conexao.close();
            }
        }

        throw new ContatoNaoEncontradoException(
                "Contato com o id " + id + " não encontrado.");
    }

    public void atualizar(Contato contato)
            throws SQLException, ContatoNaoEncontradoException {

        String sql = """
                UPDATE contatos
                SET nome = ?, telefone = ?, email = ?, empresa = ?
                WHERE id = ?
                """;

        Connection conexao = obterConexao();

        try {
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setString(1, contato.getNome());
                stmt.setString(2, contato.getTelefone());
                stmt.setString(3, contato.getEmail());

                if (contato instanceof ContatoComercial contatoComercial) {
                    stmt.setString(4, contatoComercial.getEmpresa());
                } else {
                    stmt.setNull(4, Types.VARCHAR);
                }

                stmt.setInt(5, contato.getId());

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas == 0) {
                    throw new ContatoNaoEncontradoException(
                            "Nenhum contato com o id "
                                    + contato.getId()
                                    + " para atualizar.");
                }
            }

        } finally {
            if (!usaConexaoInjetada()) {
                conexao.close();
            }
        }
    }

    public void deletar(int id)
            throws SQLException, ContatoNaoEncontradoException {

        String sql = "DELETE FROM contatos WHERE id = ?";

        Connection conexao = obterConexao();

        try {
            try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

                stmt.setInt(1, id);

                int linhasAfetadas = stmt.executeUpdate();

                if (linhasAfetadas == 0) {
                    throw new ContatoNaoEncontradoException(
                            "Contato com o id "
                                    + id
                                    + " não encontrado para exclusão.");
                }
            }

        } finally {
            if (!usaConexaoInjetada()) {
                conexao.close();
            }
        }
    }

    private Contato mapearContato(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String telefone = rs.getString("telefone");
        String email = rs.getString("email");
        String empresa = rs.getString("empresa");

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