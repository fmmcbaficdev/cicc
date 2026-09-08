import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Ocorrencia } from '../../cad/domain/ocorrencia.model';
import { SalaApi } from '../infrastructure/sala.api';

@Injectable()
export class SalaFacade {
  private readonly api = inject(SalaApi);

  listar(mesa: string): Observable<Ocorrencia[]> {
    return this.api.ocorrenciasNaMesa(mesa);
  }
}
