package br.org.catolicasc;

public class CarrinhoService {

    private static final int MIN_ITEMS_PARA_PROMOCAO = 3;
    private static final double PERCENTUAL_DESCONTO_PROMOCAO = 0.10; // 10%

    private final EstoqueRepository estoqueRepository;
    private final FreteAPI freteAPI;

    public CarrinhoService(EstoqueRepository estoqueRepository, FreteAPI freteAPI) {
        this.estoqueRepository = estoqueRepository;
        this.freteAPI = freteAPI;
    }

    public double calcularTotal(Carrinho carrinho, String cupom) {

        if (carrinho == null) {
            throw new IllegalArgumentException("Carrinho não pode ser nulo");
        }

        double subtotal = 0.0;
        for (Produto p : carrinho.getItens()) {
            subtotal += p.getPreco();
        }

        if (carrinho.getQuantidadeItens() >= MIN_ITEMS_PARA_PROMOCAO) {
            double desconto = subtotal * PERCENTUAL_DESCONTO_PROMOCAO;
            subtotal = subtotal - desconto;
        }

        double descontoCupom = 0.0;
        if (cupom != null) {
            if (cupom.equals("DEZ")) {
                descontoCupom = subtotal * 0.10; //10%
            } else if (cupom.equals("VINTE")) {
                descontoCupom = subtotal * 0.20; //20%
            }
        }

        //Retorna o subtotal (com promoção) MENOS o desconto do cupom
        return subtotal - descontoCupom;
    }

    public Pedido fecharCompra(Carrinho carrinho, String cep, String cupom) {
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
        double subtotal = this.calcularTotal(carrinho, cupom);

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