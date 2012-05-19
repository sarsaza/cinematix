package kz.cinematix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cm_seat")
public class Seat {

	public Long id;
	public String position;
	public Long x;
	public Long y;
	public String type;
    public Hall hall;
    
    @Id
    @GeneratedValue
    @Column(name="ID_")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="POSITION_")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(name="X_")
	public Long getX() {
		return x;
	}
	public void setX(Long x) {
		this.x = x;
	}
	
	@Column(name="Y_")
	public Long getY() {
		return y;
	}
	public void setY(Long y) {
		this.y = y;
	}
	public String getType() {
		return type;
	}
	

	@Column(name="TYPE_")
	public void setType(String type) {
		this.type = type;
	}
	
	@ManyToOne
	@JoinColumn(name="HALL_")
	public Hall getHall() {
		return hall;
	}
	public void setHall(Hall hall) {
		this.hall = hall;
	}
}
