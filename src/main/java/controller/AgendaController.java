package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import exceptions.ContatoNaoEncontradoException;
import model.Contato;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AgendaController {
    @FXML private TableView<Contato> tabelaContatos;

    @FXML
    private TableColumn<Contato, Integer> colunaId;
    @FXML private TableColumn<Contato, String> colunaNome;
    @FXML private TableColumn<Contato, String>  colunaTelefone;
    @FXML private TableColumn<Contato, String>  colunaEmail;

    @FXML private TextField campoBusca;

    private final Agenda agenda = new Agenda();

    private final ObservableList<Contato> listaObservavel = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colunaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        tabelaContatos.setItems(listaObservavel);
        carregarContatos();
    }

    @FXML
    private void onAdicionar() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fxml/tela-cadastro.fxml")
            );
            Stage stage = new Stage();
            stage.setTitle("Novo Contato");
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait(); // espera a janela fechar antes de continuar

            carregarContatos(); // atualiza a lista após cadastro
        } catch (IOException e) {
            mostrarAlerta("Erro ao abrir tela de cadastro: " + e.getMessage());
        }
    }

    @FXML
    private void onRemover() {
        Contato selecionado = tabelaContatos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um contato na lista para remover.");
            return;
        }

        // Confirmação antes de deletar
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                "Deseja remover o contato \"" + selecionado.getNome() + "\"?",
                ButtonType.YES, ButtonType.NO);
        confirmacao.setTitle("Confirmar Remoção");
        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                try {
                    agenda.removerContato(selecionado.getId());
                    carregarContatos();
                } catch (SQLException | ContatoNaoEncontradoException e) {
                    mostrarAlerta("Erro ao remover: " + e.getMessage());
                }
            }
        });
    }

    @FXML
    private void onBuscar() {
        String texto = campoBusca.getText().trim();
        try {
            List<Contato> resultado = texto.isEmpty()
                    ? agenda.listarContatos()
                    : agenda.buscarPorNome(texto);

            listaObservavel.setAll(resultado);
        } catch (SQLException e) {
            mostrarAlerta("Erro na busca: " + e.getMessage());
        }
    }

    private void carregarContatos() {
        try {
            List<Contato> contatos = agenda.listarContatos();
            listaObservavel.setAll(contatos);
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar contatos: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Aviso");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
