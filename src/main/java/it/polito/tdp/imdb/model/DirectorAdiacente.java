package it.polito.tdp.imdb.model;

public class DirectorAdiacente  implements Comparable <DirectorAdiacente> {
Director director;
double peso;

public DirectorAdiacente(Director director, double peso){
	super();
	this.director = director;
	this.peso = peso;
}
public Director getDirector() {
	return director;
}
public void setDirector(Director director) {
	this.director = director;
}
public double getPeso() {
	return peso;
}

public void setPeso(double peso) {
	this.peso = peso;
}
@Override
public int compareTo(DirectorAdiacente o) {
	
	return (int) -(this.getPeso()-o.getPeso());
}
@Override
public String toString() {
	return director + " con peso=" + peso ;
}


}
