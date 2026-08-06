package model;

public class Contato {
    private int id;
    private String nome;
    private String telefone;
    private String email;

    //Construtores
    public Contato() {
    }

    public Contato(String nome, String telefone, String email) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    public int getId() {
        return this.id;
    }
    public String getNome() {
        return this.nome;
    }
    public String getTelefone() {
        return this.telefone;
    }
    public String getEmail() {
        return  this.email;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    //Métodos
    public void exibirContato() {
        System.out.println("Nome: " + nome);
        System.out.println("Telefone: " + telefone);
        System.out.println("Email: " + email);
    }

    @Override
    public String toString() {
        return "Contato [id:" + id + ", nome:'" + nome + "', telefone:'" + telefone + "', email:'" + email + "']";
    }
}
