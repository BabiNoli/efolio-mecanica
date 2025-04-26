(* leitor_base_dados.ml  *)
(* ----------------------------------------------------------- *)
(* 1. Módulos que vamos usar                                   *)
(* ----------------------------------------------------------- *)
open Str              (* funções de expressão-regular          *)
open Dominio_item     (* tipos: tipo_item, tipo_servico, …     *)
open Unix             (* para Unix.stat – detectar alteração   *)

(* ----------------------------------------------------------- *)
(* 2. Estrutura que guarda todos os dados em memória           *)
(* ----------------------------------------------------------- *)
type t = {
  caminho_ficheiro       : string;          (* onde está o database.pl *)
  mutable instante_ultimo_load : float;     (* POSIX time do último parse *)

  itens       : (int, tipo_item)       Hashtbl.t;
  servicos    : (int, tipo_servico)    Hashtbl.t;
  mecanicos   : (int, tipo_mecanico)   Hashtbl.t;
  desc_marca  : (string, float)        Hashtbl.t;   (* marca → % *)
  desc_mao    : tipo_desc_mao list ref;             (* lista de regras *)
}

(* ----------------------------------------------------------- *)
(* 3. Construtor                                               *)
(* ----------------------------------------------------------- *)
let criar caminho =
  { caminho_ficheiro = caminho;
    instante_ultimo_load = 0.0;

    itens       = Hashtbl.create 64;
    servicos    = Hashtbl.create 32;
    mecanicos   = Hashtbl.create 32;
    desc_marca  = Hashtbl.create 16;
    desc_mao    = ref [];
  }

(* ----------------------------------------------------------- *)
(* 4. Funções auxiliares: ler linhas e saber se ficheiro mudou *)
(* ----------------------------------------------------------- *)
let ler_linhas caminho =
  let canal = open_in caminho in
  let rec aux acc =
    match input_line canal with
    | linha -> aux (linha :: acc)
    | exception End_of_file -> close_in canal; List.rev acc
  in
  aux []

let ficheiro_modificado caminho instante =
  (stat caminho).st_mtime > instante

(* ----------------------------------------------------------- *)
(* 5. Regexes para cada entidade                               *)
(* ----------------------------------------------------------- *)
let regex_item =
  Str.regexp "item(\\([0-9]+\\), '\\([^']+\\)', '\\([^']+\\)', '\\([^']+\\)', \\([0-9.]+\\), \\([0-9.]+\\), \\([0-9]+\\))."
  
let regex_mecanico =
  Str.regexp "mecanico(\\([0-9]+\\), \"\\([^\"]+\\)\", \\([0-9.]+\\))."

let regex_desc_marca =
  Str.regexp "desconto_marca('\\([^']+\\)', \\([0-9.]+\\))."

let regex_desc_mao =
  Str.regexp "desconto_mao_obra(\\([<>][0-9.]+\\), \\([0-9.]+\\))."

(* ----------------------------------------------------------- *)
(* 6. Parse de uma única linha                                 *)
(* ----------------------------------------------------------- *)

(* -------- novo parser de servico sem regex gigante -------- *)
let parse_linha_servico bd linha =
  let linha = String.trim linha in
  if String.starts_with ~prefix:"servico(" linha then
    (* retira prefixo "servico(" e sufixo ")." ou ")" *)
    let len   = String.length linha in
    let conteudo =
      if linha.[len - 1] = '.'                       (* termina ")." *)
      then String.sub linha 8 (len - 10)
      else String.sub linha 8 (len - 9)
    in
    (* separa os 3 campos finais de trás-para-frente *)
    let partes_rev = List.rev (String.split_on_char ',' conteudo) in
    match partes_rev with
    | custo_s :: tempo_s :: mec_s :: resto_rev ->
        let custo = float_of_string (String.trim custo_s) in
        let tempo = float_of_string (String.trim tempo_s) in
        let n_mec = int_of_string  (String.trim mec_s)   in
        let resto = String.concat "," (List.rev resto_rev) in
        (* resto: id , 'Nome' , [lista] *)
        let id_end = String.index resto ',' in
        let id     = int_of_string (String.sub resto 0 id_end) in
        let pos_abre = String.index resto '[' in
        let nome =
          String.sub resto (id_end + 1) (pos_abre - id_end - 2) |> String.trim
        in
        let lista_raw =
          String.sub resto pos_abre (String.length resto - pos_abre)
        in
        let categorias =
          let sem_colchetes =
            String.sub lista_raw 1 (String.length lista_raw - 2)
          in
          sem_colchetes
          |> String.split_on_char ','
          |> List.map (fun s ->
                s |> String.trim |> Str.global_replace (Str.regexp "'") "")
        in
        let novo = {
          id_servico         = id;
          nome_servico       = Str.global_replace (Str.regexp "'") "" nome;
          categorias_req     = categorias;
          num_mecanicos      = n_mec;
          tempo_horas        = tempo;
          custo_fixo_servico = custo;
        } in
        Hashtbl.replace bd.servicos id novo
    | _ ->
        Printf.eprintf "✗ Falha inesperada no parse de serviço:\n%s\n%!" linha


let parse_linha bd linha =
  if Str.string_match regex_item linha 0 then
    (* ------------------ ITEM ------------------ *)
    (try
      let id = int_of_string (matched_group 1 linha) in
      let novo = {
        id_item          = id;
        nome_item        = matched_group 2 linha;
        marca_item       = matched_group 3 linha;
        categoria_item   = matched_group 4 linha;
        custo_item       = float_of_string (matched_group 5 linha);
        preco_venda_item = float_of_string (matched_group 6 linha);
        quantidade_item  = int_of_string (matched_group 7 linha);
      } in
      Hashtbl.replace bd.itens id novo
    with Invalid_argument _ ->
      Printf.eprintf "✗ Regex ITEM falhou nesta linha:\n%s\n%!" linha)

  else if string_match regex_mecanico linha 0 then
    (* --------------- MECÂNICO ------------------ *)
    (try
      let id = int_of_string (matched_group 1 linha) in
      let novo = {
        id_mec    = id;
        nome_mec  = matched_group 2 linha;
        custo_hora= float_of_string (matched_group 3 linha);
      } in
      Hashtbl.replace bd.mecanicos id novo
    with Invalid_argument _ ->
      Printf.eprintf "✗ REGEX MECANICO falhou:\n%s\n%!" linha)

  else if string_match regex_desc_marca linha 0 then
    (* --------- DESCONTO POR MARCA -------------- *)
    (try
      let marca = matched_group 1 linha in
      let taxa  = float_of_string (matched_group 2 linha) in
      Hashtbl.replace bd.desc_marca marca taxa
    with Invalid_argument _ ->
      Printf.eprintf "✗ REGEX DESC_MARCA falhou:\n%s\n%!" linha)
  
  else if string_match regex_desc_mao linha 0 then
    (* ------ DESCONTO POR MÃO-DE-OBRA ----------- *)
    (try
      let lim = matched_group 1 linha in
      let taxa = float_of_string (matched_group 2 linha) in
      let (lim_inf, lim_sup) =
        if String.get lim 0 = '<' then
          (None, Some (float_of_string (String.sub lim 1 (String.length lim - 1))))
        else
          (Some (float_of_string (String.sub lim 1 (String.length lim - 1))), None)
      in
      bd.desc_mao := { lim_inf; lim_sup; taxa } :: !(bd.desc_mao)
    with Invalid_argument _ ->
      Printf.eprintf "✗ REGEX DESC_MAO falhou:\n%s\n%!" linha)
  
  else
    parse_linha_servico bd linha
  
(* ----------------------------------------------------------- *)
(* 7. Recarregar todo o ficheiro se ele mudou                  *)
(* ----------------------------------------------------------- *)
let recarregar bd =
  Hashtbl.clear bd.itens;
  Hashtbl.clear bd.servicos;
  Hashtbl.clear bd.mecanicos;
  Hashtbl.clear bd.desc_marca;
  bd.desc_mao := [];

  ler_linhas bd.caminho_ficheiro |> List.iter (parse_linha bd);
  bd.instante_ultimo_load <- (stat bd.caminho_ficheiro).st_mtime

let garantir_dados bd =
  if ficheiro_modificado bd.caminho_ficheiro bd.instante_ultimo_load then
    recarregar bd

(* ----------------------------------------------------------- *)
(* 8. Funções “públicas” que o main.ml vai usar                 *)
(* ----------------------------------------------------------- *)
let carregar_itens caminho =
  let bd = criar caminho in
  garantir_dados bd;
  (* devolve apenas a lista de itens; o bd completo será usado
     por outras funções depois *)
  Hashtbl.to_seq bd.itens |> List.of_seq |> List.map snd

(** ---- lista de itens já ordenada por Categoria → Marca → Nome ------ *)
let itens_ordenados (bd : t) : tipo_item list =
  Hashtbl.to_seq bd.itens
  |> List.of_seq |> List.map snd
  |> List.sort (fun a b ->
       let c1 = compare a.categoria_item b.categoria_item in
       if c1 <> 0 then c1 else
       let c2 = compare a.marca_item b.marca_item in
       if c2 <> 0 then c2 else
       compare a.nome_item b.nome_item)
