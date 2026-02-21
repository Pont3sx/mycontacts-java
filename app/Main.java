package app;

import controller.Agenda;
import exceptions.ContatoNaoEncontradoException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean SistemaEmExecucao = true;

        Scanner leitorInput = new Scanner(System.in);

        Agenda agenda = new Agenda();

        System.out.println("Bem-vindo ao MyContacts!");
        while (SistemaEmExecucao) {
            System.out.println("====AGENDA DE CONTATOS====");
            System.out.println("Digite 1 para Adicionar novo Contato");
            System.out.println("Digite 2 para Listar os Contatos");
            System.out.println("Digite 3 para Buscar Contato por Nome");
            System.out.println("Digite 4 para Remover Contato");
            System.out.println("Digite 5 para Sair");
            System.out.print("Digite um número: ");

            int numeroMenu = leitorInput.nextInt();
            leitorInput.nextLine();
            switch (numeroMenu) {
                case 1:
                    System.out.println("Digite um Nome para adicionar ao contato: ");
                    String novoNome = leitorInput.nextLine();

                    System.out.println("Digite um Telefone para adicionar ao contato: ");
                    String novoTelefone = leitorInput.nextLine();

                    System.out.println("Digite um E-mail para adicionar ao contato: ");
                    String novoEmail = leitorInput.nextLine();

                    agenda.adicionarContato(novoNome, novoTelefone, novoEmail);
                    break;
                case 2:
                    agenda.listarContatos();
                    break;
                case 3:
                    System.out.println("Digite um Nome para buscar:");
                    String nomeBuscado = leitorInput.nextLine();

                    try {
                        agenda.buscarPorNome(nomeBuscado);
                    } catch (ContatoNaoEncontradoException excecao) {
                        System.out.println(excecao.getMessage());
                    }
                    break;
                case 4:
                    System.out.println("Digite um Nome para ser removido: ");
                    String nomeRemovido = leitorInput.nextLine();

                    try {
                        agenda.removerContato(nomeRemovido);
                    } catch (ContatoNaoEncontradoException excecao) {
                        System.out.println(excecao.getMessage());
                    }
                    break;
                case 5:
                    System.out.println("Encerrando Sitema...");
                    SistemaEmExecucao = false;
                    break;
                default:
                    System.out.println("Opção inexistente! Digite outro número.");
            }
        }
        leitorInput.close();
    }
}
