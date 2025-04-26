open Dominio_item   (* passa a conhecer tipo_item e seus campos *)
open Orcamento_total


let () =
  match Array.to_list Sys.argv with
  | _ :: "listar_items" :: _ ->
    (* 1. carregar toda a base de dados numa variável bd *)
    let bd = Leitor_base_dados.criar "database.pl" in
    Leitor_base_dados.garantir_dados bd;

    (* 2. imprimir resumo *)
    Printf.printf "Resumo: itens = %d   serviços = %d\n"
      (Hashtbl.length bd.itens)
      (Hashtbl.length bd.servicos);

    (* 3. iterar e imprimir cada item *)

    Leitor_base_dados.itens_ordenados bd
    |> List.iter (fun i ->
        Printf.printf "%d; %s; %s; %s; %.2f; %.2f; %d\n"
          i.id_item i.nome_item i.marca_item i.categoria_item
          i.custo_item i.preco_venda_item i.quantidade_item)

  | _ :: "orcamento_items" :: ids_raw :: _ ->
    (* 1. converte "1,2,7" em [1;2;7] *)
    let ids =
      String.split_on_char ',' ids_raw
      |> List.filter_map (fun s ->
            match int_of_string_opt (String.trim s) with
            | Some n -> Some n | None -> None)
    in
    let bd = Leitor_base_dados.criar "database.pl" in
    Leitor_base_dados.garantir_dados bd;
    let pecas = Orcamento.pecas_para_servicos bd ids in
    List.iter (fun i ->
      let lucro = Orcamento.lucro_item bd i in
      Printf.printf "%d; %s; %s; %s; %.2f; %.2f; %.2f\n"
        i.id_item i.nome_item i.marca_item i.categoria_item
        i.custo_item i.preco_venda_item lucro)
      pecas

  (* --- Orçamento de mão-de-obra --- *)
  | _ :: "orcamento_mecanico" :: ids_raw :: _ ->
    (* 1. transforma "1,2,5" em [1;2;5] *)
    let ids =
      String.split_on_char ',' ids_raw
      |> List.filter_map (fun s -> int_of_string_opt (String.trim s))
    in
    let bd = Leitor_base_dados.criar "database.pl" in
    Leitor_base_dados.garantir_dados bd;
    let orc = Orcamento_mecanico.orcamento_mecanico bd ids in
    List.iter (fun (id, h, ch, sem, desc, tot) ->
      Printf.printf "%d; %.2f; %.2f; %.2f; %.2f; %.2f\n"
        id h ch sem desc tot)
      orc

  (* a) desconto em peças *)
  | _ :: "orcamento_desconto_items" :: ids_raw :: _ ->
    let ids =
      String.split_on_char ',' ids_raw
      |> List.filter_map int_of_string_opt
    in
    let bd = Leitor_base_dados.criar "database.pl" in
    Leitor_base_dados.garantir_dados bd;
    Orcamento.orcamento_desconto_items bd ids
    |> List.iter (fun (id, pct, v) ->
        Printf.printf "%d; %.2f; %.2f\n" id (pct *. 100.0) v)

  (* b) preço fixo de serviço *)
  | _ :: "orcamento_preco_fixo" :: ids_raw :: _ ->
    let ids =
      String.split_on_char ',' ids_raw
      |> List.filter_map int_of_string_opt
    in
    let bd = Leitor_base_dados.criar "database.pl" in
    Leitor_base_dados.garantir_dados bd;
    Orcamento.orcamento_preco_fixo bd ids
    |> List.iter (fun (id, v) ->
        Printf.printf "%d; %.2f\n" id v)

  (* --- Orçamento Total --- *)
  | _ :: "orcamento_total" :: ids_raw :: _ ->
    let ids =
      String.split_on_char ',' ids_raw
      |> List.filter_map (fun s -> int_of_string_opt (String.trim s))
    in
    let bd = Leitor_base_dados.criar "database.pl" in
    Leitor_base_dados.garantir_dados bd;
    let resumo = Orcamento_total.orcamento_total bd ids in

    (* Imprime detalhes das peças *)
    print_endline "PEÇAS:";
    List.iter (fun p ->
      let it = p.item in
      Printf.printf "%d; %s; %s; %s; bruto=%.2f; desc=%.2f; líquido=%.2f\n"
        it.id_item it.nome_item it.marca_item it.categoria_item
        p.preco_bruto p.desconto p.preco_liquido)
      resumo.pecas;

    (* Imprime detalhes da mão-de-obra *)
    print_endline "\nMÃO DE OBRA:";
    List.iter (fun m ->
      Printf.printf "%d; horas=%.2f; ch=%.2f; bruto=%.2f; desc=%.2f; líquido=%.2f\n"
        m.id_servico m.horas m.custo_hora m.custo_bruto m.desconto_mo m.custo_liquido)
      resumo.mao_de_obra;

    (* Somatórios e total *)
    Printf.printf "\nCusto Fixo Serviços: %.2f\n" resumo.custo_fixo;
    Printf.printf "Σ Peças (bruto):      %.2f\n" resumo.soma_pecas;
    Printf.printf "Σ Desconto Peças:     %.2f\n" resumo.soma_desc_pecas;
    Printf.printf "Σ Mão-obra (bruto):   %.2f\n" resumo.soma_mo;
    Printf.printf "Σ Desconto Mão-obra:  %.2f\n" resumo.soma_desc_mo;
    Printf.printf "\nTotal Final:          %.2f\n" resumo.total


  (* ajuda *)
  | _ ->
    Printf.printf
      "Comandos disponíveis:\n\
      ├ listar_items\n\
      ├ orcamento_items   <ids>\n\
      ├ valor_mao_obra    <ids>\n\
      ├ orcamento_desconto_items <ids>\n\
      ├ orcamento_preco_fixo     <ids>\n\
      └ orcamento_total          <ids>\n"