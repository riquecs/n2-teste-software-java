package br.org.catolicasc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoServiceTest {

    @Test
    void deveAplicarPromocaoProgressivaParaTresItens() {
        // 1. Arrange (Preparação)
        // O IntelliJ vai marcar tudo abaixo em VERMELHO. Isso é o TDD!
        CarrinhoService service = new CarrinhoService();
        Produto p1 = new Produto("Produto A", 100.0);
        Produto p2 = new Produto("Produto B", 100.0);
        Produto p3 = new Produto("Produto C", 100.0);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(p1);
        carrinho.adicionarItem(p2);
        carrinho.adicionarItem(p3);

        // 2. Act (Ação)
        // Tenta calcular o total, esperando que a promoção seja aplicada
        double total = service.calcularTotal(carrinho);

        // 3. Assert (Verificação)
        // Total esperado = (100 + 100 + 100) * 0.90 (10% de desconto) = 270.0
        assertEquals(270.0, total);
    }
}