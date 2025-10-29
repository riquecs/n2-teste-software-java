package br.org.catolicasc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@ExtendWith(MockitoExtension.class)
public class CarrinhoServiceTest {

    @Mock
    private EstoqueRepository estoqueRepositoryMock; //Um mock que finge ser o EstoqueRepository

    @Mock
    private FreteAPI freteAPIMock; //Um mock que finge ser o FreteAPI

    private CarrinhoService service;

    @BeforeEach
    void setup() {
        //Inicializa o serviço, injetando os Mocks falsos nele
        service = new CarrinhoService(estoqueRepositoryMock, freteAPIMock);
    }

    @Test
    void deveLancarExcecaoQuandoCarrinhoForNulo() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.calcularTotal(null);
        });
    }

    @ParameterizedTest
    @CsvSource({
            "2, 100.0, 200.0",
            "3, 100.0, 270.0",
            "5, 100.0, 450.0",
            "0, 100.0, 0.0"
    })
    void deveCalcularTotalCorretamenteParaDiferentesQuantidades(int quantidadeItens, double precoItem, double totalEsperado) {
        Carrinho carrinho = new Carrinho();
        for (int i = 0; i < quantidadeItens; i++) {
            carrinho.adicionarItem(new Produto("Item", precoItem));
        }
        double totalCalculado = service.calcularTotal(carrinho);
        assertEquals(totalEsperado, totalCalculado, 0.001);
    }
}