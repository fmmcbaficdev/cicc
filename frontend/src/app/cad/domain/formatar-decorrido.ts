export function formatarDecorrido(inicioIso: string, agoraMs: number): string {
  const inicio = new Date(inicioIso).getTime();
  if (Number.isNaN(inicio)) {
    return '—';
  }
  const total = Math.max(0, Math.floor((agoraMs - inicio) / 1000));
  const horas = Math.floor(total / 3600);
  const minutos = Math.floor((total % 3600) / 60);
  const segundos = total % 60;
  const mm = String(minutos).padStart(2, '0');
  const ss = String(segundos).padStart(2, '0');
  return horas > 0 ? `${horas}:${mm}:${ss}` : `${mm}:${ss}`;
}
