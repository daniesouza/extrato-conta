package com.itau.extrato.service;

import com.itau.extrato.exception.MovimentacaoRepositoryException;
import com.itau.extrato.exception.MovimentacaoServiceException;
import com.itau.extrato.model.Movimentacao;
import com.itau.extrato.model.Pagamento;
import com.itau.extrato.model.Recebimento;

import java.util.List;

public interface MovimentacaoService {

    List<Movimentacao> listTotalMovimentacoes() throws MovimentacaoServiceException, MovimentacaoRepositoryException;

    List<Pagamento> listPagamentosPorCategoria() throws MovimentacaoServiceException, MovimentacaoRepositoryException;

    Pagamento getMaiorPagamentoPorCategoria() throws MovimentacaoServiceException, MovimentacaoRepositoryException;

    Pagamento getMaiorPagamentoMes() throws MovimentacaoServiceException, MovimentacaoRepositoryException;

    Pagamento getTotalPagamentos() throws MovimentacaoServiceException, MovimentacaoRepositoryException;

    Recebimento getTotalRecebimentos() throws MovimentacaoServiceException, MovimentacaoRepositoryException;


}
