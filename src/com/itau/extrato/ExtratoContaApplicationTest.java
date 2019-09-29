package com.itau.extrato;

import com.itau.extrato.exception.MovimentacaoRepositoryException;
import com.itau.extrato.exception.MovimentacaoServiceException;
import com.itau.extrato.model.Movimentacao;
import com.itau.extrato.model.Pagamento;
import com.itau.extrato.model.Recebimento;
import com.itau.extrato.service.MovimentacaoService;
import com.itau.extrato.service.impl.MovimentacaoServiceImpl;
import com.itau.extrato.util.StringUtil;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ExtratoContaApplicationTest {

    private MovimentacaoService movimentacaoService;
    private SimpleDateFormat format;

    public ExtratoContaApplicationTest() {
        this.movimentacaoService = new MovimentacaoServiceImpl();
        this.format = new SimpleDateFormat("dd-MMM", Locale.US);
    }

    public static void main(String[] args) {

        ExtratoContaApplicationTest test = new ExtratoContaApplicationTest();
        try{
            test.testMovimentacoesOrdenadoData();
            test.testTotalGastosCategoria();
            test.testMaiorGastoCategoria();
            test.testMaiorGastoMes();
            test.testTotalGastos();
            test.testTotalRecebimentos();
            test.testSaldoTotalMovimentacoes();
        }catch (MovimentacaoRepositoryException ex){
            System.err.println("FALHA AO BUSCAR DADOS DO REPOSITORY.");
            ex.printStackTrace();
        }catch (MovimentacaoServiceException ex){
            System.err.println("FALHA AO PROCESSAR INFORMAÇÕES DE EXTRATO.");
            ex.printStackTrace();
        }

    }


    private void testMovimentacoesOrdenadoData() {

        List<Movimentacao> movimentacoes = movimentacaoService.listTotalMovimentacoes();

        System.out.println("\n******************* MOVIMENTACOES ORDENADO POR DATA ********************\n");

        String data = StringUtil.paddingRight("Data", 20);
        String descricao = StringUtil.paddingRight("Descricao", 30);
        String valor = StringUtil.paddingRight("Valor", 30);
        String categoria = StringUtil.paddingRight("Categoria", 15);

        String header = data + descricao + valor + categoria;

        System.out.println(header);

        for (Movimentacao movimentacao : movimentacoes) {

            data = StringUtil.paddingRight(format.format(movimentacao.getData()), 20);
            descricao = StringUtil.paddingRight(movimentacao.getDescricao(), 30);
            valor = StringUtil.paddingRight(movimentacao.getValor().toString(), 30);

            if (movimentacao instanceof Pagamento) {
                categoria = StringUtil.paddingRight(((Pagamento) movimentacao).getCategoria(), 15);
            } else {
                categoria = "";
            }


            String line = data + descricao + valor + categoria;

            System.out.println(line);
        }
    }

    private void testTotalGastosCategoria() {

        System.out.println("\n\n\n******************* TOTAL DE GASTOS POR CATEGORIA ********************\n");

        List<Pagamento> gastosPorCategoria = movimentacaoService.listPagamentosPorCategoria();


        String categoria = StringUtil.paddingRight("Categoria", 20);
        String valor = StringUtil.paddingRight("Valor", 30);

        String header = categoria + valor;

        System.out.println(header);

        gastosPorCategoria.forEach((pagamento) -> {
            String line = StringUtil.paddingRight(pagamento.getCategoria(), 20) + StringUtil.paddingRight(pagamento.getValor().toString(), 30);
            System.out.println(line);
        });
    }


    private void testMaiorGastoCategoria() {

        System.out.println("\n\n\n******************* CATEGORIA QUE O CLIENTE MAIS GASTOU ********************\n");

        Pagamento pagamento = movimentacaoService.getMaiorPagamentoPorCategoria();

        String categoria = StringUtil.paddingRight("Categoria", 20);
        String valor = StringUtil.paddingRight("Valor", 30);

        String header = categoria + valor;

        System.out.println(header);

        String line = StringUtil.paddingRight(pagamento.getCategoria(), 20) + StringUtil.paddingRight(pagamento.getValor().toString(), 30);
        System.out.println(line);
    }


    private void testMaiorGastoMes() {

        System.out.println("\n\n\n******************* MES QUE O CLIENTE MAIS GASTOU ********************\n");

        Pagamento pagamento = movimentacaoService.getMaiorPagamentoPorCategoria();

        String data = StringUtil.paddingRight("Data", 20);
        String valor = StringUtil.paddingRight("Valor", 30);

        String header = data + valor;

        System.out.println(header);

        String line = StringUtil.paddingRight(pagamento.getCategoria(), 20) + StringUtil.paddingRight(pagamento.getValor().toString(), 30);
        System.out.println(line);
    }

    private void testTotalGastos() {

        System.out.println("\n******************* TOTAL DE GASTOS DO CLIENTE ********************\n");

        Pagamento pagamento = movimentacaoService.getTotalPagamentos();

        String header = StringUtil.paddingRight("TOTAL", 20);

        System.out.println(header);

        String line = StringUtil.paddingRight(pagamento.getValor().toString(), 20);

        System.out.println(line);
    }

    public void testTotalRecebimentos() {

        System.out.println("\n******************* TOTAL DE RECEBIMENTOS DO CLIENTE ********************\n");

        Recebimento recebimento = movimentacaoService.getTotalRecebimentos();

        String header = StringUtil.paddingRight("TOTAL", 20);

        System.out.println(header);

        String line = StringUtil.paddingRight(recebimento.getValor().toString(), 20);

        System.out.println(line);
    }

    private void testSaldoTotalMovimentacoes(){

        List<Movimentacao> movimentacoes = movimentacaoService.listTotalMovimentacoes();

        System.out.println("\n******************* SALDO TOTAL DE MOVIMENTACOES DO CLIENTE ********************\n");

        String header = StringUtil.paddingRight("QUANTIDADE TOTAL DE MOVIMENTACOES", 50)
                + StringUtil.paddingRight("SALDO TOTAL", 20);

        System.out.println(header);

        Double total = movimentacoes.stream().mapToDouble(pag -> pag.getValor().doubleValue()).sum();
        String linha = StringUtil.paddingRight("" + movimentacoes.size(), 50) + StringUtil.paddingRight(total.toString(), 20);

        System.out.println(linha);
    }
}
