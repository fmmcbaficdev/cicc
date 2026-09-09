import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { MesaPage } from './mesa.page';

describe('MesaPage', () => {
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MesaPage],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();
    http = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    http.verify();
  });

  it('mostra a Livre mais próxima e empenha com T2', async () => {
    const fixture = TestBed.createComponent(MesaPage);
    fixture.detectChanges();

    http.expectOne('/mesa/CBA/ocorrencias').flush([
      {
        id: 'occ-1',
        protocolo: 'CICC-2026-MESA-T2',
        natureza: 'Roubo',
        gravidade: 'CRITICA',
        inicioAtendimento: '2026-09-07T22:30:00Z',
        telefone: '65981234567',
        pabxUid: 'pabx-mock-190-cba-001',
        endereco: 'Av. Historiador Rubens de Mendonça, Cuiabá',
        latitude: -15.601411,
        longitude: -56.097892,
        situacao: 'NA_MESA',
        mesa: 'CBA',
        encaminhadaEm: '2026-09-07T22:32:00Z',
        prefixoEmpenhado: null,
        inicioDeslocamento: null,
      },
    ]);
    fixture.detectChanges();

    http.expectOne('/ocorrencias/occ-1/viaturas-sugeridas').flush([
      { prefixo: 'PM-CBA-01', latitude: -15.6018, longitude: -56.0982, distanciaMetros: 90 },
    ]);
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('Roubo');
    expect(fixture.nativeElement.textContent).toContain('PM-CBA-01');
    fixture.nativeElement.querySelector('button.empenhar').click();
    fixture.detectChanges();

    http.expectOne('/ocorrencias/occ-1/empenhar').flush({
      id: 'occ-1',
      protocolo: 'CICC-2026-MESA-T2',
      natureza: 'Roubo',
      gravidade: 'CRITICA',
      inicioAtendimento: '2026-09-07T22:30:00Z',
      telefone: '65981234567',
      pabxUid: 'pabx-mock-190-cba-001',
      endereco: 'Av. Historiador Rubens de Mendonça, Cuiabá',
      latitude: -15.601411,
      longitude: -56.097892,
      situacao: 'EMPENHADA',
      mesa: 'CBA',
      encaminhadaEm: '2026-09-07T22:32:00Z',
      prefixoEmpenhado: 'PM-CBA-01',
      inicioDeslocamento: '2026-09-07T22:35:00Z',
    });
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('Empenhada PM-CBA-01');
    expect(fixture.nativeElement.textContent).toContain('T2');
    fixture.nativeElement.querySelector('button.no-local').click();
    fixture.detectChanges();

    http.expectOne('/ocorrencias/occ-1/no-local').flush({
      id: 'occ-1',
      protocolo: 'CICC-2026-MESA-T2',
      natureza: 'Roubo',
      gravidade: 'CRITICA',
      inicioAtendimento: '2026-09-07T22:30:00Z',
      telefone: '65981234567',
      pabxUid: 'pabx-mock-190-cba-001',
      endereco: 'Av. Historiador Rubens de Mendonça, Cuiabá',
      latitude: -15.601411,
      longitude: -56.097892,
      situacao: 'NO_LOCAL',
      mesa: 'CBA',
      encaminhadaEm: '2026-09-07T22:32:00Z',
      prefixoEmpenhado: 'PM-CBA-01',
      inicioDeslocamento: '2026-09-07T22:35:00Z',
      noLocalEm: '2026-09-07T22:48:00Z',
      noLocalManual: true,
    });
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('No local (manual)');
    fixture.nativeElement.querySelector('button.encerrar').click();
    fixture.detectChanges();

    http.expectOne('/ocorrencias/occ-1/encerrar').flush({
      id: 'occ-1',
      protocolo: 'CICC-2026-MESA-T2',
      natureza: 'Roubo',
      gravidade: 'CRITICA',
      inicioAtendimento: '2026-09-07T22:30:00Z',
      telefone: '65981234567',
      pabxUid: 'pabx-mock-190-cba-001',
      endereco: 'Av. Historiador Rubens de Mendonça, Cuiabá',
      latitude: -15.601411,
      longitude: -56.097892,
      situacao: 'ENCERRADA',
      mesa: 'CBA',
      encaminhadaEm: '2026-09-07T22:32:00Z',
      prefixoEmpenhado: 'PM-CBA-01',
      inicioDeslocamento: '2026-09-07T22:35:00Z',
      noLocalEm: '2026-09-07T22:48:00Z',
      noLocalManual: true,
      encerradaEm: '2026-09-07T23:00:00Z',
    });
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('voltou a Livre');
  });

  it('sem Livre perto oferece puxar outro bairro', async () => {
    const fixture = TestBed.createComponent(MesaPage);
    fixture.detectChanges();

    http.expectOne('/mesa/CBA/ocorrencias').flush([
      {
        id: 'occ-2',
        protocolo: 'CICC-2026-MESA-FILA',
        natureza: 'Furto',
        gravidade: 'BAIXA',
        inicioAtendimento: '2026-09-07T22:30:00Z',
        telefone: null,
        pabxUid: null,
        endereco: 'Centro, Cuiabá',
        latitude: -15.601411,
        longitude: -56.097892,
        situacao: 'NA_MESA',
        mesa: 'CBA',
        encaminhadaEm: '2026-09-07T22:32:00Z',
        prefixoEmpenhado: null,
        inicioDeslocamento: null,
      },
    ]);
    fixture.detectChanges();

    http.expectOne('/ocorrencias/occ-2/viaturas-sugeridas').flush([]);
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('Puxar outro bairro');
    fixture.nativeElement.querySelector('button.ampliar').click();
    fixture.detectChanges();

    http.expectOne('/ocorrencias/occ-2/viaturas-sugeridas?ampliar=true').flush([
      { prefixo: 'PM-VG-01', latitude: -15.6465, longitude: -56.1326, distanciaMetros: 6200 },
    ]);
    fixture.detectChanges();
    await fixture.whenStable();

    expect(fixture.nativeElement.textContent).toContain('PM-VG-01');
  });
});
