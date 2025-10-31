package br.org.catolicasc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * BÔNUS: Teste de Contrato (Item Bônus)
 * *Esta classe testa se a nossa implementação (InMemoryEstoqueRepository)
 * adere corretamente ao 'contrato' definido pela interface (EstoqueRepository).
 */
class InMemoryEstoqueRepositoryContractTest {

    private EstoqueRepository repository; //Testa contra a interface
    private Produto produtoA;

    @BeforeEach
    void setup() {
        //Inicializa a implementação específica que queremos testar
        InMemoryEstoqueRepository inMemoryRepo = new InMemoryEstoqueRepository();

        //Configura o estado inicial
        produtoA = new Produto("Produto A", 10.0);
        inMemoryRepo.adicionarProduto(produtoA, 20); //Começa com 20 em estoque

        //O teste vai usar a interface genérica
        this.repository = inMemoryRepo;
    }

    @Test
    void contrato_deveRetornarQuantidadeCorreta_QuandoProdutoExiste() {
        assertEquals(20, repository.getQuantidade(produtoA));
    }

    @Test
    void contrato_deveRetornarZero_QuandoProdutoNaoExiste() {
        Produto produtoB = new Produto("Produto B", 5.0);
        assertEquals(0, repository.getQuantidade(produtoB));
    }

    @Test
    void contrato_deveReservarEstoqueCorretamente_QuandoHaEstoque() {
        assertDoesNotThrow(() -> {
            repository.reservar(produtoA, 5); //Tenta reservar 5
        });

        assertEquals(15, repository.getQuantidade(produtoA)); //Deve sobrar 15
    }

    @Test
    void contrato_deveLancarExcecao_QuandoTentaReservarMaisQueOEstoque() {
        //Tenta reservar 21, mas só tem 20
        assertThrows(IllegalStateException.class, () -> {
            repository.reservar(produtoA, 21);
        });

        //Garante que o estoque não mudou (transação falhou)
        assertEquals(20, repository.getQuantidade(produtoA));
    }
}