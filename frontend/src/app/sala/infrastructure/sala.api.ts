import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { catchError, map, Observable, throwError } from 'rxjs';

import { Ocorrencia } from '../../cad/domain/ocorrencia.model';
import { OcorrenciaResponse } from '../../cad/infrastructure/ocorrencia.response';

@Injectable({ providedIn: 'root' })
export class SalaApi {
  private readonly http = inject(HttpClient);

  ocorrenciasNaMesa(mesa: string): Observable<Ocorrencia[]> {
    return this.http.get<OcorrenciaResponse[]>(`/mesa/${mesa}/ocorrencias`).pipe(
      map((lista) =>
        lista.map((response) => ({
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
        })),
      ),
      catchError((erro: HttpErrorResponse) =>
        throwError(() => new Error(erro.status ? `Mesa indisponível (${erro.status})` : 'Mesa indisponível')),
      ),
    );
  }
}
