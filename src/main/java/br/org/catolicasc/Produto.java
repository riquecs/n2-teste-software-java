package br.org.catolicasc;

public class Produto {
    private String nome;
    private double preco;

    public Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    //O service vai precisar deste método para calcular o total
    public double getPreco() {
        return preco;
    }

    public String getNome() {
        return nome;
    }
}