package com.alison.lojadelivros.model.dto;

import java.io.Serializable;
import java.util.Date;

public class LivroDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long codigolivro;
	private String titulo;
	private Double valorunitario;
	private Integer qtde;
	private Double valortotal;
	private Date datadacompra;
	private Long codigocliente;
	private String nomecliente;

	public Long getCodigolivro() {
		return codigolivro;
	}

	public void setCodigolivro(Long codigolivro) {
		this.codigolivro = codigolivro;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Double getValorunitario() {
		return valorunitario;
	}

	public void setValorunitario(Double valorunitario) {
		this.valorunitario = valorunitario;
	}

	public Integer getQtde() {
		return qtde;
	}

	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}

	public Double getValortotal() {
		return valortotal;
	}

	public void setValortotal(Double valortotal) {
		this.valortotal = valortotal;
	}

	public Date getDatadacompra() {
		return datadacompra;
	}

	public void setDatadacompra(Date datadacompra) {
		this.datadacompra = datadacompra;
	}

	public Long getCodigocliente() {
		return codigocliente;
	}

	public void setCodigocliente(Long codigocliente) {
		this.codigocliente = codigocliente;
	}

	public String getNomecliente() {
		return nomecliente;
	}

	public void setNomecliente(String nomecliente) {
		this.nomecliente = nomecliente;
	}

}
