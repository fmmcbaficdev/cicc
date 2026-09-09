import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { CadApi } from '../infrastructure/cad.api';
import { CadFacade } from './cad.facade';

describe('CadFacade', () => {
  let facade: CadFacade;
  let http: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CadFacade, CadApi, provideHttpClient(), provideHttpClientTesting()],
    });
    facade = TestBed.inject(CadFacade);
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    http.verify();
  });

  it('devolve a ligação da mesa quando o PABX responde', () => {
    let atual: { uid: string; telefone: string } | null | undefined;
    facade.carregarChamada().subscribe((chamada) => {
      atual = chamada;
    });

    http.expectOne('/pabx/chamada').flush({
      uid: 'pabx-mock-190-cba-001',
      telefone: '65981234567',
      tronco: '190',
      unidade: 'CBA',
      instante: '2026-09-07T22:40:00Z',
    });

    expect(atual?.uid).toBe('pabx-mock-190-cba-001');
    expect(atual?.telefone).toBe('65981234567');
  });

  it('PABX mudo devolve vazio e não impede abrir', () => {
    let atual: unknown = 'nao-chamou';
    facade.carregarChamada().subscribe((chamada) => {
      atual = chamada;
    });

    http.expectOne('/pabx/chamada').flush(null, { status: 404, statusText: 'Not Found' });

    expect(atual).toBeNull();
  });

  it('abrir devolve protocolo e T1', () => {
    let aberta: { protocolo: string; inicioAtendimento: string } | undefined;
    facade
      .abrir({
        natureza: 'Roubo',
        descricao: 'Roubo a mão armada agora',
        gravidade: 'CRITICA',
        endereco: 'Centro, Cuiabá',
        latitude: -15.6,
        longitude: -56.1,
        protocolo: 'CICC-2026-UI-001',
        pabxUid: 'pabx-mock-190-cba-001',
      })
      .subscribe((ocorrencia) => {
        aberta = ocorrencia;
      });

    http.expectOne('/ocorrencias').flush({
      id: 'occ-1',
      protocolo: 'CICC-2026-UI-001',
      natureza: 'Roubo',
      inicioAtendimento: '2026-09-07T22:30:00Z',
      telefone: '65981234567',
      pabxUid: 'pabx-mock-190-cba-001',
      endereco: 'Centro, Cuiabá',
      latitude: -15.6,
      longitude: -56.1,
      situacao: 'EM_TRIAGEM',
      mesa: null,
      encaminhadaEm: null,
    });

    expect(aberta?.protocolo).toBe('CICC-2026-UI-001');
    expect(aberta?.inicioAtendimento).toBe('2026-09-07T22:30:00Z');
  });
});
