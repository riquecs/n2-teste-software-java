package br.org.catolicasc;

public class CarrinhoService {

    public double calcularTotal(Carrinho carrinho) {

        //Calcular o total bruto (somando o preço de cada item)
        double totalBruto = 0.0;
        for (Produto p : carrinho.getItens()) {
            totalBruto += p.getPreco();
        }

        //Aplicar a regra de negócio (Promoção Progressiva)
        //Se tiver 3 ou mais itens, aplica 10% de desconto.
        if (carrinho.getQuantidadeItens() >= 3) {
            double desconto = totalBruto * 0.10;
            return totalBruto - desconto;
        }

        //Se não atingiu a regra, retorna apenas o total bruto
        return totalBruto;
    }
}