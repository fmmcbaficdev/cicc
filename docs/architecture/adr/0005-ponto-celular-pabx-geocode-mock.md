# ADR 0005 — Ponto do celular no PABX; geocode por porta mock

- Status: aceita
- Data: 2026-09-08
- Decisores: PO
- Complementa: [0002](0002-cad-proprio-pabx.md) (contrato da ligação). Não substitui o CAD próprio nem o fallback à mão.

## Contexto

O PABX entrega latitude e longitude do **celular da ligação**. Esse ponto pode ser o local da ocorrência ou só de onde a pessoa ligou. O atendente precisa:

- ver o endereço correspondente no campo **Onde** (reverse geocode);
- **ajustar o ponto no mapa** se o fato for em outro lugar;
- informar um **ponto de referência** e vê-lo no mapa.

O contrato do ADR 0002 era `uid`, `telefone`, `tronco`, `unidade`, `instante` — sem coordenada. Nominatim, Google ou OSRM puxariam vendor, latência e política de uso para uma fatia de triagem.

*Ilities:* 3 (PABX alimenta o CAD) e 5 (sala registra mesmo se geocode ou PABX falhar). T1 continua no clique, não no ponto.

## Opções consideradas

1. **Só endereço digitado; mapa ilustrativo**
   - Prós: fatia já existia; zero dependência de geocode.
   - Contras: o PABX já manda o ponto do celular e a sala joga fora; o atendente redigita o “onde”.

2. **Reverse/forward geocode no vendor (Nominatim, Google, OSRM)**
   - Prós: endereço real da operadora de mapa.
   - Contras: ToS, quota, rede e outro SPOF no T1; *ility* 3 vira “PABX + mapa externo”. Sem driver para isso no piloto.

3. **Contrato PABX ganha lat/long; `GeocodificacaoPort` mock; mapa clicável no CAD**
   - Prós: a tela usa o ponto do celular; o atendente corrige; referência aparece no mapa; o hexágono troca o mock depois sem mudar use case.
   - Contras: o endereço escrito pelo mock não é o do mundo real; ajuste no mapa é grade de tiles, não SIOSP-GEO.

## Decisão

**Opção 3.** `Chamada` passa a carregar `pontoCelular` opcional. Sem ponto, o atendente segue à mão (ADR 0002). Com ponto, o CAD preenche lat/long e pede o endereço à `GeocodificacaoPort`. O ponto da ocorrência é o que o atendente confirma (clique no mapa ou digitação). `pontoReferencia` é texto de apoio; se o mock achar um marco, plota um segundo pino.

Trade-off aceito: **endereço e marco são de catálogo de demo**, não de um geocoder de produção. Homologar PABX real e um geocoder estadual continua hipótese — não Nominatim-por-padrão.

## Consequências

- Positivas: origem da ligação e local do fato ficam distintos na operação; ajuste manual não depende do PABX; porta pronta para um adapter real.
- Negativas / dívida: mock não cobre rua nova; tiles OSM só no piloto; `pontoReferencia` ainda não é geometria Spatial.
- *Ilities* afetadas: 3 (contrato PABX + lat/long); 5 (PABX sem coordenada ou geocode vazio não bloqueia abrir).
- Specs: `jornada.md` (Fase 1), `nomenclatura.md` (`GeocodificacaoPort`), `component-map.md`, `quality-attributes.md`, `checklist.md`.
