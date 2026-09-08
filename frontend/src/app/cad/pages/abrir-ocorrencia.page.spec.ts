import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { AbrirOcorrenciaPage } from './abrir-ocorrencia.page';

describe('AbrirOcorrenciaPage', () => {
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbrirOcorrenciaPage],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    http.verify();
  });

  it('mostra o T1 depois do clique de abrir', async () => {
    const fixture = TestBed.createComponent(AbrirOcorrenciaPage);
    fixture.detectChanges();
    http.expectOne('/pabx/chamada').flush({
      uid: 'pabx-mock-190-cba-001',
      telefone: '65981234567',
      tronco: '190',
      unidade: 'CBA',
      instante: '2026-09-07T22:40:00Z',
    });
    fixture.detectChanges();

        fixture.nativeElement.querySelector('button[type="submit"]').click();
    fixture.detectChanges();

    http.expectOne('/ocorrencias').flush({
      id: 'occ-1',
      protocolo: 'CICC-2026-UI-T1',
      inicioAtendimento: '2026-09-07T22:30:00Z',
      telefone: '65981234567',
      pabxUid: 'pabx-mock-190-cba-001',
      endereco: 'Av. Historiador Rubens de Mendonça, Cuiabá',
      latitude: -15.601411,
      longitude: -56.097892,
      situacao: 'EM_TRIAGEM',
      mesa: null,
      encaminhadaEm: null,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    const texto = fixture.nativeElement.textContent as string;
    expect(texto).toContain('Atendimento iniciado');
    expect(texto).toContain('CICC-2026-UI-T1');
    expect(texto).toContain('Av. Historiador Rubens de Mendonça, Cuiabá');
    expect(fixture.nativeElement.querySelector('.relogio-t1__tempo')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('.relogio-t1__tempo').textContent).toMatch(/\d+:\d{2}/);
    expect(texto).toContain('Encaminhar à mesa CBA');
    expect(fixture.nativeElement.querySelector('iframe[title="Ponto da ocorrência no mapa"]')).toBeTruthy();
  });

  it('permite abrir à mão quando o PABX está mudo', () => {
    const fixture = TestBed.createComponent(AbrirOcorrenciaPage);
    fixture.detectChanges();
    http.expectOne('/pabx/chamada').flush(null, { status: 404, statusText: 'Not Found' });
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('PABX sem ligação na mesa');
  });
});
