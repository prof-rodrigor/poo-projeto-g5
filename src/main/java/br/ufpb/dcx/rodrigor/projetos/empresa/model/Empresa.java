package br.ufpb.dcx.rodrigor.projetos.empresa.model;

public class Empresa {
    private String nome, site, instagram, linkedin, github, telefone;

    private Endereco endereco;

    public Empresa(String nome, String site, String instagram, String linkedin, String github, String telefone, Endereco endereco) {
        testaEntrada(nome);
        testaEntrada(site);
        testaEntrada(instagram);
        testaEntrada(linkedin);
        testaEntrada(github);
        testaNumero(telefone);
        this.nome = nome;
        this.site = site;
        this.instagram = instagram;
        this.linkedin = linkedin;
        this.github = github;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public Empresa() {
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        testaEntrada(nome);
        this.nome = nome;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        testaEntrada(site);
        this.site = site;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        testaEntrada(instagram);
        this.instagram = instagram;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        testaEntrada(linkedin);
        this.linkedin = linkedin;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        testaEntrada(github);
        this.github = github;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        testaNumero(telefone);
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
