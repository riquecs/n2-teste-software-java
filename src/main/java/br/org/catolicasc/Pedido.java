package br.org.catolicasc;

import java.util.List;

public class Pedido {
    private final List<Produto> itens;
    private final double subtotal; //Total dos produtos + promoção
    private final double frete;
    private final double totalFinal; //subtotal + frete

    public Pedido(List<Produto> itens, double subtotal, double frete) {
        this.itens = itens;
        this.subtotal = subtotal;
        this.frete = frete;
        this.totalFinal = subtotal + frete;
    }

    public double getSubtotal() { return subtotal; }
    public double getFrete() { return frete; }
    public double getTotalFinal() { return totalFinal; }
    public List<Produto> getItens() { return itens; }
}