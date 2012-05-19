package kz.bee.cinematix.cruds;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import kz.cinematix.Cinema;
import kz.cinematix.Hall;

import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("cinemaManager")
@Scope(ScopeType.PAGE)
public class CinemaManager {
	
	@In("entityManager")
	EntityManager em;
	
	private Cinema cinema;
	private Hall hall;
	private List<Cinema> cinemas = new ArrayList<Cinema>();
	
	public List<Hall> getHalls(Cinema cinema){
		return (List<Hall>) em.createQuery("from Hall h where h.cinema=:cinema").setParameter("cinema", cinema).getResultList();
	}
	
	public void createCinema(){
		cinema = new Cinema();
	}
	
	public void saveCinema(){
		if(cinema.getId()==null)
			em.persist(cinema);
		else
			em.merge(cinema);
		cinema = null;
	}
	
	public void removeCinema(Long cinemaId){
		System.out.println("removeCinema "+ cinemaId);
		try {
			em.remove(em.find(Cinema.class, cinemaId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createHall(Cinema cinema){
		hall = new Hall();
		hall.setCinema(cinema);
	}
	
	public void saveHall(){
		if(hall.getId()==null)
			em.persist(hall);
		else
			em.merge(hall);
		hall = null;
	}
	
	public void cancel(){
		hall = null;
		cinema = null;
	}
	
	public void remove(){
		if(cinema!=null){
			removeCinema(cinema.getId());
			cinema = null;
		}
		if(hall!=null){
			removeHall(hall.getId());
			hall = null;
		}
	}
	
	public void removeHall(Long hallId){
		try {
			em.remove(em.find(Hall.class, hallId));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Cinema> getCinemas() {
		if(cinemas==null || cinemas.isEmpty())
			cinemas = em.createQuery("from Cinema").getResultList();
		return cinemas;
	}

	public void setCinemas(List<Cinema> cinemas) {
		this.cinemas = cinemas;
	}

	public Hall getHall() {
		return hall;
	}

	public void setHall(Hall hall) {
		this.hall = hall;
	}

	public Cinema getCinema() {
		return cinema;
	}

	public void setCinema(Cinema cinema) {
		this.cinema = cinema;
	}

}
