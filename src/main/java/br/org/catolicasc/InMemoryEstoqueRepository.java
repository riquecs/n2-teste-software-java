package br.org.catolicasc;

import java.util.HashMap;
import java.util.Map;

public class InMemoryEstoqueRepository implements EstoqueRepository {

    //Simula nossa 'tabela' de banco de dados (Produto -> Quantidade)
    private final Map<Produto, Integer> estoque = new HashMap<>();

    public void adicionarProduto(Produto produto, int quantidadeInicial) {
        estoque.put(produto, quantidadeInicial);
    }

    @Override
    public int getQuantidade(Produto produto) {
        //Retorna a quantidade do Map, ou 0 se o produto não existir
        return estoque.getOrDefault(produto, 0);
    }

    @Override
    public void reservar(Produto produto, int quantidade) {
        int estoqueAtual = getQuantidade(produto);
        if (estoqueAtual < quantidade) {
            throw new IllegalStateException("Estoque insuficiente no stub para " + produto.getNome());
        }
        //Atualiza o 'banco de dados' em memória
        estoque.put(produto, estoqueAtual - quantidade);
    }
}