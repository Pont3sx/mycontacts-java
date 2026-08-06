package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Conexao {
    private static final String URL = "jdbc:mysql://localhost:3306/mycontacts";
    private static final String USUARIO = "root";
    private static final String SENHA = "1234";

    private static Connection instancia = null;

    private Conexao() {}

    public static synchronized Connection getConexao() throws SQLException {
        if (instancia == null || instancia.isClosed()) {
            instancia = DriverManager.getConnection(URL, USUARIO, SENHA);
            criarTabelaSeNaoExistir(instancia);
        }
        return instancia;
    }

    private static void criarTabelaSeNaoExistir(Connection connection) throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS contatos (
                    id       INT          AUTO_INCREMENT PRIMARY KEY,
                    nome     VARCHAR(100) NOT NULL,
                    telefone VARCHAR(20),
                    email    VARCHAR(100),
                    empresa  VARCHAR(100)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        }
    }

    public static void fechar() {
       try {
           if (instancia == null && !instancia.isClosed()) {
               instancia.close();
           }
       } catch (SQLException e) {
           System.out.println("Erro ao fechar conexão: " + e.getMessage());
       }

    }
}
