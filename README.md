# Trabalho N2 - Teste de Software (Cenário B)

Este projeto implementa o **Cenário B (Catálogo & Carrinho)** do trabalho N2 de Teste de Software, utilizando Java 21, Maven, JUnit 5 e Mockito.

O pipeline de CI deste projeto, executando todos os testes e gerando o relatório de cobertura, pode ser visto aqui:
**https://github.com/riquecs/n2-teste-software-java/actions**

---

## 1. Instruções de Execução

### Requisitos
* Java JDK 21
* Apache Maven

### Executando os Testes e Cobertura
Para executar todos os testes (18 testes no total) e gerar o relatório de cobertura (JaCoCo), execute o seguinte comando Maven na raiz do projeto:

```bash
  mvn verify
```
O comando irá compilar o código, executar todos os testes e, ao final, gerar o relatório de cobertura.
O relatório de cobertura pode ser acessado localmente abrindo o arquivo:target/site/jacoco/index.html 
As metas de cobertura ($\ge80\%$ linhas e $\ge70\%$ branches) 1 são validadas na classe de regras de negócio CarrinhoService.

## 2. Decisões de Design

### Arquitetura:
O projeto utiliza uma camada de Serviço (CarrinhoService) para conter as regras de negócio (TDD, promoção, reserva), separando-as das entidades (Produto, Carrinho).

### Injeção de Dependência:
O CarrinhoService recebe suas dependências externas (EstoqueRepository, FreteAPI) via construtor. Isso é crucial para permitir a substituição por Mocks e Stubs durante os testes.

### Stubs vs. Mocks (Item 5):
*  FreteAPI foi tratada como Mock (via Mockito). Ela representa uma dependência sem estado, onde apenas simulamos seu retorno (when(...).thenReturn(...)).
*  EstoqueRepository foi implementado como Stub (InMemoryEstoqueRepository). Este stub funcional (um repositório em memória) permite um teste de integração mais completo, onde verificamos não apenas o comportamento, mas também a mudança de estado (a baixa no estoque).

## 3. Mapa de Testes
Todos os testes de unidade e integração estão na classe br.org.catolicasc.CarrinhoServiceTest e cobrem os seguintes requisitos do trabalho:

### Testes de Regra (Item 4: Parametrizado):
* deveCalcularTotalCorretamenteParaDiferentesQuantidades: Utiliza @ParameterizedTest e @CsvSource para validar a regra de promoção progressiva  em múltiplos cenários (0 itens, 2 itens [sem desconto], 3 itens [com desconto], 5 itens [com desconto]).
* deveAplicarDescontoDoCupom_AposPromocao: Valida a regra de cupom  (DEZ, VINTE, INVALIDO) após o cálculo da promoção.
* deveAplicarPromocaoProgressivaEOCupom: Valida a interação das duas regras de desconto (promoção + cupom).

### Testes de Exceção (Item 3):
* deveLancarExcecaoQuandoCarrinhoForNulo: Valida o tratamento de entradas nulas (IllegalArgumentException).
* deveLancarExcecao_QuandoFecharCompra_ComEstoqueInsuficiente: Garante que o fluxo de compra é interrompido se o estoque (via Mock) for insuficiente, lançando IllegalStateException.

### Testes de Integração (Item 6):
* deveFecharCompraComSucessoSeTiverEstoqueECalcularFrete: Testa o fluxo completo (ponta-a-ponta) fecharCompra usando Mocks do Mockito. Valida se os métodos corretos (reservar) foram chamados (verify).
* deveFecharCompra_UsandoUmStubDeEstoqueEmMemoria: Testa o fluxo completo usando o Stub InMemoryEstoqueRepository  e valida a mudança de estado real no stub (a baixa no estoque).

### Teste de Performance (Item 7):
* deveFecharCompraRapidamente_TesteDePerformance: Utiliza assertTimeout  para garantir que o fluxo de compra seja executado dentro do limite de 200ms.

---
## Bônus: Teste de Contrato
O projeto implementa o bônus de Teste de Contrato. A classe `InMemoryEstoqueRepositoryContractTest` define e valida o 'contrato' da interface `EstoqueRepository`, garantindo que nossa implementação `InMemoryEstoqueRepository` se comporta como esperado (manipulando o estoque e lançando exceções corretamente).

## 4. Limites Conhecidos
* Quantidade de Itens: Para simplificar, o sistema trata cada item no carrinho como "quantidade 1". A lógica de fecharCompra e InMemoryEstoqueRepository precisaria ser adaptada para suportar um Carrinho com 5 unidades do "Produto A", por exemplo
* Persistência: O InMemoryEstoqueRepository é um stub volátil e não persiste dados.
