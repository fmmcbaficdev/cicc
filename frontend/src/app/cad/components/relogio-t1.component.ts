import { Component, computed, DestroyRef, inject, input, signal } from '@angular/core';

import { formatarDecorrido } from '../domain/formatar-decorrido';

@Component({
  selector: 'app-relogio-t1',
  templateUrl: './relogio-t1.component.html',
  styleUrl: './relogio-t1.component.scss',
})
export class RelogioT1Component {
  readonly inicio = input.required<string>();
  readonly fim = input<string | null>(null);
  readonly compacto = input(false);
  readonly rotulo = input('T1');

  private readonly agora = signal(Date.now());

  readonly decorrido = computed(() => {
    const encerramento = this.fim();
    const referencia = encerramento ? new Date(encerramento).getTime() : this.agora();
    return formatarDecorrido(this.inicio(), referencia);
  });
  readonly inicioFormatado = computed(() =>
    new Date(this.inicio()).toLocaleString('pt-BR', { timeZone: 'America/Cuiaba' }),
  );

  constructor() {
    const id = setInterval(() => this.agora.set(Date.now()), 1000);
    inject(DestroyRef).onDestroy(() => clearInterval(id));
  }
}
