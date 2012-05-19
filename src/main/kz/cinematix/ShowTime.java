package kz.cinematix;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="cm_showtime")
public class ShowTime {
	
	public Long id;
	public Movie movie;
	public Date begin;
	public Long duration;
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
	
	@Column(name="BEGIN_")
	public Date getBegin() {
		return begin;
	}
	public void setBegin(Date begin) {
		this.begin = begin;
	}
	
	@Column(name="DURATION_")
	public Long getDuration() {
		return duration;
	}
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	
	@ManyToOne
	@JoinColumn(name = "HALL_")
	public Hall getHall() {
		return hall;
	}
	public void setHall(Hall hall) {
		this.hall = hall;
	}
	
	@ManyToOne
	@JoinColumn(name="MOVIE_")
	public Movie getMovie(){
		return movie;
	}
	public void setMovie(Movie movie){
		this.movie = movie;
	}
}
