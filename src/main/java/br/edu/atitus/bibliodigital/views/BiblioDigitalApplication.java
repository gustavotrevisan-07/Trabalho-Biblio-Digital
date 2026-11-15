package br.edu.atitus.bibliodigital.views;

import br.edu.atitus.bibliodigital.entities.AudioLivro;
import br.edu.atitus.bibliodigital.entities.Livro;
import br.edu.atitus.bibliodigital.entities.LivroDigital;
import br.edu.atitus.bibliodigital.entities.LivroFisico;
import br.edu.atitus.bibliodigital.services.LivroService;

import java.util.List;

public class BiblioDigitalApplication {
    private final LivroService service = new LivroService();

    void main() {
        IO.println("===== Bem vindo ao BiblioDigital =====");
        int opcao = -1;
        do {
            mostrarMenu();
            try {
                opcao = Integer.parseInt(IO.readln("Escolha uma das opções acima: ").trim());
                switch (opcao) {
                    case 1 -> cadastrar();
                    case 2 -> pesquisarPorTitulo();
                    case 3 -> pesquisarPorAutor();
                    case 4 -> alterar();
                    case 5 -> excluir();
                    case 0 -> IO.println("Até logo!!!");
                    default -> IO.println("Erro: opção inexistente");
                }
            } catch (NumberFormatException ex) {
                IO.println("Erro: digite um número válido");
            } catch (RuntimeException ex) {
                IO.println("Erro: " + ex.getMessage());
            }
        } while (opcao != 0);
    }

    void mostrarMenu() {
        IO.println("""

                Menu:

                1 - Cadastrar novo Livro

                2 - Pesquisar Livro por título

                3 - Pesquisar Livro por autor

                4 - Alterar dados do Livro

                5 - Excluir Livro

                0 - Sair
                """);
    }

    void cadastrar() {
        String tipoLivro = """
                Qual tipo de livro deseja cadastrar?
                1 - Livro físico
                2 - Livro digital
                3 - Audiolivro

                Escolha uma opção:
                """;
        int opcao = Integer.parseInt(IO.readln(tipoLivro).trim());
        if (opcao < 1 || opcao > 3) {
            IO.println("Opção de tipo de livro inválida");
            return;
        }
        Livro novoLivro = lerLivro(opcao);
        service.inserir(novoLivro);
        IO.println("Livro cadastrado com sucesso!");
    }

    Livro lerLivro(int opcao) {
        String titulo = IO.readln("Digite o título do livro: ");
        String autor = IO.readln("Digite o autor do livro: ");
        int anoPublicacao = Integer.parseInt(IO.readln("Digite o ano de publicação do livro: ").trim());
        if (opcao == 1) {
            int quantidadeExemplares = Integer.parseInt(IO.readln("Digite o número de exemplares: ").trim());
            String localizacao = IO.readln("Digite a localização: ");
            return new LivroFisico(titulo, autor, anoPublicacao, quantidadeExemplares, localizacao);
        } else if (opcao == 2) {
            String formato = IO.readln("Digite o formato do arquivo: ");
            double tamanhoArquivo = Double.parseDouble(IO.readln("Digite o tamanho do arquivo: ").trim());
            return new LivroDigital(titulo, autor, anoPublicacao, formato, tamanhoArquivo);
        } else {
            int duracao = Integer.parseInt(IO.readln("Digite a duração do audiolivro: ").trim());
            String narrador = IO.readln("Digite o nome do narrador: ");
            return new AudioLivro(titulo, autor, anoPublicacao, duracao, narrador);
        }
    }

    void pesquisarPorTitulo() {
        String pesquisa = IO.readln("Pesquisa por título: ");
        imprimirLivros(service.findByTitulo(pesquisa));
    }

    void pesquisarPorAutor() {
        String pesquisa = IO.readln("Pesquisa por autor: ");
        imprimirLivros(service.findByAutor(pesquisa));
    }

    void imprimirLivros(List<Livro> livros) {
        if (livros.isEmpty()) {
            IO.println("Nenhum livro encontrado para a pesquisa realizada");
        } else {
            for (Livro livro : livros) {
                IO.println(livro);
                IO.println("Tipo: " + livro.getTipo());
            }
        }
    }

    boolean listarLivros() {
        List<Livro> livros = service.findAll();
        if (livros.isEmpty()) {
            IO.println("Não existe nenhum livro cadastrado");
            return false;
        }
        IO.println("===== Livros cadastrados =====");
        IO.println("");
        for (int i = 0; i < livros.size(); i++) {
            IO.println((i + 1) + " - " + livros.get(i).getTitulo());
        }
        IO.println("");
        return true;
    }

    void alterar() {
        if (!listarLivros())
            return;
        int indice = Integer.parseInt(IO.readln("Informe o índice do livro que deseja alterar: ").trim()) - 1;
        Livro atual = service.findByIndex(indice);
        IO.println("Dados atuais:");
        IO.println(atual);
        IO.println("Tipo: " + atual.getTipo());
        IO.println("Informe os novos dados:");

        int tipo = 3;
        if (atual instanceof LivroFisico)
            tipo = 1;
        else if (atual instanceof LivroDigital)
            tipo = 2;

        Livro alterado = lerLivro(tipo);
        service.alterar(indice, alterado);
        IO.println("Livro alterado com sucesso!");
    }

    void excluir() {
        if (!listarLivros())
            return;
        int indice = Integer.parseInt(IO.readln("Informe o índice do livro que deseja excluir: ").trim()) - 1;
        Livro livro = service.findByIndex(indice);
        String confirmacao = IO.readln("Deseja excluir o livro " + livro.getTitulo() + "? (S/N): ");
        if (confirmacao.trim().equalsIgnoreCase("S")) {
            service.excluir(indice);
            IO.println("Livro excluído com sucesso!");
        } else {
            IO.println("Exclusão cancelada");
        }
    }
}