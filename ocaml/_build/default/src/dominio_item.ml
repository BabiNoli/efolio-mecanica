type tipo_item = {
  id_item          : int;
  nome_item        : string;
  marca_item       : string;
  categoria_item   : string;
  custo_item       : float;
  preco_venda_item : float;
  quantidade_item  : int;
}

type tipo_servico = {
  id_servico          : int;
  nome_servico        : string;
  categorias_req      : string list;
  num_mecanicos       : int;
  tempo_horas         : float;
  custo_fixo_servico  : float;
}

type tipo_mecanico = {
  id_mec   : int;
  nome_mec : string;
  custo_hora : float;
}

type tipo_desc_marca = { marca : string; taxa : float }
type tipo_desc_mao   = { lim_inf : float option; lim_sup : float option; taxa : float }
