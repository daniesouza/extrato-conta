package com.itau.extrato.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public abstract class Movimentacao implements Serializable {

    private Long id;
    private Date data;
    private String descricao;
    private String moeda;
    private BigDecimal valor;
    private String categoria;




    public static Comparator<Movimentacao> ORDER_BY_DATA_ASC = Comparator.comparing(Movimentacao::getData);


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        this.moeda = moeda;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }


    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + getId() +
                ", data=" + getData() +
                ", descricao='" + getDescricao() + '\'' +
                ", moeda='" + getMoeda() + '\'' +
                ", valor=" + getValor() +
                ", categoria=" + getCategoria() +
                '}';
    }
}
