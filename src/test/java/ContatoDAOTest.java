import dao.ContatoDAO;
import exceptions.ContatoNaoEncontradoException;
import model.Contato;
import model.ContatoComercial;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContatoDAOTest {
    private Connection conexaoTeste;
    private ContatoDAO dao;

    // Roda ANTES de cada @Test — prepara banco limpo
    @BeforeEach
    void configurar() throws SQLException {
        // H2 em memória simulando o MySQL
        conexaoTeste = DriverManager.getConnection("jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1", "sa", "");

        String criarTabela = """
                CREATE TABLE IF NOT EXISTS contatos (
                    id       INT          AUTO_INCREMENT PRIMARY KEY,
                    nome     VARCHAR(100) NOT NULL,
                    telefone VARCHAR(20),
                    email    VARCHAR(100),
                    empresa  VARCHAR(100)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;
        try (Statement stmt = conexaoTeste.createStatement()) {
            stmt.execute(criarTabela);
        }

        //DAO usa H2 em vez de MySQL
        dao = new ContatoDAO(conexaoTeste);
    }

    // Roda DEPOIS de cada @Test — limpa para o próximo começar do zero
    @AfterEach
    void encerrar() throws SQLException {
        try (Statement statement = conexaoTeste.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS contatos");
        }
        if (conexaoTeste != null && !conexaoTeste.isClosed()) {
            conexaoTeste.close();
        }
    }

    @Test
    @DisplayName("inserir contato simples deve atribuir id gerado pelo banco")
    void inserir_contatoSimples_deveAtribuirIdDoBanco() throws SQLException {
        Contato c = new Contato("Ana Silva", "11999990000", "ana@email.com");
        dao.inserir(c);
        assertTrue(c.getId() > 0);
    }

    @Test
    @DisplayName("inserir ContatoComercial deve salvar campo empresa")
    void inserir_contatoComercial_deveSalvarEmpresa() throws SQLException, ContatoNaoEncontradoException {
        ContatoComercial cc = new ContatoComercial("Carlos", "111", "carlos@emp.com", "TechCo");
        dao.inserir(cc);

        Contato buscado = dao.buscarPorId(cc.getId());
        assertInstanceOf(ContatoComercial.class, buscado);
        assertEquals("TechCo", ((ContatoComercial) buscado).getEmpresa());
    }

    @Test
    @DisplayName("buscarTodos em banco vazio deve retornar lista vazia")
    void buscarTodos_bancoVazio_deveRetornarListaVazia() throws SQLException {
        assertTrue(dao.buscarTodos().isEmpty());
    }

    @Test
    @DisplayName("buscarTodos deve retornar todos os contatos inseridos")
    void buscarTodos_aposInserir_deveRetornarTodos() throws SQLException {
        dao.inserir(new Contato("Ana", "111", "ana@x.com"));
        dao.inserir(new Contato("Bruno", "222", "bruno@x.com"));
        assertEquals(2, dao.buscarTodos().size());
    }

    @Test
    @DisplayName("buscarPorId deve encontrar contato existente")
    void buscarPorId_existente_deveRetornarContato() throws SQLException, ContatoNaoEncontradoException {
        Contato c = new Contato("Daniela", "333", "dani@x.com");
        dao.inserir(c);

        Contato encontrado = dao.buscarPorId(c.getId());
        assertEquals("Daniela", encontrado.getNome());
    }

    @Test
    @DisplayName("buscarPorId com id inexistente deve lançar exceção")
    void buscarPorId_inexistente_deveLancarExcecao() {
        assertThrows(ContatoNaoEncontradoException.class, () -> dao.buscarPorId(999));
    }

    @Test
    @DisplayName("buscarPorNome deve encontrar por correspondência parcial")
    void buscarPorNome_parcial_deveEncontrar() throws SQLException {
        dao.inserir(new Contato("Maria Silva", "111", "m@x.com"));
        dao.inserir(new Contato("Mariana Costa", "222", "mc@x.com"));
        dao.inserir(new Contato("João", "333", "j@x.com"));

        assertEquals(2, dao.buscarPorNome("Mari").size());
    }

    @Test
    @DisplayName("atualizar deve modificar os dados no banco")
    void atualizar_dadosValidos_deveModificarNoBanco() throws SQLException, ContatoNaoEncontradoException {
        Contato c = new Contato("Eduardo", "111", "edu@x.com");
        dao.inserir(c);

        c.setNome("Eduardo Santos");
        dao.atualizar(c);

        assertEquals("Eduardo Santos", dao.buscarPorId(c.getId()).getNome());
    }

    @Test
    @DisplayName("deletar contato existente deve removê-lo do banco")
    void deletar_existente_deveRemoverDoBanco() throws SQLException, ContatoNaoEncontradoException {
        Contato c = new Contato("Fernanda", "444", "fe@x.com");
        dao.inserir(c);

        dao.deletar(c.getId());
        assertTrue(dao.buscarTodos().isEmpty());
    }

    @Test
    @DisplayName("deletar id inexistente deve lançar exceção")
    void deletar_inexistente_deveLancarExcecao() {
        assertThrows(ContatoNaoEncontradoException.class, () -> dao.deletar(999));
    }

    @Test
    @DisplayName("deletar deve remover apenas o contato correto")
    void deletar_deveRemoverApenasUm() throws SQLException, ContatoNaoEncontradoException {
        Contato c1 = new Contato("Ana", "111", "ana@x.com");
        Contato c2 = new Contato("Bruno", "222", "bruno@x.com");
        dao.inserir(c1);
        dao.inserir(c2);

        dao.deletar(c1.getId());

        List<Contato> lista = dao.buscarTodos();
        assertEquals(1, lista.size());
        assertEquals("Bruno", lista.get(0).getNome());
    }

}