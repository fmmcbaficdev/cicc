export interface AbrirOcorrenciaRequest {
  descricao: string;
  gravidade: string;
  endereco: string;
  latitude: number;
  longitude: number;
  protocolo: string;
  telefone?: string | null;
  pabxUid?: string | null;
}
