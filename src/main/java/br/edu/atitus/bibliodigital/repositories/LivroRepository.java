package br.edu.atitus.bibliodigital.repositories;

import br.edu.atitus.bibliodigital.entities.Livro;

import java.util.ArrayList;
import java.util.List;

public class LivroRepository {
    private List<Livro> acervo = new ArrayList<>();

    public List<Livro> findAll() {
        return new ArrayList<>(acervo);
    }

    public Livro findByIndex(int indice) {
        return acervo.get(indice);
    }

    public void inserir(Livro livro) {
        acervo.add(livro);
    }

    public List<Livro> findByTitulo(String parametro) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : acervo) {
            if (livro.getTitulo().toUpperCase().contains(parametro.toUpperCase())) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public List<Livro> findByAutor(String parametro) {
        List<Livro> resultado = new ArrayList<>();
        for (Livro livro : acervo) {
            if (livro.getAutor().toUpperCase().contains(parametro.toUpperCase())) {
                resultado.add(livro);
            }
        }
        return resultado;
    }

    public boolean existsByTitulo(String titulo, int indiceIgnorado) {
        for (int i = 0; i < acervo.size(); i++) {
            if (i != indiceIgnorado && acervo.get(i).getTitulo().equalsIgnoreCase(titulo)) {
                return true;
            }
        }
        return false;
    }

    public void alterar(int indice, Livro livro) {
        acervo.set(indice, livro);
    }

    public void removeByIndex(int indice) {
        acervo.remove(indice);
    }

}