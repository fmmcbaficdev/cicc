# Sala — fila sem Livre perto (item 6)

## Objetivo

Se não houver Livre a até **3 km** do ponto, o cartão fica na fila **Crítica → Alta → Média → Baixa** e o despachador da mesa CBA (piloto) pode **puxar outro bairro** — ainda pelas mais próximas, sem cerca de mesa.

## Contexto

- [component-map](component-map.md) — dono: `sala`; frota: porta `posicao-avl`
- [estrutura-hexagonal](estrutura-hexagonal.md) · [nomenclatura](nomenclatura.md)
- Backlog item 6 · R10 / R11 · [folha 2](../product/folha-02-sala-despacho.md)
- AVL **mock**. Sem Hungarian/OSRM, JPA, “No local” (item 7)

Bairro define a **mesa**, não o matching. Ampliar não escolhe viatura: só tira o raio de 3 km.

## Tasks

### Domínio

- [x] `Gravidade.ordemNaFila` — `hex-value-object`
- [x] `RaioProximidade` (perto = 3 km) — `hex-value-object`
- [x] `FilaMesa` + `MatchingProximidade` perto vs ampliar — `hex-agregado` / domínio `sala`

### Application

- [x] Ordenar `ListarOcorrenciasNaMesaUseCase`; `SugerirViaturasUseCase` com `ampliar` — `hex-caso-de-uso`

### Infra

- [ ] JPA — fora desta fatia
- [x] REST `?ampliar=` + mock com Livre longe (VG) — `hex-adapter-rest`

### Frontend

- [x] Mesa: gravidade na fila; botão puxar outro bairro — `hex-feature-angular`

## Resultado

- `GET /mesa/{mesa}/ocorrencias` — `NA_MESA` primeiro, Crítica no topo, T1 mais antigo no empate
- `GET /ocorrencias/{id}/viaturas-sugeridas` — Livre até 3 km
- `GET /ocorrencias/{id}/viaturas-sugeridas?ampliar=true` — mesmas regras, sem raio
- Tela: sem Livre perto, o cartão não some; despachador amplia ou rádio

## Encerramento

Evidência: `mvn test` nos três módulos + `ng test` da mesa. No local: [modulo-no-local.md](modulo-no-local.md).
