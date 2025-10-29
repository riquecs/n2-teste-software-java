package br.org.catolicasc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CarrinhoServiceTest {

    @Test
    void deveAplicarPromocaoProgressivaParaTresItens() {

        CarrinhoService service = new CarrinhoService();
        Produto p1 = new Produto("Produto A", 100.0);
        Produto p2 = new Produto("Produto B", 100.0);
        Produto p3 = new Produto("Produto C", 100.0);

        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(p1);
        carrinho.adicionarItem(p2);
        carrinho.adicionarItem(p3);

        double total = service.calcularTotal(carrinho);

        assertEquals(270.0, total);
    }

    @Test
    void deveLancarExcecaoQuandoCarrinhoForNulo() {

        CarrinhoService service = new CarrinhoService();
        //Verificamos se a exceção 'IllegalArgumentException' é lançada
        //quando tentamos chamar 'calcularTotal' com um carrinho nulo.
        assertThrows(IllegalArgumentException.class, () -> {
            service.calcularTotal(null);
        });
    }
}