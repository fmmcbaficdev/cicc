import { Component, computed, inject, input, output } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

const ZOOM = 16;

export interface PontoAjuste {
  latitude: number;
  longitude: number;
}

@Component({
  selector: 'app-ponto-mapa',
  templateUrl: './ponto-mapa.component.html',
  styleUrl: './ponto-mapa.component.scss',
})
export class PontoMapaComponent {
  private readonly sanitizer = inject(DomSanitizer);

  readonly latitude = input.required<number>();
  readonly longitude = input.required<number>();
  readonly endereco = input('');
  readonly ajustavel = input(false);
  readonly referenciaLatitude = input<number | null>(null);
  readonly referenciaLongitude = input<number | null>(null);
  readonly pontoAjustado = output<PontoAjuste>();

  readonly src = computed<SafeResourceUrl>(() => {
    const lat = this.latitude();
    const lon = this.longitude();
    const delta = 0.008;
    const url =
      `https://www.openstreetmap.org/export/embed.html?bbox=${lon - delta},${lat - delta},${lon + delta},${lat + delta}` +
      `&layer=mapnik&marker=${lat},${lon}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  });

  readonly tiles = computed(() => {
    const origem = this.origemGrade();
    const lista: { key: string; col: number; row: number; url: string }[] = [];
    for (let row = 0; row < 3; row++) {
      for (let col = 0; col < 3; col++) {
        const x = origem.tileX + col;
        const y = origem.tileY + row;
        lista.push({
          key: `${x}/${y}`,
          col: col + 1,
          row: row + 1,
          url: `https://tile.openstreetmap.org/${ZOOM}/${x}/${y}.png`,
        });
      }
    }
    return lista;
  });

  readonly pinOcorrencia = computed(() => this.pinDe(this.latitude(), this.longitude()));

  readonly pinReferencia = computed(() => {
    const lat = this.referenciaLatitude();
    const lon = this.referenciaLongitude();
    if (lat == null || lon == null) {
      return null;
    }
    return this.pinDe(lat, lon);
  });

  ajustar(evento: MouseEvent): void {
    if (!this.ajustavel()) {
      return;
    }
    const alvo = evento.currentTarget as HTMLElement;
    const box = alvo.getBoundingClientRect();
    if (box.width === 0 || box.height === 0) {
      return;
    }
    const origem = this.origemGrade();
    const worldX = origem.tileX + ((evento.clientX - box.left) / box.width) * 3;
    const worldY = origem.tileY + ((evento.clientY - box.top) / box.height) * 3;
    this.pontoAjustado.emit({
      latitude: Number(yParaLat(worldY, ZOOM).toFixed(6)),
      longitude: Number(xParaLon(worldX, ZOOM).toFixed(6)),
    });
  }

  private origemGrade(): { tileX: number; tileY: number } {
    return {
      tileX: Math.floor(lonParaX(this.longitude(), ZOOM)) - 1,
      tileY: Math.floor(latParaY(this.latitude(), ZOOM)) - 1,
    };
  }

  private pinDe(latitude: number, longitude: number): { x: number; y: number } | null {
    const origem = this.origemGrade();
    const x = ((lonParaX(longitude, ZOOM) - origem.tileX) / 3) * 100;
    const y = ((latParaY(latitude, ZOOM) - origem.tileY) / 3) * 100;
    if (x < 2 || x > 98 || y < 2 || y > 98) {
      return null;
    }
    return { x, y };
  }
}

function lonParaX(lon: number, zoom: number): number {
  return ((lon + 180) / 360) * Math.pow(2, zoom);
}

function latParaY(lat: number, zoom: number): number {
  const rad = (lat * Math.PI) / 180;
  return ((1 - Math.log(Math.tan(rad) + 1 / Math.cos(rad)) / Math.PI) / 2) * Math.pow(2, zoom);
}

function xParaLon(x: number, zoom: number): number {
  return (x / Math.pow(2, zoom)) * 360 - 180;
}

function yParaLat(y: number, zoom: number): number {
  const n = Math.PI * (1 - (2 * y) / Math.pow(2, zoom));
  return (Math.atan(Math.sinh(n)) * 180) / Math.PI;
}
