import { Component, inject, OnInit, signal } from '@angular/core';

import { RelogioT1Component } from '../../cad/components/relogio-t1.component';
import { Ocorrencia } from '../../cad/domain/ocorrencia.model';
import { SalaFacade } from '../application/sala.facade';

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
  readonly erro = signal<string | null>(null);

  ngOnInit(): void {
    this.carregar();
  }

  carregar(): void {
    this.erro.set(null);
    this.facade.listar(this.mesa()).subscribe({
      next: (cartoes) => this.cartoes.set(cartoes),
      error: () => this.erro.set('Não foi possível carregar a mesa'),
    });
  }
}
