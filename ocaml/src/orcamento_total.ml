(*---------------------------------------------------------------------
  orcamento_total.ml
  combina peças, mão-de-obra, preço fixo e aplica descontos
---------------------------------------------------------------------*)

open Dominio_item
open Leitor_base_dados
open Orcamento           (* para desconto_marca, pecas_para_servicos, lucro_item *)
open Orcamento_mecanico  (* para orcamento_mecanico *)

(** Informações de uma peça no orçamento *)
type info_peca = {
  item          : tipo_item;
  preco_bruto   : float;  (* preco_venda_item *)
  desconto      : float;  (* preco_venda_item *. desconto_marca *)
  preco_liquido : float;  (* preco_bruto -. desconto *)
}

(** Informações de mão-de-obra por serviço *)
type info_mo = {
  id_servico    : int;
  horas         : float;
  custo_hora    : float;
  custo_bruto   : float; (* horas *. custo_hora *. num_mec *)
  desconto_mo    : float; (* custo_bruto *. taxa_desconto *)
  custo_liquido : float; (* custo_bruto -. desconto_mo *)
}

(** Resultado final do orçamento*)
type resumo_orcamento = {
  pecas           : info_peca list;
  mao_de_obra     : info_mo    list;
  custo_fixo      : float;
  soma_pecas      : float;  (* Σ preco_bruto *)
  soma_desc_pecas : float;  (* Σ desconto *)
  soma_mo         : float;  (* Σ custo_bruto *)
  soma_desc_mo     : float;  (* Σ desconto_mo *)
  total           : float;  (* soma_pecas + soma_mo + custo_fixo - soma_desc_pecas - soma_desc_mo *)
}

(** Gera o orçamento total para os serviços indicados *)
let orcamento_total (bd : t) (serv_ids : int list) : resumo_orcamento =
  (* 1. Peças mais lucrativas *)
  let pecas = pecas_para_servicos bd serv_ids in
  let info_pecas =
    List.map (fun it ->
      let desc_pct = Orcamento.desconto_marca bd it.marca_item in
      let preco_bruto = it.preco_venda_item in
      let desconto = preco_bruto *. desc_pct in
      let preco_liq = preco_bruto -. desconto in
      { item = it; preco_bruto; desconto; preco_liquido = preco_liq })
      pecas
  in
  (* 2. Mão de obra *)
  let infos_mo =
    orcamento_mecanico bd serv_ids
    |> List.map (fun (id, h, ch, bruto, desc, _) ->
      { id_servico    = id;
        horas         = h;
        custo_hora    = ch;
        custo_bruto   = bruto;
        desconto_mo    = desc;
        custo_liquido = bruto -. desc })
  in
  (* 3. Preço fixo dos serviços *)
  let custo_fixo =
    serv_ids
    |> List.filter_map (Hashtbl.find_opt bd.servicos)
    |> List.fold_left (fun acc s -> acc +. s.custo_fixo_servico) 0.0
  in
  (* 4. Somatórios *)
  let soma_pecas      = List.fold_left (fun acc p -> acc +. p.preco_bruto)      0.0 info_pecas in
  let soma_desc_pecas = List.fold_left (fun acc p -> acc +. p.desconto)         0.0 info_pecas in
  let soma_mo         = List.fold_left (fun acc m -> acc +. m.custo_bruto)      0.0 infos_mo   in
  let soma_desc_mo     = List.fold_left (fun acc m -> acc +. m.desconto_mo)       0.0 infos_mo   in
  (* 5. Total final *)
  let total =
    soma_pecas +. soma_mo +. custo_fixo -. soma_desc_pecas -. soma_desc_mo
  in
  { pecas           = info_pecas;
    mao_de_obra     = infos_mo;
    custo_fixo;
    soma_pecas;
    soma_desc_pecas;
    soma_mo;
    soma_desc_mo;
    total }
