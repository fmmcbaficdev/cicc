import { Component, computed, inject, input } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

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

  readonly src = computed<SafeResourceUrl>(() => {
    const lat = this.latitude();
    const lon = this.longitude();
    const delta = 0.008;
    const url =
      `https://www.openstreetmap.org/export/embed.html?bbox=${lon - delta},${lat - delta},${lon + delta},${lat + delta}` +
      `&layer=mapnik&marker=${lat},${lon}`;
    return this.sanitizer.bypassSecurityTrustResourceUrl(url);
  });
}
