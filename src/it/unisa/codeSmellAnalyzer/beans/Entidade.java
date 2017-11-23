package it.unisa.codeSmellAnalyzer.beans;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class Entidade {
	
	private String nome;
	
	private Set<String> dependencias;
	
	private String classe;
	
	private Map<Entidade,Double> distancia;

	public Entidade(String nome,String classe) {
		super();
		this.nome = nome;
		this.classe = classe;
		dependencias = new LinkedHashSet<>();
		distancia = new HashMap<>();
	}
	
	public void addDistancia(Entidade entidade,Double valor) {
		distancia.put(entidade,valor);
	}
	
	public Map<Entidade, Double> getDistancia() {
		return distancia;
	}

	public String getClasse() {
		return classe;
	}

	public void addDependencia(String dependencia) {
		dependencias.add(dependencia);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<String> getDependencias() {
		return dependencias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entidade other = (Entidade) obj;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}
