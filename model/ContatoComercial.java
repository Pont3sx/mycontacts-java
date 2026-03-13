package model;

public class ContatoComercial extends Contato {
    private String empresa;

    //Construtor
    public ContatoComercial(String nome, String telefone, String email, String empresa) {
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
        super.exibirContato();
        System.out.println("Empresa: " + empresa);
    }
}
