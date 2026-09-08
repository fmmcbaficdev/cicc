import { formatarDecorrido } from './formatar-decorrido';

describe('formatarDecorrido', () => {
  const inicio = '2026-09-07T22:30:00.000Z';

  it('mostra minutos e segundos abaixo de uma hora', () => {
    const agora = Date.parse(inicio) + 125_000;
    expect(formatarDecorrido(inicio, agora)).toBe('02:05');
  });

  it('mostra horas quando o T1 passa de 60 minutos', () => {
    const agora = Date.parse(inicio) + 3_661_000;
    expect(formatarDecorrido(inicio, agora)).toBe('1:01:01');
  });
});
