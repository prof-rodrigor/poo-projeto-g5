package br.ufpb.dcx.rodrigor.projetos.projeto.model;

public enum Categoria {
    PE("Extensão"),
    PP("Pesquisa"),
    PIE("Integração"),
    Other("Outro");

    private String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
