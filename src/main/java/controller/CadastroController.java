package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.ValidadorEmail;

import java.sql.SQLException;

public class CadastroController {
    @FXML private TextField campoNome;
    @FXML
    private TextField campoTelefone;
    @FXML private TextField campoEmail;
    @FXML private TextField campoEmpresa;

    // Label que exibe mensagem de erro abaixo do formulário
    @FXML private Label labelErro;

    // ComboBox para escolher o tipo: "Simples" ou "Comercial"
    @FXML private ComboBox<String> comboTipo;

    private final Agenda agenda = new Agenda();

    @FXML
    public void initialize() {
        comboTipo.getItems().addAll("Simples", "Comercial");
        comboTipo.setValue("Simples");

        // Quando tipo mudar para "Comercial", habilita o campo empresa
        comboTipo.valueProperty().addListener((obs, antigo, novo) -> {
            campoEmpresa.setDisable(!"Comercial".equals(novo));
            if (!"Comercial".equals(novo)) {
                campoEmpresa.clear();
            }
        });

        campoEmpresa.setDisable(true);
    }

    @FXML
    private void onSalvar() {
        String nome     = campoNome.getText().trim();
        String telefone = campoTelefone.getText().trim();
        String email    = campoEmail.getText().trim();
        String empresa  = campoEmpresa.getText().trim();
        String tipo     = comboTipo.getValue();

        // Validação visual: nome obrigatório
        if (nome.isBlank()) {
            labelErro.setText("O nome é obrigatório.");
            campoNome.requestFocus();
            return;
        }

        // Validação visual: e-mail
        if (!ValidadorEmail.emailValido(email)) {
            labelErro.setText("Digite um e-mail válido (ex: nome@dominio.com).");
            campoEmail.requestFocus();
            return;
        }

        try {
            if ("Comercial".equals(tipo)) {
                agenda.adicionarContatoComercial(nome, telefone, email, empresa);
            } else {
                agenda.adicionarContato(nome, telefone, email);
            }

            fecharJanela();

        } catch (SQLException e) {
            labelErro.setText("Erro ao salvar no banco: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            labelErro.setText(e.getMessage());
        }
    }

    @FXML
    private void onCancelar() {
        fecharJanela();
    }

    private void fecharJanela() {
        Stage stage = (Stage) campoNome.getScene().getWindow();
        stage.close();
    }
}
