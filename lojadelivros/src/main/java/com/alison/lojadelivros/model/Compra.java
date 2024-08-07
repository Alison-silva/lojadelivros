package com.alison.lojadelivros.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Compra implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private Cliente cliente;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCompra = new Date();

	private Double valorTotal = 0.;

	@OneToMany(mappedBy = "compra", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<ItensCompra> itensCompras;

	public List<ItensCompra> getItensCompras() {
		return itensCompras;
	}

	public void setItensCompras(List<ItensCompra> itensCompras) {
		this.itensCompras = itensCompras;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Date getDataCompra() {
		return dataCompra;
	}

	public void setDataCompra(Date dataCompra) {
		this.dataCompra = dataCompra;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Compra other = (Compra) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Compra [id=" + id + ", cliente=" + cliente + ", dataCompra=" + dataCompra + ", valorTotal=" + valorTotal
				+ ", itensCompras=" + itensCompras + "]";
	}
	
	

}
