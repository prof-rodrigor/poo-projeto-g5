package br.ufpb.dcx.rodrigor.projetos.empresa.model;

public class Endereco {
    private String cidade, estado, rua, numero, complemento;

    public Endereco(String cidade, String estado, String rua, String numero) {
        testaEntrada(cidade);
        testaEntrada(estado);
        testaEntrada(rua);
        testaNumero(numero);
        this.cidade = cidade;
        this.estado = estado;
        this.rua = rua;
        this.numero = numero;
    }

    public Endereco() {
    }

    public void testaEntrada(String x){
        if (x == null || x.isEmpty()){ throw new IllegalArgumentException("É obrigatório escrever algo neste campo.");}
    }

    public void testaNumero(String x) {
        for (int i = 0; i < x.length(); i++) {
            char c = x.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("O número informado contém valores que não são números.");
            }
        }
    }
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        testaEntrada(cidade);
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        testaEntrada(estado);
        this.estado = estado;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        testaEntrada(rua);
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        testaNumero(numero);
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}