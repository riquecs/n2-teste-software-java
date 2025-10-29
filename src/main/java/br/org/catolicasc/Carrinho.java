package br.org.catolicasc;

import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    //Uma lista para guardar os produtos
    private List<Produto> itens = new ArrayList<>();

    public void adicionarItem(Produto produto) {
        this.itens.add(produto);
    }

    //O service precisa saber quais produtos estão no carrinho
    public List<Produto> getItens() {
        return this.itens;
    }

    //O service precisa saber a *quantidade* de itens para a regra da promoção
    public int getQuantidadeItens() {
        return this.itens.size();
    }
}