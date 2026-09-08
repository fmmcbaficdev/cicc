import { Component, inject, OnInit, signal } from '@angular/core';
import { forkJoin, map } from 'rxjs';

import { RelogioT1Component } from '../../cad/components/relogio-t1.component';
import { Ocorrencia } from '../../cad/domain/ocorrencia.model';
import { SalaFacade } from '../application/sala.facade';
import { ViaturaSugerida } from '../domain/viatura-sugerida.model';

@Component({
  selector: 'app-mesa-page',
  imports: [RelogioT1Component],
  providers: [SalaFacade],
  templateUrl: './mesa.page.html',
  styleUrl: './mesa.page.scss',
})
export class MesaPage implements OnInit {
  private readonly facade = inject(SalaFacade);

  readonly mesa = signal('CBA');
  readonly cartoes = signal<Ocorrencia[]>([]);
  readonly sugestoes = signal<Record<string, ViaturaSugerida[]>>({});
  readonly ampliado = signal<Record<string, boolean>>({});
  readonly empenhando = signal<string | null>(null);
  readonly erro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.erro.set(null);
    this.ampliado.set({});
    this.facade.listar(this.mesa()).subscribe({
      next: (cartoes) => {
        this.cartoes.set(cartoes);
        this.carregarSugestoes(cartoes.filter((cartao) => cartao.situacao === 'NA_MESA'));
      },
      error: (falha: Error) => this.erro.set(falha.message),
    });
  }

  empenhar(ocorrenciaId: string, prefixo: string): void {
    if (this.empenhando()) {
      return;
    }
    this.empenhando.set(ocorrenciaId);
    this.erro.set(null);
    this.facade.empenhar(ocorrenciaId, prefixo).subscribe({
      next: (atualizada) => {
        this.empenhando.set(null);
        this.cartoes.update((lista) => lista.map((cartao) => (cartao.id === atualizada.id ? atualizada : cartao)));
        this.sugestoes.update((atual) => {
          const proximo = { ...atual };
          delete proximo[ocorrenciaId];
          return proximo;
        });
        this.ampliado.update((atual) => {
          const proximo = { ...atual };
          delete proximo[ocorrenciaId];
          return proximo;
        });
      },
      error: (falha: Error) => {
        this.empenhando.set(null);
        this.erro.set(falha.message);
      },
    });
  }

  registrarNoLocal(ocorrenciaId: string): void {
    if (this.empenhando()) {
      return;
    }
    this.empenhando.set(ocorrenciaId);
    this.erro.set(null);
    this.facade.registrarNoLocal(ocorrenciaId).subscribe({
      next: (atualizada) => {
        this.empenhando.set(null);
        this.cartoes.update((lista) => lista.map((cartao) => (cartao.id === atualizada.id ? atualizada : cartao)));
      },
      error: (falha: Error) => {
        this.empenhando.set(null);
        this.erro.set(falha.message);
      },
    });
  }

  formatarDistancia(metros: number): string {
    return metros >= 1000 ? `${(metros / 1000).toFixed(1)} km` : `${metros} m`;
  }

  puxarOutroBairro(ocorrenciaId: string): void {
    this.erro.set(null);
    this.facade.sugerir(ocorrenciaId, true).subscribe({
      next: (lista) => {
        this.sugestoes.update((atual) => ({ ...atual, [ocorrenciaId]: lista }));
        this.ampliado.update((atual) => ({ ...atual, [ocorrenciaId]: true }));
      },
      error: (falha: Error) => this.erro.set(falha.message),
    });
  }

  private carregarSugestoes(pendentes: Ocorrencia[]): void {
    if (pendentes.length === 0) {
      this.sugestoes.set({});
      return;
    }
    forkJoin(
      pendentes.map((cartao) =>
        this.facade.sugerir(cartao.id).pipe(map((lista) => [cartao.id, lista] as const)),
      ),
    ).subscribe({
      next: (pares) => this.sugestoes.set(Object.fromEntries(pares)),
      error: (falha: Error) => this.erro.set(falha.message),
    });
  }
}
