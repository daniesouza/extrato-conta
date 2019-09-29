package com.itau.extrato.dao.impl;

import com.itau.extrato.exception.MovimentacaoRepositoryException;
import com.itau.extrato.util.StringUtil;
import com.itau.extrato.dao.MovimentacaoRepository;
import com.itau.extrato.model.Movimentacao;
import com.itau.extrato.model.Pagamento;
import com.itau.extrato.model.Recebimento;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovimentacaoFileRepository implements MovimentacaoRepository {


    private SimpleDateFormat format = new SimpleDateFormat("dd-MMM", Locale.US);
    private final String MOVIMENTACAO_LOCATION = "movimentacao.log";

    @Override
    public List<Movimentacao> listAll() throws MovimentacaoRepositoryException {

        int lineNumber = 2;
        try (InputStream inputStream = new FileInputStream(new File(MOVIMENTACAO_LOCATION))) {

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            String header = br.readLine();

            List<Movimentacao> movimentacoes = new ArrayList<>();

            while ((line = br.readLine()) != null) {

                Movimentacao movimentacao = parseMovimentacao(line);
                movimentacoes.add(movimentacao);
                lineNumber++;
            }

            return movimentacoes;
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado "+e.getMessage());
            throw new MovimentacaoRepositoryException(e);
        } catch (IOException e) {
            throw new MovimentacaoRepositoryException(e);
        } catch (Exception e) {
            System.err.println("Erro ao ler linha "+lineNumber+" do arquivo. Arquivo Inválido.");
            throw new MovimentacaoRepositoryException(e);
        }
    }

    @Override
    public List<Pagamento> listPagamentos() throws MovimentacaoRepositoryException {

        int lineNumber = 2;
        try (InputStream inputStream = new FileInputStream(new File(MOVIMENTACAO_LOCATION))) {

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            String header = br.readLine();

            List<Pagamento> pagamentos = new ArrayList<>();

            while ((line = br.readLine()) != null) {

                Pagamento pagamento;

                if (isPagamento(line)) {
                    pagamento = (Pagamento) parseMovimentacao(line);
                    pagamentos.add(pagamento);
                    lineNumber++;
                }

            }

            return pagamentos;
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado "+e.getMessage());
            throw new MovimentacaoRepositoryException(e);
        } catch (IOException e) {
            throw new MovimentacaoRepositoryException(e);
        } catch (Exception e) {
            System.err.println("Erro ao ler linha "+lineNumber+" do arquivo. Arquivo Inválido.");
            throw new MovimentacaoRepositoryException(e);
        }
    }

    @Override
    public List<Recebimento> listRecebimentos() throws MovimentacaoRepositoryException {

        int lineNumber = 2;
        try (InputStream inputStream = new FileInputStream(new File(MOVIMENTACAO_LOCATION))) {

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            String header = br.readLine();

            List<Recebimento> recebimentos = new ArrayList<>();

            while ((line = br.readLine()) != null) {

                Recebimento recebimento;

                if (!isPagamento(line)) {
                    recebimento = (Recebimento) parseMovimentacao(line);
                    recebimentos.add(recebimento);
                    lineNumber++;
                }

            }

            return recebimentos;
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado "+e.getMessage());
            throw new MovimentacaoRepositoryException(e);
        } catch (IOException e) {
            throw new MovimentacaoRepositoryException(e);
        } catch (Exception e) {
            System.err.println("Erro ao ler linha "+lineNumber+" do arquivo. Arquivo Inválido.");
            throw new MovimentacaoRepositoryException(e);
        }
    }

    private Movimentacao parseMovimentacao(String line) throws ParseException {

        String data = line.substring(0, 6);
        String descricao = line.substring(10, 38).trim();
        BigDecimal value = getValue(line);
        String categoria = hasCategoria(line) ? line.substring(58).trim() : "";
        categoria = StringUtil.convertToAscII(categoria);

        Movimentacao movimentacao = isPagamento(line) ? new Pagamento() : new Recebimento();

        movimentacao.setData(format.parse(data));
        movimentacao.setDescricao(descricao);
        movimentacao.setValor(value);
        movimentacao.setCategoria(categoria);

        return movimentacao;
    }


    private boolean isPagamento(String line){

        BigDecimal value = getValue(line);

        return value.doubleValue() < 0.0;
    }

    private BigDecimal getValue(String line){

        BigDecimal value;
        String valueStr;

        if(hasCategoria(line)){
            valueStr = line.substring(38, 58);
        }else{
            valueStr = line.substring(38).trim();
        }

        valueStr = StringUtil.convertFromCurrency(valueStr);
        value = new BigDecimal(valueStr);

        return value;
    }

    private boolean hasCategoria(String line) {
        return line.length() > 58;
    }
}
