open Dominio_item
open Leitor_base_dados     (* para tipo t e acesso às tabelas *)

(** procura desconto da marca; devolve 0.0 se não existir *)
let desconto_marca bd marca =
  match Hashtbl.find_opt bd.desc_marca marca with
  | Some d -> d
  | None   -> 0.0

(** lucro de uma peça, já com desconto da marca *)
let lucro_item bd (it : tipo_item) : float =
  let desc = desconto_marca bd it.marca_item in
  let preco_descontado = it.preco_venda_item *. (1.0 -. desc) in
  preco_descontado -. it.custo_item

(** devolve peça mais lucrativa de uma dada categoria, se existir *)
let peca_mais_lucrativa bd categoria : tipo_item option =
  Hashtbl.to_seq bd.itens
  |> Seq.map snd
  |> Seq.filter (fun it -> it.categoria_item = categoria && it.quantidade_item > 0)
  |> Seq.fold_left
       (fun melhor it ->
          match melhor with
          | None -> Some it
          | Some m ->
              if lucro_item bd it > lucro_item bd m then Some it else melhor)
       None

(** Dado [serv_ids], devolve lista de peças vencedoras, já ordenada
    por categoria para ficar legível. *)
let pecas_para_servicos bd (serv_ids : int list) : tipo_item list =
  (* 1. obter todas as categorias requeridas pelos serviços *)
  let cats =
    serv_ids
    |> List.filter_map (fun id -> Hashtbl.find_opt bd.servicos id)
    |> List.concat_map (fun s -> s.categorias_req)
    |> List.sort_uniq String.compare
  in
  (* 2. para cada categoria escolhe a peça com maior lucro *)
  cats
  |> List.filter_map (fun cat ->
      if String.trim cat = "" then
      None  (* Categoria vazia, ignora *)
    else
      match peca_mais_lucrativa bd cat with
      | Some p -> Some p
      | None ->
          Printf.eprintf "⚠️  Sem stock para a categoria %s\n%!" cat;
          None)


(*---------------------------------------------------------------------
  Exercício 4 a) e b)
---------------------------------------------------------------------*)

(** desconto aplicado em cada peça selecionada *)
let orcamento_desconto_items bd serv_ids : (int * float * float) list =
  let pecas = pecas_para_servicos bd serv_ids in
  List.map (fun it ->
    let pct   = desconto_marca bd it.marca_item in
    let valor = it.preco_venda_item *. pct in
    (it.id_item, pct, valor))
    pecas

(** preço fixo de cada serviço *)
let orcamento_preco_fixo bd serv_ids : (int * float) list =
  serv_ids
  |> List.filter_map (fun id -> Hashtbl.find_opt bd.servicos id)
  |> List.map (fun s -> (s.id_servico, s.custo_fixo_servico))
