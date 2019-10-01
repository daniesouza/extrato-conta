package com.itau.extrato.service.impl;

import com.itau.extrato.dao.MovimentacaoRepository;
import com.itau.extrato.dao.impl.MovimentacaoFileRepository;
import com.itau.extrato.dao.impl.MovimentacaoWSRepository;
import com.itau.extrato.exception.MovimentacaoServiceException;
import com.itau.extrato.model.Movimentacao;
import com.itau.extrato.model.Pagamento;
import com.itau.extrato.model.Recebimento;
import com.itau.extrato.service.MovimentacaoService;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class MovimentacaoServiceImpl implements MovimentacaoService {

    private MovimentacaoRepository movimentacaoFile;
    private MovimentacaoRepository movimentacaoWs;
    private SimpleDateFormat format = new SimpleDateFormat("dd-MMM", Locale.US);


    public MovimentacaoServiceImpl() {
        this.movimentacaoFile = new MovimentacaoFileRepository();
        this.movimentacaoWs = new MovimentacaoWSRepository();
    }


    @Override
    public List<Movimentacao> listTotalMovimentacoes() {

        List<Movimentacao> movimentacoes = movimentacaoFile.listAll();
        movimentacoes.addAll(movimentacaoWs.listAll());

        movimentacoes.sort(Movimentacao.ORDER_BY_DATA_ASC);

        return movimentacoes;

    }

    @Override
    public List<Pagamento> listPagamentosPorCategoria() {

        List<Pagamento> pagamentos = movimentacaoFile.listPagamentos();
        pagamentos.addAll(movimentacaoWs.listPagamentos());

        List<Pagamento> gastosPorCategoria = new ArrayList<>();

        try {


            Map<String, BigDecimal> gastosPorCategoriaMap = new TreeMap<>();

            pagamentos.forEach(pagamento -> {

                String key = pagamento.getCategoria().toLowerCase();


                BigDecimal value = gastosPorCategoriaMap.get(key);

                if (value == null) {
                    value = BigDecimal.ZERO;
                }

                value = value.add(pagamento.getValor());

                gastosPorCategoriaMap.put(key, value);

            });


            gastosPorCategoriaMap.forEach((key, value) -> {
                Pagamento pagamento = new Pagamento();
                pagamento.setCategoria(key);
                pagamento.setValor(value);
                gastosPorCategoria.add(pagamento);
            });

        } catch (Exception ex) {
            throw new MovimentacaoServiceException(ex);
        }


        return gastosPorCategoria;


    }

    @Override
    public Pagamento getMaiorPagamentoPorCategoria() {


        List<Pagamento> pagamentos = movimentacaoFile.listPagamentos();
        pagamentos.addAll(movimentacaoWs.listPagamentos());

        try {
            Map<String, BigDecimal> gastosPorCategoriaMap = new TreeMap<>();

            pagamentos.forEach(pagamento -> {

                String key = pagamento.getCategoria().toLowerCase();


                BigDecimal value = gastosPorCategoriaMap.get(key);

                if (value == null) {
                    value = BigDecimal.ZERO;
                }

                value = value.add(pagamento.getValor());

                gastosPorCategoriaMap.put(key, value);

            });

            Map.Entry<String, BigDecimal> min = Collections.min(gastosPorCategoriaMap.entrySet(),
                    Comparator.comparing(Map.Entry::getValue));


            Pagamento pagamento = new Pagamento();
            pagamento.setCategoria(min.getKey());
            pagamento.setValor(min.getValue());

            return pagamento;

        } catch (Exception ex) {
            throw new MovimentacaoServiceException(ex);
        }

    }

    @Override
    public Pagamento getMaiorPagamentoMes() {

        List<Pagamento> pagamentos = movimentacaoFile.listPagamentos();
        pagamentos.addAll(movimentacaoWs.listPagamentos());

        try {
            Map<String, BigDecimal> gastosPorData = new TreeMap<>();

            pagamentos.forEach(pagamento -> {

                String key = format.format(pagamento.getData());


                BigDecimal value = gastosPorData.get(key);

                if (value == null) {
                    value = BigDecimal.ZERO;
                }

                value = value.add(pagamento.getValor());

                gastosPorData.put(key, value);

            });


            Map.Entry<String, BigDecimal> min = Collections.min(gastosPorData.entrySet(), Comparator.comparing(Map.Entry::getValue));

            Pagamento pagamento = new Pagamento();
            pagamento.setData(format.parse(min.getKey()));
            pagamento.setValor(min.getValue());

            return pagamento;
        } catch (Exception ex) {
            throw new MovimentacaoServiceException(ex);
        }

    }

    @Override
    public Pagamento getTotalPagamentos() {

        List<Pagamento> pagamentos = movimentacaoFile.listPagamentos();
        pagamentos.addAll(movimentacaoWs.listPagamentos());

        try {

            double total = pagamentos.stream().mapToDouble(pag -> pag.getValor().doubleValue()).sum();
            Pagamento pagamento = new Pagamento();
            pagamento.setValor(BigDecimal.valueOf(total));

            return pagamento;
        } catch (Exception ex) {
            throw new MovimentacaoServiceException(ex);
        }


    }

    @Override
    public Recebimento getTotalRecebimentos() {

        List<Recebimento> recebimentos = movimentacaoFile.listRecebimentos();
        recebimentos.addAll(movimentacaoWs.listRecebimentos());

        try {
            double total = recebimentos.stream().mapToDouble(pag -> pag.getValor().doubleValue()).sum();

            Recebimento recebimento = new Recebimento();
            recebimento.setValor(BigDecimal.valueOf(total));

            return recebimento;
        } catch (Exception ex) {
            throw new MovimentacaoServiceException(ex);
        }

    }

}
