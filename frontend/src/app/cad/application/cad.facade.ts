import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Chamada } from '../domain/chamada.model';
import { Ocorrencia } from '../domain/ocorrencia.model';
import { AbrirOcorrenciaRequest } from '../infrastructure/abrir-ocorrencia.request';
import { CadApi } from '../infrastructure/cad.api';

@Injectable()
export class CadFacade {
  private readonly api = inject(CadApi);

  carregarChamada(): Observable<Chamada | null> {
    return this.api.chamadaAtual();
  }

  abrir(request: AbrirOcorrenciaRequest): Observable<Ocorrencia> {
    return this.api.abrir(request);
  }

  encaminhar(id: string, mesa: string): Observable<Ocorrencia> {
    return this.api.encaminhar(id, mesa);
  }

  enderecoDoPonto(latitude: number, longitude: number): Observable<string | null> {
    return this.api.enderecoDoPonto(latitude, longitude);
  }

  pontoDoTexto(texto: string): Observable<{ latitude: number; longitude: number } | null> {
    return this.api.pontoDoTexto(texto);
  }
}
