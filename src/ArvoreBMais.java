public class ArvoreBMais {

    private final int ORDEM = 4;

    class No {
        boolean folha;

        int n;
        int[] chaves = new int[ORDEM];
        int[] valores = new int[ORDEM]; 
        No[] filhos = new No[ORDEM + 1];
        No pai;
        No proxima;

        No(boolean folha) {
            this.folha = folha;
        }
    }

    private No raiz = new No(true);

    private No localizarFolha(int chave) {
        No no = raiz;
        while (!no.folha) {
            int i = 0;
            while (i < no.n - 1 && chave >= no.chaves[i]) {
                i++;
            }
            no = no.filhos[i];
        }
        return no;
    }

    public int buscar(int chave) {
        No folha = localizarFolha(chave);
        for (int i = 0; i < folha.n; i++) {
            if (folha.chaves[i] == chave) {
                return folha.valores[i];
            }
        }
        return -1;
    }

    private void atualizar(No no) {
        if (!no.folha) {
            for (int i = 1; i < no.n; i++) {
                No menor = no.filhos[i];
                while (!menor.folha) {
                    menor = menor.filhos[0];
                }
                no.chaves[i - 1] = menor.chaves[0];
            }
        }
    }

    private void atualizarAteRaiz(No no) {
        while (no != null) {
            atualizar(no);
            no = no.pai;
        }
    }

    public void inserir(int chave, int posicaoRegistro) {
        No no = localizarFolha(chave);
        int i = 0;
        while (i < no.n && no.chaves[i] < chave) {
            i++;
        }
        if (i < no.n && no.chaves[i] == chave) {
            no.valores[i] = posicaoRegistro;
            return;
        }
        for (int j = no.n; j > i; j--) {
            no.chaves[j] = no.chaves[j - 1];
            no.valores[j] = no.valores[j - 1];
        }
        no.chaves[i] = chave;
        no.valores[i] = posicaoRegistro;
        no.n++;

        while ((no.folha && no.n == ORDEM) || (!no.folha && no.n == ORDEM + 1)) {
            No direita = new No(no.folha);
            int meio = no.n / 2;
            for (int j = meio; j < no.n; j++) {
                if (no.folha) {
                    direita.chaves[direita.n] = no.chaves[j];
                    direita.valores[direita.n] = no.valores[j];
                } else {
                    direita.filhos[direita.n] = no.filhos[j];
                    direita.filhos[direita.n].pai = direita;
                    no.filhos[j] = null;
                }
                direita.n++;
            }
            no.n = meio;
            if (no.folha) {
                direita.proxima = no.proxima;
                no.proxima = direita;
            }
            atualizar(no);
            atualizar(direita);
            No pai = no.pai;
            if (pai == null) {
                pai = new No(false);
                pai.filhos[0] = no;
                pai.n = 1;
                no.pai = pai;
                raiz = pai;
            }
            int pos = 0;
            while (pai.filhos[pos] != no) {
                pos++;
            }
            for (int j = pai.n; j > pos + 1; j--) {
                pai.filhos[j] = pai.filhos[j - 1];
            }
            pai.filhos[pos + 1] = direita;
            direita.pai = pai;
            pai.n++;
            atualizar(pai);
            no = pai;
        }
        atualizarAteRaiz(no);
    }

    private void copiar(No origem, int de, No destino, int para) {
        if (origem.folha) {
            destino.chaves[para] = origem.chaves[de];
            destino.valores[para] = origem.valores[de];
        } else {
            destino.filhos[para] = origem.filhos[de];
            destino.filhos[para].pai = destino;
        }
    }

    private void retirar(No no, int pos) {
        for (int i = pos; i < no.n - 1; i++) {
            copiar(no, i + 1, no, i);
        }
        no.n--;
        if (!no.folha) {
            no.filhos[no.n] = null;
        }
    }

    public boolean remover(int chave) {
        No no = localizarFolha(chave);
        int pos = 0;
        while (pos < no.n && no.chaves[pos] != chave) {
            pos++;
        }
        if (pos == no.n) {
            return false;
        }
        retirar(no, pos);
        while (no != raiz && no.n < 2) {
            No pai = no.pai;
            int i = 0;
            while (pai.filhos[i] != no) {
                i++;
            }
            No esquerda = null;
            No direita = null;
            if (i > 0) {
                esquerda = pai.filhos[i - 1];
            }
            if (i + 1 < pai.n) {
                direita = pai.filhos[i + 1];
            }
            if (esquerda != null && esquerda.n > 2) {
                for (int j = no.n; j > 0; j--) {
                    copiar(no, j - 1, no, j);
                }
                copiar(esquerda, esquerda.n - 1, no, 0);
                no.n++;
                retirar(esquerda, esquerda.n - 1);
                atualizar(esquerda);
                atualizarAteRaiz(no);
                return true;
            } else if (direita != null && direita.n > 2) {
                copiar(direita, 0, no, no.n);
                no.n++;
                retirar(direita, 0);
                atualizar(direita);
                atualizarAteRaiz(no);
                return true;
            } else {
                No destino = no;
                No origem = direita;
                int indiceRemovido = i + 1;
                if (esquerda != null) {
                    destino = esquerda;
                    origem = no;
                    indiceRemovido = i;
                }
                for (int j = 0; j < origem.n; j++) {
                    copiar(origem, j, destino, destino.n);
                    destino.n++;
                }
                if (destino.folha) {
                    destino.proxima = origem.proxima;
                }
                atualizar(destino);
                retirar(pai, indiceRemovido);
                atualizar(pai);
                no = pai;
            }
        }
        if (!raiz.folha && raiz.n == 1) {
            raiz = raiz.filhos[0];
            raiz.pai = null;
        }
        atualizarAteRaiz(no);
        return true;
    }

    public void mostrar() {
        No no = raiz;
        int nivel = 0;
        while (no != null) {
            for (int i = 0; i < nivel; i++) {
                System.out.print("    ");
            }
            int quantidade = no.n;
            if (no.folha) {
                System.out.print("Folha: ");
            } else {
                System.out.print("Índice: ");
                quantidade--;
            }
            for (int i = 0; i < quantidade; i++) {
                System.out.print(no.chaves[i] + " ");
            }
            System.out.println();
            if (!no.folha) {
                no = no.filhos[0];
                nivel++;
            } else {
                boolean encontrou = false;
                while (no.pai != null && !encontrou) {
                    No pai = no.pai;
                    int i = 0;
                    while (pai.filhos[i] != no) {
                        i++;
                    }
                    if (i + 1 < pai.n) {
                        no = pai.filhos[i + 1];
                        encontrou = true;
                    } else {
                        no = pai;
                        nivel--;
                    }
                }
                if (!encontrou) {
                    no = null;
                }
            }
        }
    }

    class Aluno {
        int matricula;
        String nome;
        String curso;
        int periodo;
        boolean ativo;

        Aluno(int matricula, String nome, String curso, int periodo) {
            this.matricula = matricula;
            this.nome = nome;
            this.curso = curso;
            this.periodo = periodo;
            this.ativo = true;
        }
    }

    class BancoDados {
        Aluno[] tabela = new Aluno[100];
        int quantidade = 0;
        ArvoreBMais indice = new ArvoreBMais();

        public void inserir(int matricula, String nome, String curso, int periodo) {
            if (indice.buscar(matricula) != -1) {
                System.out.println("Matrícula já cadastrada.");
                return;
            }
            if (quantidade == tabela.length) {
                System.out.println("Tabela cheia.");
                return;
            }
            tabela[quantidade] = new Aluno(matricula, nome, curso, periodo);
            indice.inserir(matricula, quantidade);
            quantidade++;
            System.out.println("Aluno cadastrado: " + matricula);
        }

        public void buscar(int matricula) {
            int posicao = indice.buscar(matricula);
            if (posicao == -1) {
                System.out.println("Aluno não encontrado.");
            } else {
                Aluno aluno = tabela[posicao];
                System.out.println(aluno.matricula + " | " + aluno.nome + " | " + aluno.curso + " | " + aluno.periodo);
            }
        }

        public void atualizar(int matricula, String nome, String curso, int periodo) {
            int posicao = indice.buscar(matricula);
            if (posicao == -1) {
                System.out.println("Aluno não encontrado.");
            } else {
                tabela[posicao].nome = nome;
                tabela[posicao].curso = curso;
                tabela[posicao].periodo = periodo;
                System.out.println("Aluno atualizado.");
            }
        }

        public void removerRegistro(int matricula) {
            int posicao = indice.buscar(matricula);
            if (posicao == -1) {
                System.out.println("Aluno não encontrado.");
            } else {
                if (indice.remover(matricula)) {
                    tabela[posicao].ativo = false;
                    System.out.println("Aluno removido: " + matricula);
                }
            }
        }

        public void listar() {
            for (int i = 0; i < quantidade; i++) {
                if (tabela[i].ativo) {
                    Aluno aluno = tabela[i];
                    System.out.println(aluno.matricula + " | " + aluno.nome + " | " + aluno.curso + " | " + aluno.periodo);
                }
            }
        }

        public void consultarIntervalo(int inicio, int fim) {
            if (inicio > fim) {
                System.out.println("Intervalo invalido.");
                return;
            }
            No folha = indice.localizarFolha(inicio);
            while (folha != null) {
                for (int i = 0; i < folha.n; i++) {
                    int chave = folha.chaves[i];
                    if (chave > fim) {
                        return;
                    }
                    if (chave >= inicio) {
                        Aluno aluno = tabela[folha.valores[i]];
                        if (aluno.ativo) {
                            System.out.println(aluno.matricula + " | " + aluno.nome + " | " + aluno.curso + " | " + aluno.periodo);
                        }
                    }
                }
                folha = folha.proxima;
            }
        }
    }

    public static void main(String[] args) {
        ArvoreBMais arvore = new ArvoreBMais();
        BancoDados banco = arvore.new BancoDados();
        
        // Inserções no banco de dados simples
        banco.inserir(40, "Ana", "Computacao", 4);
        banco.inserir(10, "Bruno", "Engenharia", 2);
        banco.inserir(70, "Carlos", "Sistemas", 3);
        banco.inserir(20, "Daniela", "Computacao", 1);
        banco.inserir(60, "Eduardo", "Engenharia", 5);
        banco.inserir(30, "Fernanda", "Computacao", 2);
        banco.inserir(80, "Gabriel", "Sistemas", 4);
        banco.inserir(50, "Helena", "Computacao", 3);
        banco.inserir(15, "Isabela", "Engenharia", 1);
        banco.inserir(65, "Joao", "Sistemas", 5);
        banco.inserir(25, "Larissa", "Computacao", 2);
        banco.inserir(75, "Marcos", "Engenharia", 4);
        
        // Índice B+
        banco.indice.mostrar();
        
        // Busca
        banco.buscar(50);
        
        // Consultar intervalo de 20 a 60
        banco.consultarIntervalo(20, 60);
        
        // Atualização dos dados
        banco.atualizar(50, "Helena Silva", "Computacao", 4);
        banco.buscar(50);
        
        // Remoções
        int[] remocoes = {30, 70, 10, 65, 40, 15};
        
        for (int i = 0; i < remocoes.length; i++) {
            banco.removerRegistro(remocoes[i]);
        }
        
        // Índice após remoção
        banco.indice.mostrar();
    }
}
