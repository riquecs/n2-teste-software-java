package br.org.catolicasc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CarrinhoServiceTest {

    @Test
    void deveLancarExcecaoQuandoCarrinhoForNulo() {
        CarrinhoService service = new CarrinhoService();
        //Verificamos se a exceção 'IllegalArgumentException' é lançada
        //quando tentamos chamar 'calcularTotal' com um carrinho nulo.
        assertThrows(IllegalArgumentException.class, () -> {
            service.calcularTotal(null);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "2, 100.0, 200.0", //Caso 1: 2 itens (sem desconto)
            "3, 100.0, 270.0", //Caso 2: 3 itens (10% desconto)
            "5, 100.0, 450.0", //Caso 3: 5 itens (10% desconto)
            "0, 100.0, 0.0"    //Caso 4: 0 itens (total 0)
    })
    void deveCalcularTotalCorretamenteParaDiferentesQuantidades(int quantidadeItens, double precoItem, double totalEsperado) {
        CarrinhoService service = new CarrinhoService();
        Carrinho carrinho = new Carrinho();

        for (int i = 0; i < quantidadeItens; i++) {
            carrinho.adicionarItem(new Produto("Item", precoItem));
        }

        double totalCalculado = service.calcularTotal(carrinho);

        assertEquals(totalEsperado, totalCalculado, 0.001);
    }
}