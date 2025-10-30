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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

    @Test
    void deveFecharCompraComSucessoSeTiverEstoqueECalcularFrete() {
        //Configura os produtos e o carrinho
        Produto p1 = new Produto("Produto A", 100.0);
        Produto p2 = new Produto("Produto B", 100.0);
        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(p1);
        carrinho.adicionarItem(p2); //2 itens = R$ 200.0 (sem promoção)

        String cep = "89250-000";

        //Dizemos ao mock do Estoque para fingir que tem 10 itens em estoque para QUALQUER produto
        when(estoqueRepositoryMock.getQuantidade(any(Produto.class))).thenReturn(10);

        //Dizemos ao mock do Frete para fingir que o frete para este CEP é R$ 25.0
        when(freteAPIMock.calcularFrete(cep)).thenReturn(25.0);

        //Tentamos fechar a compra
        Pedido pedido = service.fecharCompra(carrinho, cep);

        assertNotNull(pedido); //O pedido foi criado
        assertEquals(2, pedido.getItens().size());
        assertEquals(200.0, pedido.getSubtotal()); //Subtotal (sem promoção)
        assertEquals(25.0, pedido.getFrete()); //Frete mockado
        assertEquals(225.0, pedido.getTotalFinal()); //Total

        //Verifica se os mocks foram chamados
        //Queremos garantir que o serviço realmente tentou reservar os 2 itens
        verify(estoqueRepositoryMock, times(2)).reservar(any(Produto.class), eq(1));
    }

    @Test
    void deveLancarExcecao_QuandoFecharCompra_ComEstoqueInsuficiente() {
        //Configura um carrinho com 1 item
        Produto p1 = new Produto("Produto A", 100.0);
        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(p1);
        String cep = "89250-000";

        //Dizemos ao mock do Estoque para fingir que tem 0 itens em estoque
        when(estoqueRepositoryMock.getQuantidade(p1)).thenReturn(0);

        //Verificamos se o serviço lança a exceção correta
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.fecharCompra(carrinho, cep);
        });

        //Verifica se a mensagem de erro está correta
        assertEquals("Estoque insuficiente para Produto A", exception.getMessage());

        //Verifica que o 'reservar' nunca foi chamado
        verify(estoqueRepositoryMock, never()).reservar(any(), anyInt());
    }

    @Test
    void deveFecharCompra_UsandoUmStubDeEstoqueEmMemoria() {
        // ão usamos @Mock, criamos uma instância real do Stub
        InMemoryEstoqueRepository estoqueStub = new InMemoryEstoqueRepository();

        //Criamos o service injetando o Stub e um Mock para o frete
        CarrinhoService serviceComStub = new CarrinhoService(estoqueStub, freteAPIMock);

        //Prepara os dados no Stub
        Produto p1 = new Produto("Produto A", 100.0);
        estoqueStub.adicionarProduto(p1, 20); // Coloca 20 itens no 'banco de dados'

        Carrinho carrinho = new Carrinho();
        carrinho.adicionarItem(p1);
        String cep = "89250-000";

        //Configura o mock do Frete
        when(freteAPIMock.calcularFrete(cep)).thenReturn(10.0);

        Pedido pedido = serviceComStub.fecharCompra(carrinho, cep);

        assertNotNull(pedido);
        assertEquals(100.0, pedido.getSubtotal());
        assertEquals(10.0, pedido.getFrete());
        assertEquals(110.0, pedido.getTotalFinal());

        //O estoque de p1 deve ter diminuído de 20 para 19.
        assertEquals(19, estoqueStub.getQuantidade(p1));
    }
}