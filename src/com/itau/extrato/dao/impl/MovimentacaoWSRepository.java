package com.itau.extrato.dao.impl;

import com.google.gson.Gson;
import com.itau.extrato.exception.MovimentacaoRepositoryException;
import com.itau.extrato.util.StringUtil;
import com.itau.extrato.dao.MovimentacaoRepository;
import com.itau.extrato.model.Movimentacao;
import com.itau.extrato.model.MovimentacaoJson;
import com.itau.extrato.model.Pagamento;
import com.itau.extrato.model.Recebimento;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovimentacaoWSRepository implements MovimentacaoRepository {

    private SimpleDateFormat format = new SimpleDateFormat("dd/MMM", new Locale("pt", "BR"));
    private final String URL_WS = "https://my-json-server.typicode.com/cairano/backend-test";


    @Override
    public List<Movimentacao> listAll() throws MovimentacaoRepositoryException {

        List<Pagamento> pagamentos = listPagamentos();
        List<Recebimento> recebimentos = listRecebimentos();

        List<Movimentacao> movimentacoes = new ArrayList<>(pagamentos);
        movimentacoes.addAll(recebimentos);

        return movimentacoes;
    }

    @Override
    public List<Pagamento> listPagamentos() throws MovimentacaoRepositoryException {

        try {

            String json = readJsonFromUrl(URL_WS + "/pagamentos");

            Gson gson = new Gson();

            MovimentacaoJson[] movimentacoes = gson.fromJson(json.toString().replace(" / ", "/"), MovimentacaoJson[].class);

            List<Pagamento> pagamentos = new ArrayList<>();

            for (MovimentacaoJson movimentacaoJson : movimentacoes) {
                Pagamento pagamento = parsePagamento(movimentacaoJson);
                pagamentos.add(pagamento);
            }

            return pagamentos;

        } catch (IOException e) {
            System.err.println("Erro ao ler URL. URL inválida.  "+e.getMessage());
            throw new MovimentacaoRepositoryException(e);
        } catch (Exception e) {
            System.err.println("Erro ao ler json. Json Inválido.");
            throw new MovimentacaoRepositoryException(e);
        }

    }

    @Override
    public List<Recebimento> listRecebimentos() throws MovimentacaoRepositoryException {

        try {
            String json = readJsonFromUrl(URL_WS + "/recebimentos");

            Gson gson = new Gson();

            MovimentacaoJson[] movimentacoes = gson.fromJson(json.toString().replace(" / ", "/"), MovimentacaoJson[].class);

            List<Recebimento> recebimentos = new ArrayList<>();

            for (MovimentacaoJson movimentacaoJson : movimentacoes) {
                Recebimento recebimento = parseRecebimento(movimentacaoJson);
                recebimentos.add(recebimento);
            }

            return recebimentos;
        } catch (IOException e) {
            System.err.println("Erro ao ler URL. URL inválida.  "+e.getMessage());
            throw new MovimentacaoRepositoryException(e);
        } catch (Exception e) {
            System.err.println("Erro ao ler json. Json Inválido.");
            throw new MovimentacaoRepositoryException(e);
        }
    }


    private String readJsonFromUrl(String url) throws IOException {

        try (InputStream is = new URL(url).openStream()) {

            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            StringBuilder jsonBuilder = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                jsonBuilder.append((char) cp);
            }

            String json = jsonBuilder.toString().replace(" / ", "/");

            return json;

        }
    }


    private Pagamento parsePagamento(MovimentacaoJson json) throws ParseException {

        Pagamento pagamento = new Pagamento();
        parseMovimentacao(json, pagamento);

        return pagamento;
    }

    private void parseMovimentacao(MovimentacaoJson json, Movimentacao movimentacao) throws ParseException {
        String valor = json.getValor();
        valor = StringUtil.convertFromCurrency(valor);
        movimentacao.setData(format.parse(json.getData()));
        movimentacao.setValor(new BigDecimal(valor));
        movimentacao.setMoeda(json.getMoeda());
        movimentacao.setDescricao(json.getDescricao());
        movimentacao.setCategoria(StringUtil.convertToAscII(json.getCategoria()));
    }

    private Recebimento parseRecebimento(MovimentacaoJson json) throws ParseException {

        Recebimento recebimento = new Recebimento();
        parseMovimentacao(json, recebimento);

        return recebimento;
    }
}
