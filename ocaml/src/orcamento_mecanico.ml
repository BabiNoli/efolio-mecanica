open Dominio_item
open Leitor_base_dados

(** Calcula o custo/hora conforme a nova regra da oficina:
    - Se num_mecanicos = 1 → só Severo (ID 2)
    - Se num_mecanicos = 2 → Severo (8.00) + Ganancio (10.00) = média 9.00 *)
let custo_hora_mecanicos bd n_mec =
  match n_mec with
  | 1 -> (
      match Hashtbl.find_opt bd.mecanicos 2 with
      | Some severo -> severo.custo_hora
      | None -> failwith "Mecânico Severo (ID 2) não encontrado")
  | 2 -> (
      match (Hashtbl.find_opt bd.mecanicos 1, Hashtbl.find_opt bd.mecanicos 2) with
      | Some ganancio, Some severo ->
          (ganancio.custo_hora +. severo.custo_hora) /. 2.0
      | _ -> failwith "Ganancio (1) ou Severo (2) não encontrados na base de dados")
  | _ -> failwith "Mais de 2 mecânicos não suportado"

(** Regra de desconto por tempo de serviço
  desconto de mão-de-obra segundo regras carregadas em bd.desc_mao *)
let desconto_mao bd tempo =
  !(bd.desc_mao)
  |> List.find_opt (fun { lim_inf; lim_sup; _ } ->
       let ok_inf = match lim_inf with None -> true | Some li -> tempo >= li in
       let ok_sup = match lim_sup with None -> true | Some ls -> tempo <= ls in
       ok_inf && ok_sup)
  |> Option.map (fun r -> r.taxa)
  |> Option.value ~default:0.0


(** Gera o orçamento de mão-de-obra para os serviços indicados.
    Retorna uma lista de tuplos:
    (id_servico, horas, custo_hora, custo_sem_desc, valor_desconto, total) *)
let orcamento_mecanico bd serv_ids =
  serv_ids
  |> List.filter_map (fun id ->
       match Hashtbl.find_opt bd.servicos id with
       | None ->
           Printf.eprintf "⚠️  Serviço %d não encontrado\n%!" id;
           None
       | Some s -> Some s)
  |> List.map (fun s ->
       let h = s.tempo_horas in
       let n_mec = s.num_mecanicos in
       let ch = custo_hora_mecanicos bd n_mec in
       let custo_sem = h *. ch *. float_of_int n_mec in
       let taxa = desconto_mao bd h in
       let valor_desc = custo_sem *. taxa in
       let total = custo_sem -. valor_desc in
       (s.id_servico, h, ch, custo_sem, valor_desc, total))
