package kz.cinematix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cm_hall")
public class Hall {
	
	public Long id;
	public String name;
	public Cinema cinema;
	public Long rows;
	public Long columns;
	public String type;
	
	@Id
	@GeneratedValue
	@Column(name="ID_")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="NAME_")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn(name="CINEMA_")
	public Cinema getCinema() {
		return cinema;
	}
	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}
	
	@Column(name="ROWS_")
	public Long getRows() {
		return rows;
	}
	public void setRows(Long rows) {
		this.rows = rows;
	}
	
	@Column(name="COLUMNS_")
	public Long getColumns() {
		return columns;
	}
	public void setColumns(Long columns) {
		this.columns = columns;
	}
	
	@Column(name="TYPE_")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
