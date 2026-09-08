import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, of, throwError } from 'rxjs';

import { Chamada } from '../domain/chamada.model';
import { Ocorrencia } from '../domain/ocorrencia.model';
import { AbrirOcorrenciaRequest } from './abrir-ocorrencia.request';
import { ChamadaResponse } from './chamada.response';
import { OcorrenciaResponse } from './ocorrencia.response';

@Injectable({ providedIn: 'root' })
export class CadApi {
  private readonly http = inject(HttpClient);

  chamadaAtual(): Observable<Chamada | null> {
    return this.http.get<ChamadaResponse>('/pabx/chamada').pipe(
      map((response) => ({
        uid: response.uid,
        telefone: response.telefone,
        tronco: response.tronco,
        unidade: response.unidade,
        instante: response.instante,
      })),
      catchError((erro: HttpErrorResponse) => {
        if (erro.status === 404) {
          return of(null);
        }
        return throwError(() => erro);
      }),
    );
  }

  abrir(request: AbrirOcorrenciaRequest): Observable<Ocorrencia> {
    return this.http.post<OcorrenciaResponse>('/ocorrencias', request).pipe(
      map(paraOcorrencia),
      catchError((erro: HttpErrorResponse) =>
        throwError(() => new Error(mensagemDeErro(erro))),
      ),
    );
  }

  encaminhar(id: string, mesa: string): Observable<Ocorrencia> {
    return this.http.post<OcorrenciaResponse>(`/ocorrencias/${id}/encaminhar`, { mesa }).pipe(
      map(paraOcorrencia),
      catchError((erro: HttpErrorResponse) =>
        throwError(() => new Error(mensagemDeErro(erro))),
      ),
    );
  }
}

function paraOcorrencia(response: OcorrenciaResponse): Ocorrencia {
  return {
    id: response.id,
    protocolo: response.protocolo,
    inicioAtendimento: response.inicioAtendimento,
    telefone: response.telefone,
    pabxUid: response.pabxUid,
    endereco: response.endereco,
    latitude: response.latitude,
    longitude: response.longitude,
    situacao: response.situacao,
    mesa: response.mesa,
    encaminhadaEm: response.encaminhadaEm,
  };
}

function mensagemDeErro(erro: HttpErrorResponse): string {
  if (typeof erro.error === 'string' && erro.error.trim().length > 0) {
    return erro.error;
  }
  return 'Não foi possível abrir a ocorrência';
}
