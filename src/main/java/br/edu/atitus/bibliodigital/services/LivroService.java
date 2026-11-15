package br.edu.atitus.bibliodigital.services;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.repositories.LivroRepository;

import java.util.List;

public class LivroService {
    private final LivroRepository repository = new LivroRepository();

    public void inserir(Livro livro) {
        validar(livro, -1);
        repository.inserir(livro);
    }

    public List<Livro> findAll() {
        return repository.findAll();
    }

    public Livro findByIndex(int indice) {
        validarIndice(indice);
        return repository.findByIndex(indice);
    }

    public List<Livro> findByTitulo(String parametro) {
        if (parametro == null || parametro.isBlank())
            throw new RuntimeException("Parâmetro inválido");
        return repository.findByTitulo(parametro.trim());
    }

    public List<Livro> findByAutor(String parametro) {
        if (parametro == null || parametro.isBlank())
            throw new RuntimeException("Parâmetro inválido");
        return repository.findByAutor(parametro.trim());
    }

    public void alterar(int indice, Livro livro) {
        validarIndice(indice);
        validar(livro, indice);
        repository.alterar(indice, livro);
    }

    public void excluir(int indice) {
        validarIndice(indice);
        repository.removeByIndex(indice);
    }

    private void validar(Livro livro, int indiceIgnorado) {
        if (livro == null)
            throw new RuntimeException("Objeto Nulo");
        if (livro.getTitulo() == null || livro.getTitulo().isBlank())
            throw new RuntimeException("Título Inválido");
        if (livro.getAutor() == null || livro.getAutor().isBlank())
            throw new RuntimeException("Autor Inválido");
        if (livro.getAnoPublicacao() < 1900)
            throw new RuntimeException("Ano de publicação deve ser igual ou superior a 1900");

        livro.setTitulo(livro.getTitulo().trim());
        livro.setAutor(livro.getAutor().trim());
        if (livro instanceof LivroFisico) {
            LivroFisico fisico = (LivroFisico) livro;
            if (fisico.getLocalizacao() != null)
                fisico.setLocalizacao(fisico.getLocalizacao().trim());
        } else if (livro instanceof LivroDigital) {
            LivroDigital digital = (LivroDigital) livro;
            if (digital.getFormato() != null)
                digital.setFormato(digital.getFormato().trim());
        } else if (livro instanceof AudioLivro) {
            AudioLivro audio = (AudioLivro) livro;
            if (audio.getNarrador() != null)
                audio.setNarrador(audio.getNarrador().trim());
        }

        if (repository.existsByTitulo(livro.getTitulo(), indiceIgnorado))
            throw new RuntimeException("Já existe um livro com esse título");
    }

    private void validarIndice(int indice) {
        if (repository.findAll().isEmpty())
            throw new RuntimeException("Não existe nenhum livro cadastrado");
        if (indice < 0 || indice >= repository.findAll().size())
            throw new RuntimeException("Índice inválido");
    }

}