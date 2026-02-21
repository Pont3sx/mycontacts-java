package model;

public class ContatoComercial extends Contato {
    private String empresa;

    //Construtor
    ContatoComercial(String nome, String telefone, String email, String empresa) {
        super(nome, telefone, email);
        this.empresa = empresa;
    }

    public String getEmpresa() {
        return this.empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    // sobrescrevendo o método para mostrar a empresa também
    @Override
    public void exibirContato() {
        System.out.println("Nome: " + getNome());
        System.out.println("Telefone: " + getTelefone());
        System.out.println("Email: " + getEmail());
        System.out.println("Empresa: " + empresa);
    }
}
