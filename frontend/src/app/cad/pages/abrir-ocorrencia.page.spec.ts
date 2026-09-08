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
    });
    fixture.detectChanges();
    await fixture.whenStable();

    const texto = fixture.nativeElement.textContent as string;
    expect(texto).toContain('Atendimento iniciado');
    expect(texto).toContain('CICC-2026-UI-T1');
  });

  it('permite abrir à mão quando o PABX está mudo', () => {
    const fixture = TestBed.createComponent(AbrirOcorrenciaPage);
    fixture.detectChanges();
    http.expectOne('/pabx/chamada').flush(null, { status: 404, statusText: 'Not Found' });
    fixture.detectChanges();

    expect(fixture.nativeElement.textContent).toContain('PABX sem ligação na mesa');
  });
});
