# Árvore B+ — Estruturas de Dados e Indexação

Implementação didática de uma **Árvore B+ de ordem 4 em Java**, desenvolvida para estudar estruturas de dados multiway e sua aplicação na **indexação de registros em bancos de dados**. O projeto combina as operações de manutenção da árvore com uma tabela de alunos armazenada em memória, permitindo observar como uma chave de pesquisa é associada à posição de um registro.

A proposta é manter o algoritmo acessível para estudo: a implementação utiliza **arrays**, laços (`for` e `while`), condicionais (`if` e `else`) e classes Java, sem coleções da API como `ArrayList` ou `HashMap`.

## Funcionalidades

### Árvore B+

- Inserção ordenada de chaves e divisão de nós quando necessário;
- Busca por chave por meio dos nós de índice;
- Atualização das chaves separadoras dos nós internos;
- Remoção com tentativa de empréstimo entre irmãos ou fusão de nós;
- Encadeamento das folhas para consultas por intervalo;
- Exibição textual dos índices e folhas por nível.

A árvore trabalha com **ordem 4**. Em condições normais, cada folha armazena até três chaves; os arrays possuem posições adicionais para auxiliar nas divisões. Os nós internos guardam separadores e referências para os filhos.

### Simulação de banco de dados

A classe interna `BancoDados` utiliza a Árvore B+ como índice para uma tabela de até **100 registros de alunos**, armazenada em um array fixo.

| Operação | Método | Descrição |
| --- | --- | --- |
| Inserir | `inserir(...)` | Cadastra um aluno e indexa sua matrícula. |
| Buscar | `buscar(matricula)` | Usa o índice para localizar e exibir um registro. |
| Atualizar | `atualizar(...)` | Altera nome, curso e período de um aluno existente. |
| Remover | `removerRegistro(matricula)` | Remove a matrícula do índice e marca o registro como inativo. |
| Listar | `listar()` | Percorre o array e imprime os registros ativos. |
| Consultar intervalo | `consultarIntervalo(inicio, fim)` | Percorre folhas encadeadas para recuperar matrículas no intervalo informado. |

Cada aluno possui os campos `matricula`, `nome`, `curso`, `periodo` e `ativo`. A **matrícula é a chave de busca** e o valor associado na folha é o **índice da posição do aluno no array** (uma referência lógica ao registro).

## Organização do repositório

```text
arvore-multiway/
├── src/
│   └── ArvoreBMais.java
├── .classpath
├── .project
├── .settings/
└── .gitignore
```

A classe `ArvoreBMais` reúne a estrutura dos nós, os algoritmos da árvore, a representação dos alunos, a simulação de banco de dados e o método `main` com os exemplos de execução.

## Como executar

**Pré-requisito:** JDK instalado, com os comandos `javac` e `java` disponíveis no terminal. O projeto também pode ser importado no **Eclipse**.

1. Clone o repositório:

   ```bash
   git clone https://github.com/sergiocalazans/arvore-multiway.git
   cd arvore-multiway
   ```

2. Compile o arquivo:

   ```bash
   javac -encoding UTF-8 src/ArvoreBMais.java
   ```

3. Execute a classe principal:

   ```bash
   java -cp src ArvoreBMais
   ```

No Eclipse, importe o diretório como projeto Java existente e execute `ArvoreBMais.java` como **Java Application**.

## Demonstração

O método `main` contém uma sequência fixa de testes:

1. Cadastra 12 alunos com matrículas fora da ordem crescente;
2. Exibe a estrutura do índice B+ resultante;
3. Busca o aluno de matrícula **50**;
4. Consulta matrículas no intervalo **20 a 60**;
5. Atualiza os dados do aluno de matrícula **50**;
6. Remove as matrículas **30, 70, 10, 65, 40 e 15**;
7. Exibe novamente a árvore após as remoções.

Essa sequência permite acompanhar a organização da estrutura e a relação entre chaves indexadas e registros armazenados.

## Conceitos estudados

- **Árvores multiway:** nós com múltiplas chaves ou referências a filhos;
- **Árvore B+:** separação entre nós internos de navegação e folhas com entradas indexadas;
- **Split:** divisão de um nó quando sua capacidade é ultrapassada;
- **Redistribuição e fusão:** mecanismos de ajuste após exclusões;
- **Índices de banco de dados:** associação entre chave de pesquisa e referência ao registro;
- **Consulta por intervalo:** aproveitamento das folhas ligadas em sequência.

## Restrições e limitações

O código foi escrito com foco na compreensão dos algoritmos e nas restrições de exercícios acadêmicos. Utiliza arrays fixos e estruturas de controle básicas, sem coleções Java ou bibliotecas externas.

**Este não é um SGBD completo.** Os registros existem apenas durante a execução; não há persistência em disco, SQL, transações ou recuperação de falhas. A exclusão da tabela é lógica: o registro é marcado como inativo e a chave é retirada do índice. Posições já ocupadas não são reutilizadas, portanto o limite de 100 registros corresponde ao número total de inserções realizadas nesta instância.

A implementação serve como estudo dos mecanismos de uma Árvore B+; seus invariantes e casos extremos ainda merecem validação com testes adicionais antes de qualquer uso além do contexto didático.

## Referências Bibliográficas

Este projeto foi desenvolvido com base nos fundamentos teóricos e algoritmos apresentados nas seguintes obras:

1. **SILBERSCHATZ, Abraham; KORTH, Henry F.; SUDARSHAN, S.** *Database System Concepts*. 7th Edition. McGraw-Hill, 2019.
   - Aplicação de Árvores B+ em sistemas de bancos de dados, organização de índices, busca por chave e consultas por intervalo.

2. **CORMEN, Thomas H.; LEISERSON, Charles E.; RIVEST, Ronald L.; STEIN, Clifford.** *Introduction to Algorithms*. 4th Edition. MIT Press, 2022.
   - Fundamentos de árvores balanceadas, operações de busca, inserção e remoção, além da análise de complexidade dos algoritmos.

3. **SZWARCFITER, Jayme Luiz; MARKENZON, Lilian.** *Estruturas de Dados e Seus Algoritmos*. 3ª edição. LTC, 2010.
   - Conceitos de estruturas de dados, árvores de múltiplos caminhos, organização hierárquica e manutenção de árvores de busca.
