import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { debounceTime, distinctUntilChanged } from 'rxjs';

import { CadFacade } from '../application/cad.facade';
import { PontoAjuste, PontoMapaComponent } from '../components/ponto-mapa.component';
import { RelogioT1Component } from '../components/relogio-t1.component';
import { Chamada } from '../domain/chamada.model';
import { Ocorrencia } from '../domain/ocorrencia.model';

@Component({
  selector: 'app-abrir-ocorrencia-page',
  imports: [ReactiveFormsModule, PontoMapaComponent, RelogioT1Component],
  providers: [CadFacade],
  templateUrl: './abrir-ocorrencia.page.html',
  styleUrl: './abrir-ocorrencia.page.scss',
})
export class AbrirOcorrenciaPage implements OnInit {
  private readonly facade = inject(CadFacade);
  private readonly formBuilder = inject(FormBuilder);
  private readonly destroyRef = inject(DestroyRef);

  readonly gravidades = [
    { valor: 'BAIXA', rotulo: 'Baixa' },
    { valor: 'MEDIA', rotulo: 'Média' },
    { valor: 'ALTA', rotulo: 'Alta' },
    { valor: 'CRITICA', rotulo: 'Crítica' },
  ] as const;

  readonly formulario = this.formBuilder.nonNullable.group({
    protocolo: ['', Validators.required],
    telefone: [''],
    gravidade: ['CRITICA', Validators.required],
    natureza: ['', Validators.required],
    descricao: ['', Validators.required],
    endereco: ['', Validators.required],
    pontoReferencia: [''],
    latitude: [-15.601411, Validators.required],
    longitude: [-56.097892, Validators.required],
  });

  readonly chamada = signal<Chamada | null>(null);
  readonly pabxMudo = signal(false);
  readonly carregandoChamada = signal(true);
  readonly enviando = signal(false);
  readonly encaminhando = signal(false);
  readonly erro = signal<string | null>(null);
  readonly ocorrencia = signal<Ocorrencia | null>(null);
  readonly origemPonto = signal<'celular' | 'ajuste' | 'manual'>('manual');
  readonly referencia = signal<{ latitude: number; longitude: number } | null>(null);

  mesaPadrao(): string {
    const unidade = this.chamada()?.unidade;
    return unidade === 'VG' || unidade === 'RDO' || unidade === 'CBA' ? unidade : 'CBA';
  }

  ajudaDoMapa(): string {
    switch (this.origemPonto()) {
      case 'celular':
        return 'Ponto do celular da ligação — pode não ser o local da ocorrência. Clique no mapa para ajustar.';
      case 'ajuste':
        return 'Ponto ajustado pelo atendente. O endereço é recalculado a partir do clique.';
      default:
        return 'Ponto no mapa a partir do endereço informado — a ligação não traz latitude/longitude.';
    }
  }

  ngOnInit(): void {
    this.formulario.controls.pontoReferencia.valueChanges
      .pipe(debounceTime(350), distinctUntilChanged(), takeUntilDestroyed(this.destroyRef))
      .subscribe((texto) => this.resolverReferencia(texto));
    this.prepararNova();
  }

  abrir(): void {
    if (this.formulario.invalid || this.enviando()) {
      this.formulario.markAllAsTouched();
      return;
    }

    const valor = this.formulario.getRawValue();
    this.enviando.set(true);
    this.erro.set(null);

    this.facade
      .abrir({
        natureza: valor.natureza,
        descricao: valor.descricao,
        gravidade: valor.gravidade,
        endereco: valor.endereco,
        latitude: valor.latitude,
        longitude: valor.longitude,
        protocolo: valor.protocolo,
        telefone: valor.telefone || null,
        pabxUid: this.chamada()?.uid ?? null,
        pontoReferencia: valor.pontoReferencia || null,
      })
      .subscribe({
        next: (ocorrencia) => {
          this.enviando.set(false);
          this.ocorrencia.set(ocorrencia);
        },
        error: (falha: Error) => {
          this.enviando.set(false);
          this.erro.set(falha.message);
        },
      });
  }

  encaminhar(): void {
    const aberta = this.ocorrencia();
    if (!aberta || this.encaminhando() || aberta.situacao === 'NA_MESA') {
      return;
    }
    this.encaminhando.set(true);
    this.erro.set(null);
    this.facade.encaminhar(aberta.id, this.mesaPadrao()).subscribe({
      next: (ocorrencia) => {
        this.encaminhando.set(false);
        this.ocorrencia.set(ocorrencia);
      },
      error: (falha: Error) => {
        this.encaminhando.set(false);
        this.erro.set(falha.message);
      },
    });
  }

  novaOcorrencia(): void {
    this.ocorrencia.set(null);
    this.prepararNova();
  }

  ajustarPonto(ponto: PontoAjuste): void {
    this.formulario.patchValue({
      latitude: ponto.latitude,
      longitude: ponto.longitude,
    });
    this.origemPonto.set('ajuste');
    this.resolverEndereco(ponto.latitude, ponto.longitude);
  }

  private prepararNova(): void {
    this.erro.set(null);
    this.origemPonto.set('manual');
    this.referencia.set(null);
    this.formulario.reset({
      protocolo: this.proximoProtocolo(),
      telefone: '',
      gravidade: 'CRITICA',
      natureza: 'Roubo',
      descricao: 'Roubo a mão armada agora',
      endereco: 'Av. Historiador Rubens de Mendonça, Cuiabá',
      pontoReferencia: '',
      latitude: -15.601411,
      longitude: -56.097892,
    });
    this.carregarChamada();
  }

  private carregarChamada(): void {
    this.carregandoChamada.set(true);
    this.pabxMudo.set(false);
    this.chamada.set(null);

    this.facade.carregarChamada().subscribe({
      next: (chamada) => {
        this.carregandoChamada.set(false);
        this.chamada.set(chamada);
        this.pabxMudo.set(chamada === null);
        if (!chamada) {
          return;
        }
        this.formulario.patchValue({ telefone: chamada.telefone });
        if (chamada.latitude != null && chamada.longitude != null) {
          this.formulario.patchValue({
            latitude: chamada.latitude,
            longitude: chamada.longitude,
          });
          this.origemPonto.set('celular');
          this.resolverEndereco(chamada.latitude, chamada.longitude);
        }
      },
      error: () => {
        this.carregandoChamada.set(false);
        this.pabxMudo.set(true);
      },
    });
  }

  private resolverEndereco(latitude: number, longitude: number): void {
    this.facade.enderecoDoPonto(latitude, longitude).subscribe((endereco) => {
      if (endereco) {
        this.formulario.patchValue({ endereco });
      }
    });
  }

  private resolverReferencia(texto: string): void {
    if (!texto.trim()) {
      this.referencia.set(null);
      return;
    }
    this.facade.pontoDoTexto(texto).subscribe({
      next: (ponto) => this.referencia.set(ponto),
      error: () => this.referencia.set(null),
    });
  }

  private proximoProtocolo(): string {
    return `CICC-2026-${Date.now().toString().slice(-8)}`;
  }
}
