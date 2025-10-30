package br.org.catolicasc;

public class CarrinhoService {

    private static final int MIN_ITENS_PARA_PROMOCAO = 3;
    private static final double PERCENTUAL_DESCONTO_PROMOCAO = 0.10; // 10%

    private final EstoqueRepository estoqueRepository;
    private final FreteAPI freteAPI;

    public CarrinhoService(EstoqueRepository estoqueRepository, FreteAPI freteAPI) {
        this.estoqueRepository = estoqueRepository;
        this.freteAPI = freteAPI;
    }

    public double calcularTotal(Carrinho carrinho) {

        //Adicionamos esta verificação de argumento inválido
        if (carrinho == null) {
            throw new IllegalArgumentException("Carrinho não pode ser nulo");
        }

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

    public Pedido fecharCompra(Carrinho carrinho, String cep) {
        if (carrinho == null) {
            throw new IllegalArgumentException("Carrinho não pode ser nulo");
        }
        if (carrinho.getItens().isEmpty()) {
            throw new IllegalArgumentException("Carrinho não pode estar vazio");
        }

        //Verifica o estoque para cada item (Regra de Negócio)
        for (Produto p : carrinho.getItens()) {
            int quantidadeNoCarrinho = 1; //Para simplificar considera 1 de cada
            int estoqueDisponivel = estoqueRepository.getQuantidade(p);

            if (estoqueDisponivel < quantidadeNoCarrinho) {
                throw new IllegalStateException("Estoque insuficiente para " + p.getNome());
            }
        }

        //Calcula o subtotal (regras de promoção)
        double subtotal = this.calcularTotal(carrinho);

        //Calcula o frete (dependência externa mockada)
        double frete = freteAPI.calcularFrete(cep);

        //Reserva os itens no estoque (dependência externa mockada)
        for (Produto p : carrinho.getItens()) {
            estoqueRepository.reservar(p, 1);
        }

        //Cria e retorna o Pedido
        return new Pedido(carrinho.getItens(), subtotal, frete);
    }
}