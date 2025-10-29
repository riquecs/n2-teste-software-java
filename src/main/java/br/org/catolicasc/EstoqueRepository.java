package br.org.catolicasc;

public interface EstoqueRepository {

    int getQuantidade(Produto produto);
    void reservar(Produto produto, int quantidade);
}
