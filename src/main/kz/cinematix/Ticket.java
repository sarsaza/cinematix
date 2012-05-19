package kz.cinematix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cm_ticket")
public class Ticket {
	
	public Long id;
	public ShowTime showTime;
	public Double price;
	public String pamentType;
	
	@Id
	@GeneratedValue
	@Column(name="ID_")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne
	@JoinColumn(name = "SHOWTIME_")
	public ShowTime getShowTime() {
		return showTime;
	}
	public void setShowTime(ShowTime showTime) {
		this.showTime = showTime;
	}
	
	@Column(name="PRICE_")
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	
	@Column(name="TYPE_")
	public String getPamentType() {
		return pamentType;
	}
	public void setPamentType(String pamentType) {
		this.pamentType = pamentType;
	}
}
