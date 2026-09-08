import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Ocorrencia } from '../../cad/domain/ocorrencia.model';
import { ViaturaSugerida } from '../domain/viatura-sugerida.model';
import { SalaApi } from '../infrastructure/sala.api';

@Injectable()
export class SalaFacade {
  private readonly api = inject(SalaApi);

  listar(mesa: string): Observable<Ocorrencia[]> {
    return this.api.ocorrenciasNaMesa(mesa);
  }

  sugerir(ocorrenciaId: string): Observable<ViaturaSugerida[]> {
    return this.api.viaturasSugeridas(ocorrenciaId);
  }

  empenhar(ocorrenciaId: string, prefixo: string): Observable<Ocorrencia> {
    return this.api.empenhar(ocorrenciaId, prefixo);
  }
}
