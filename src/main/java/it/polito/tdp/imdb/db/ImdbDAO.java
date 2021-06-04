package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public void listAllDirectors(Map <Integer, Director> idMap){
		String sql = "SELECT * FROM directors";
		//List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("id"))) {
				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				idMap.put(director.getId(), director);
				//result.add(director);
			}
			}
			conn.close();
			//return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			//return null;
		}
	}
	
	public List <Director> getVertici(int anno, Map <Integer, Director> idMap){
		String sql="SELECT d.id, d.first_name, d.last_name "
				+ "FROM directors d, movies m, movies_directors md "
				+ "WHERE d.id=md.director_id AND md.movie_id=m.id AND m.year = ? "
				+"GROUP BY d.id ";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

				try {
					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, anno);
					ResultSet res = st.executeQuery();
					while (res.next()) {

						result.add(idMap.get(res.getInt("id")));
					}
					
					res.close();
					st.close();
					conn.close();
					return result;
					
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
	
	public List <Adiacenza> getAdiacenze(int anno, Map <Integer, Director > idMap){
		
		/*
#Un arco collega due registi d se hanno diretto almeno una volta lo stesso attore nell’anno considerato.
#Il peso dell’arco è dato dal numero degli attori condivisi.
#Si specifica che un attore può essere condiviso da due registi sia perché i registi hanno diretto
#insieme lo stesso film, sia perché hanno diretto film diversi in cui recitava l’attore in questione.
 
SELECT d1.id, d2.id 
FROM directors d1, directors d2, movies m1, movies m2, movies_directors md1, movies_directors md2 , roles r1, roles r2
WHERE d1.id = md1.director_id AND md1.movie_id = m1.id AND m1.year = 2005 AND r1.movie_id=m1.id AND d1.id> d2.id AND
d2.id = md2.director_id AND md2.movie_id = m2.id AND m2.year = m1.year AND r2.movie_id = m2.id AND r1.actor_id= r2.actor_id AND
md1.director_id!= md2.director_id AND
(md1.movie_id=md2.movie_id OR #stesso film
md1.movie_id!= md2.movie_id ) #film diversi in cui c'era stesso attore
GROUP BY d1.id, d2.id

		 */
		
		String sql = "SELECT d1.id, d2.id , COUNT(r1.actor_id) AS peso "
				+ "FROM directors d1, directors d2, movies m1, movies m2, movies_directors md1, movies_directors md2 , roles r1, roles r2 "
				+ "WHERE d1.id = md1.director_id AND md1.movie_id = m1.id AND m1.year = ? AND r1.movie_id=m1.id AND d1.id> d2.id AND "
				+ "d2.id = md2.director_id AND md2.movie_id = m2.id AND m2.year = m1.year AND r2.movie_id = m2.id AND r1.actor_id= r2.actor_id AND "
				+ "md1.director_id!= md2.director_id AND "
				+ "(md1.movie_id=md2.movie_id OR "
				+ "md1.movie_id!= md2.movie_id ) "
				+ "GROUP BY d1.id, d2.id";
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

				try {
					PreparedStatement st = conn.prepareStatement(sql);
					st.setInt(1, anno);
					ResultSet res = st.executeQuery();
					while (res.next()) {
						if(idMap.containsKey(res.getInt("d1.id") ) && idMap.containsKey(res.getInt("d2.id"))) {
						Director d1 = idMap.get(res.getInt("d1.id"));
						Director d2 = idMap.get(res.getInt("d2.id"));
						int peso = res.getInt("peso");
						result.add(new Adiacenza(d1,d2, peso) );
					}
					}
					
					res.close();
					st.close();
					conn.close();
					return result;
					
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
	
	
}
