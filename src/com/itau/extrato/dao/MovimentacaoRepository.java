package com.itau.extrato.dao;

import com.itau.extrato.exception.MovimentacaoRepositoryException;
import com.itau.extrato.model.Movimentacao;
import com.itau.extrato.model.Pagamento;
import com.itau.extrato.model.Recebimento;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface MovimentacaoRepository {

    List<Movimentacao> listAll() throws MovimentacaoRepositoryException;
    List<Pagamento> listPagamentos() throws MovimentacaoRepositoryException;
    List<Recebimento> listRecebimentos() throws MovimentacaoRepositoryException;
}
