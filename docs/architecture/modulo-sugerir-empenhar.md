# Sala — sugerir Livre e empenhar (T2)

## Objetivo

O despachador da mesa CBA/VG/RDO confirma a viatura Livre mais próxima do ponto e a sala vê o T2 começar.

## Contexto

- [component-map](component-map.md) — dono: `sala`; frota: porta `posicao-avl`
- [estrutura-hexagonal](estrutura-hexagonal.md) · [nomenclatura](nomenclatura.md)
- Backlog item 5 · [folha 2](../product/folha-02-sala-despacho.md)
- AVL **mock** (posição informada). Sem GPS ao vivo, Hungarian/OSRM, fila nem “No local”.

Atendente não escolhe viatura. Matching ordena por distância haversine; o humano empilha.

## Tasks

### Domínio

- [x] `Ponto.distanciaEmMetros` — `hex-value-object`
- [x] `Ocorrencia.empenhar` carimba T2 — `hex-agregado`
- [x] `Recurso` + `AvlPort` + `MatchingProximidade` — `hex-repositorio-porta`

### Application

- [x] `SugerirViaturasUseCase` + `EmpenharViaturaUseCase` — `hex-caso-de-uso`

### Infra

- [ ] JPA — fora desta fatia (InMemory + mock AVL)
- [x] REST — `hex-adapter-rest`

### Frontend

- [x] Mesa `/sala` sugere e confirma — `hex-feature-angular`

## Resultado

- `GET /ocorrencias/{id}/viaturas-sugeridas` — até 3 Livre, mais próxima primeiro
- `POST /ocorrencias/{id}/empenhar` `{ "prefixo" }` — T2 no clique; viatura sai de Livre
- Tela da mesa mostra sugestão, prefixo empenhado e T2

## Encerramento

Evidência: `mvn test` nos três módulos + `ng test` da mesa. Sem fila (item 6) nem “No local” (item 7).
