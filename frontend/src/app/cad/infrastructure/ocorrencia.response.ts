import { Ocorrencia } from '../domain/ocorrencia.model';

export interface OcorrenciaResponse {
  id: string;
  protocolo: string;
  natureza: string;
  gravidade: string;
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
  noLocalEm: string | null;
  noLocalManual: boolean;
  encerradaEm: string | null;
}

export function paraOcorrencia(response: OcorrenciaResponse): Ocorrencia {
  return {
    id: response.id,
    protocolo: response.protocolo,
    natureza: response.natureza,
    gravidade: response.gravidade,
    inicioAtendimento: response.inicioAtendimento,
    telefone: response.telefone,
    pabxUid: response.pabxUid,
    endereco: response.endereco,
    latitude: response.latitude,
    longitude: response.longitude,
    situacao: response.situacao,
    mesa: response.mesa,
    encaminhadaEm: response.encaminhadaEm,
    prefixoEmpenhado: response.prefixoEmpenhado ?? null,
    inicioDeslocamento: response.inicioDeslocamento ?? null,
    noLocalEm: response.noLocalEm ?? null,
    noLocalManual: response.noLocalManual ?? false,
    encerradaEm: response.encerradaEm ?? null,
  };
}
