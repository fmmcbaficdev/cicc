export interface Ocorrencia {
  id: string;
  protocolo: string;
  inicioAtendimento: string;
  telefone: string | null;
  pabxUid: string | null;
  endereco: string;
  latitude: number;
  longitude: number;
  situacao: string;
  mesa: string | null;
  encaminhadaEm: string | null;
  prefixoEmpenhado: string | null;
  inicioDeslocamento: string | null;
}
