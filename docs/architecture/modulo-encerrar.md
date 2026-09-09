# Sala — encerrar e devolver Livre (Fase 4)

## Objetivo

Depois do No local, o despachador **encerra o caso** na mesa CBA. A viatura volta a **Livre** e pode ser sugerida de novo. Sem relatório de campo.

## Contexto

- [component-map](component-map.md) — dono do chamado: `cad`; frota: `posicao-avl`
- [jornada](../product/jornada.md) Fase 4 · estados `Livre → Empenhada → No local → Livre`
- [modulo-no-local.md](modulo-no-local.md) deixou a frota empenhada de propósito
- Sem JPA, sem isolamento, sem PABX/AVL reais

T1 e T2 fechado não mudam.

## Tasks

### Domínio

- [x] `Ocorrencia.encerrar` + `AvlPort.liberar` — `hex-agregado` · `hex-repositorio-porta`

### Application

- [x] `EncerrarOcorrenciaUseCase` — `hex-caso-de-uso`

### Infra

- [ ] JPA — fora desta fatia
- [x] `POST /ocorrencias/{id}/encerrar` — `hex-adapter-rest`

### Frontend

- [x] Mesa: botão Encerrar no cartão No local — `hex-feature-angular`

## Resultado

- `POST /ocorrencias/{id}/encerrar` — `ENCERRADA`, `encerradaEm`; prefixo volta a Livre no mock
- Recusar se ainda não está no local ou se já encerrou

## Encerramento

Evidência: `mvn test` nos três módulos + `ng test` da mesa.
