import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';

import { Ocorrencia } from '../../cad/domain/ocorrencia.model';
import { OcorrenciaResponse, paraOcorrencia } from '../../cad/infrastructure/ocorrencia.response';
import { ViaturaSugerida } from '../domain/viatura-sugerida.model';

@Injectable({ providedIn: 'root' })
export class SalaApi {
  private readonly http = inject(HttpClient);

  ocorrenciasNaMesa(mesa: string): Observable<Ocorrencia[]> {
    return this.http.get<OcorrenciaResponse[]>(`/mesa/${mesa}/ocorrencias`).pipe(
      map((lista) => lista.map(paraOcorrencia)),
      catchError((erro: HttpErrorResponse) =>
        throwError(() => new Error(erro.status ? `Mesa indisponível (${erro.status})` : 'Mesa indisponível')),
      ),
    );
  }

  viaturasSugeridas(ocorrenciaId: string): Observable<ViaturaSugerida[]> {
    return this.http.get<ViaturaSugerida[]>(`/ocorrencias/${ocorrenciaId}/viaturas-sugeridas`).pipe(
      catchError((erro: HttpErrorResponse) =>
        throwError(() => new Error(erro.status ? `AVL indisponível (${erro.status})` : 'AVL indisponível')),
      ),
    );
  }

  empenhar(ocorrenciaId: string, prefixo: string): Observable<Ocorrencia> {
    return this.http
      .post<OcorrenciaResponse>(`/ocorrencias/${ocorrenciaId}/empenhar`, { prefixo })
      .pipe(
        map(paraOcorrencia),
        catchError((erro: HttpErrorResponse) =>
          throwError(() => new Error(mensagemDeErro(erro))),
        ),
      );
  }
}

function mensagemDeErro(erro: HttpErrorResponse): string {
  if (typeof erro.error === 'string' && erro.error.trim().length > 0) {
    return erro.error;
  }
  return 'Não foi possível empenhar a viatura';
}
