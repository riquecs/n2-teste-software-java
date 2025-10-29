package br.org.catolicasc;

public class CarrinhoService {

    private static final int MIN_ITENS_PARA_PROMOCAO = 3;
    private static final double PERCENTUAL_DESCONTO_PROMOCAO = 0.10; // 10%

    public double calcularTotal(Carrinho carrinho) {

        double totalBruto = 0.0;
        for (Produto p : carrinho.getItens()) {
            totalBruto += p.getPreco();
        }

        //Usando as constantes
        if (carrinho.getQuantidadeItens() >= MIN_ITENS_PARA_PROMOCAO) {
            double desconto = totalBruto * PERCENTUAL_DESCONTO_PROMOCAO;
            return totalBruto - desconto;
        }

        return totalBruto;
    }
}