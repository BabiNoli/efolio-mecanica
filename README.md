README - Projeto Oficina Mecânica
=============================================================================

Esse sistema automatiza o processo de geração de orçamentos para a oficina Mecânica do Sr. Ganâncio.
Ele integra um backend em OCaml para cálculo de orçamentos e uma interface gráfica em Java/Swing.

## Estrutura

- ocaml/  
  · Código-fonte OCaml (dune)  
  · database.pl (dados de itens e serviços)  
- java/  
  · App Swing em Java 17 (Maven)  
  · Integração com executável OCaml via `IntegradorOCaml`

## Pré-requisitos

### Versões Recomendadas

- OCaml: 5.3.0  
  • Testado com Dune ≥ 2.x  
  • Deve funcionar em toda a série 5.x  
  • Pode haver ajustes para versões 4.x ou inferiores  

- Java: 17  
  • Fonte e target configurados para 17 no `pom.xml`  
  • JDK 17 ou superior

#### No Linux nativo

  1. OCaml e Dune  
     sudo apt update && sudo apt install -y ocaml dune  
  2. OpenJDK 17 + Maven  
     sudo apt install -y openjdk-17-jdk maven

#### No Windows (via WSL)

  1. Habilite o WSL (PowerShell admin):  
     `wsl --install`  
  2. Reinicie e instale uma distro (ex: Ubuntu).  
  3. No terminal WSL, siga as instruções do Linux acima.

## Build e Execução

### 1. Backend OCaml

  cd efolio-mecanica/ocaml  
  dune build   # compila executável em _build/default/src/main.exe  

  #### Exemplos de uso:
  dune exec src/main.exe -- listar_items
  dune exec src/main.exe -- orcamento_total 11,12
  dune exec src/main.exe -- orcamento_mecanico 1,5
  dune exec src/main.exe -- orcamento_desconto_itens 13
  dune exec src/main.exe -- orcamento_preco_fixo 14,15	

### 2. UI Java

  cd efolio-mecanica/java  
  mvn clean package   # gera oficina-ui-1.0.0.jar em target/  

  #### Para rodar:
  java -jar target/oficina-ui-1.0.0.jar

  #### ou via Maven:
  mvn exec:java

A aplicação Java chama o executável OCaml (`../ocaml/_build/default/src/main.exe`) para obter linhas de orçamento.

Contacto:
Entre em contato para relatar problemas ou sugerir melhorias.

Envie um e-mail para barbara.it.noli@gmail.com

Grata pelo seu feedback!
