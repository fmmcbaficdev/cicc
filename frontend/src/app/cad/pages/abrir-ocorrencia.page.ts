import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { PontoMapaComponent } from '../components/ponto-mapa.component';
import { RelogioT1Component } from '../components/relogio-t1.component';
import { CadFacade } from '../application/cad.facade';
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

  mesaPadrao(): string {
    const unidade = this.chamada()?.unidade;
    return unidade === 'VG' || unidade === 'RDO' || unidade === 'CBA' ? unidade : 'CBA';
  }

  ngOnInit(): void {
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

  private prepararNova(): void {
    this.erro.set(null);
    this.formulario.reset({
      protocolo: this.proximoProtocolo(),
      telefone: '',
      gravidade: 'CRITICA',
      natureza: 'Roubo',
      descricao: 'Roubo a mão armada agora',
      endereco: 'Av. Historiador Rubens de Mendonça, Cuiabá',
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
        if (chamada) {
          this.formulario.patchValue({ telefone: chamada.telefone });
        }
      },
      error: () => {
        this.carregandoChamada.set(false);
        this.pabxMudo.set(true);
      },
    });
  }

  private proximoProtocolo(): string {
    return `CICC-2026-${Date.now().toString().slice(-8)}`;
  }
}
