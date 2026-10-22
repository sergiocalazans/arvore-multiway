public class ArvoreBMais {

	// Ordem 4: cada folha armazena até 3 chaves antes de dividir.
	private final int ORDEM = 4;

	// Cada nó é uma folha com dados ou um índice com referências aos filhos.
	class No {
		boolean folha;

		// Nas folhas, n conta chaves; nos índices, n conta filhos.
		int n;

		int[] chaves = new int[ORDEM];
		String[] valores = new String[ORDEM];
		No[] filhos = new No[ORDEM + 1];
		No pai;

		// Liga as folhas para permitir consultas por intervalo.
		No proxima;

		No(boolean folha) {
			this.folha = folha;
		}
	}

	private No raiz = new No(true);

	// Segue os índices até encontrar a folha onde a chave deve estar.
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

	// Procura a chave na folha e devolve a descrição (ou null).
	public String buscar(int chave) {

		No folha = localizarFolha(chave);

		for (int i = 0; i < folha.n; i++) {
			if (folha.chaves[i] == chave) {
				return folha.valores[i];
			}
		}

		return null;
	}

	// Recalcula as chaves separadoras de um nó interno.
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

	// Propaga a atualização dos separadores até a raiz.
	private void atualizarAteRaiz(No no) {

		while (no != null) {
			atualizar(no);
			no = no.pai;
		}
	}

	// Insere em ordem crescente; uma chave repetida atualiza o valor.
	public void inserir(int chave, String valor) {

		if (valor == null) {
			System.out.println("Informe uma descrição diferente de null.");
			return;
		}

		No no = localizarFolha(chave);
		int i = 0;

		while (i < no.n && no.chaves[i] < chave) {
			i++;
		}

		if (i < no.n && no.chaves[i] == chave) {
			no.valores[i] = valor;
			return;

		}

		// Abre espaço deslocando as chaves maiores para a direita.
		for (int j = no.n; j > i; j--) {
			no.chaves[j] = no.chaves[j - 1];
			no.valores[j] = no.valores[j - 1];
		}

		no.chaves[i] = chave;
		no.valores[i] = valor;
		no.n++;

		// Se ultrapassar a capacidade, divide o nó e ajusta o pai.
		while ((no.folha && no.n == ORDEM) || (!no.folha && no.n == ORDEM + 1)) {
			No direita = new No(no.folha);

			// Metade das entradas fica no novo nó direito.
			int meio = no.n / 2;

			for (int j = meio; j < no.n; j++) {
				if (no.folha) {
					direita.chaves[direita.n] = no.chaves[j];
					direita.valores[direita.n] = no.valores[j];
					no.valores[j] = null;
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

			// Uma divisão da raiz cria um novo nível na árvore.
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

	// Copia uma entrada (chave/valor ou referência de filho).
	private void copiar(No origem, int de, No destino, int para) {

		if (origem.folha) {
			destino.chaves[para] = origem.chaves[de];
			destino.valores[para] = origem.valores[de];

		} else {
			destino.filhos[para] = origem.filhos[de];
			destino.filhos[para].pai = destino;

		}

	}

	// Remove uma entrada deslocando as seguintes para a esquerda.
	private void retirar(No no, int pos) {

		for (int i = pos; i < no.n - 1; i++) {
			copiar(no, i + 1, no, i);
		}

		no.n--;
		no.valores[no.n] = null;
		no.filhos[no.n] = null;

	}

	// Remove a chave e corrige nós com menos de duas entradas.
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

			// Primeiro tenta emprestar uma entrada do irmão esquerdo.
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
				// Se necessário, tenta emprestar do irmão direito.
			} else if (direita != null && direita.n > 2) {
				copiar(direita, 0, no, no.n);
				no.n++;
				retirar(direita, 0);
				atualizar(direita);
				atualizarAteRaiz(no);
				return true;
			} else {
				// Sem empréstimo possível: une dois nós irmãos.
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

		// Se restar apenas um filho na raiz, reduz a altura.
		if (!raiz.folha && raiz.n == 1) {
			raiz = raiz.filhos[0];
			raiz.pai = null;
		}

		atualizarAteRaiz(no);
		return true;

	}

	// Percorre as folhas encadeadas e imprime chaves no intervalo.
	public void listarIntervalo(int inicio, int fim) {

		No folha = localizarFolha(inicio);

		while (folha != null) {

			for (int i = 0; i < folha.n; i++) {
				if (folha.chaves[i] > fim) {
					return;
				}

				if (folha.chaves[i] >= inicio) {
					System.out.println(folha.chaves[i] + " - " + folha.valores[i]);
				}
			}

			folha = folha.proxima;
		}

	}

	// Exibe os índices e as folhas com indentação por nível.
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
				System.out.print("Indice: ");
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

	public static void main(String[] args) {
		ArvoreBMais arvore = new ArvoreBMais();

		// Sequência embaralhada, fixa para facilitar o teste de mesa.
		int[] insercoes = { 40, 10, 70, 20, 60, 30, 80, 50, 15, 65, 25, 75 };
		System.out.println("INSERÇÕES");
		for (int chave : insercoes) {
			System.out.println("\nInserindo: " + chave);
			arvore.inserir(chave, "Valor " + chave);
			arvore.mostrar();
		}

		// Remoções em ordem diferente
		int[] remocoes = { 30, 70, 10, 65, 40, 15 };
		System.out.println("\nREMOÇÕES");
		for (int chave : remocoes) {
			System.out.println("\nRemovendo: " + chave);
			System.out.println("Removida? " + arvore.remover(chave));
			arvore.mostrar();
		}

		// Demonstra uma busca e uma consulta ordenada por intervalo.
		System.out.println("\nBusca pela chave 50: " + arvore.buscar(50));
		System.out.println("\nChaves entre 20 e 80:");
		arvore.listarIntervalo(20, 80);
	}
}
