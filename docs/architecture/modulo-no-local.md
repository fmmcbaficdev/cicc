# Sala — No local fecha o T2 (item 7)

## Objetivo

O despachador da mesa CBA registra **No local na mão** (rádio / tablet mudo) e a sala vê o **T2 fechado**. Carimbo marcado como **manual**.

## Contexto

- [component-map](component-map.md) — dono do chamado: `cad`; ação na mesa: `sala`
- [estrutura-hexagonal](estrutura-hexagonal.md) · [nomenclatura](nomenclatura.md)
- Backlog item 7 · R6 · [folha 2](../product/folha-02-sala-despacho.md)
- Sem tablet CIOSP Móvel, sem JPA. Devolver Livre: [modulo-encerrar.md](modulo-encerrar.md)

T1 não muda. T2 já começou no empenho. Clique do despachador fecha.

## Tasks

### Domínio

- [x] `Ocorrencia.registrarNoLocal` + `NO_LOCAL` — `hex-agregado`

### Application

- [x] `RegistrarNoLocalUseCase` — `hex-caso-de-uso`

### Infra

- [ ] JPA — fora desta fatia
- [x] `POST /ocorrencias/{id}/no-local` — `hex-adapter-rest`

### Frontend

- [x] Mesa: botão No local; T2 para no instante — `hex-feature-angular`

## Resultado

- `POST /ocorrencias/{id}/no-local` — `NO_LOCAL`, `noLocalEm`, `noLocalManual=true`
- Tela: T2 deixa de andar; rótulo manual
- Recusar se ainda não empenhou ou se já está no local

## Encerramento

Evidência: `mvn test` nos três módulos + `ng test` da mesa. Sprint 2 Goal completo no mock.
