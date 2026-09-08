# Product Backlog

O que faremos **agora**. Universo conhecido: [levantamento](levantamento-funcional.md). Origem da ordem: [folha 1](folha-01-cad-atendente.md) (após PABX) e [folha 2](folha-02-sala-despacho.md) (após AVL).

Status: `Hipótese` | `Pronto para refinar` | `Pronto para Sprint` | `Feito` | `Descartado`

| Ordem | Origem | Item | Valor para o usuário | Dependências | Recorte | Status |
|---|---|---|---|---|---|---|
| 1 | C3, C11 · Folha 1 | Abrir ocorrência no CAD (o quê, onde, gravidade Baixa/Média/Alta/Crítica) + carimbar T1 no clique | Atendente registra no CAD nosso; a sala vê quando o atendimento começou | Ossatura `cicc-cad/` | Sprint 1 · `cad` | Feito — `POST /ocorrencias` |
| 2 | C3 · Folha 1 (evento) | Abrir o chamado **à mão** se o PABX estiver mudo | A sala não para sem correlação automática | Item 1 | Sprint 1 · `cad` | Feito — uid ausente ou PABX mudo não impede o T1 |
| 3 | C2 · Folha 1 item 4 | Porta `pabx` + **adapter mock**; telefone/uid quando houver contrato | Evita redigitar; não bloqueia o T1 | Item 1 · contrato PABX | Sprint 1 (mock) / depois (real) | Feito no mock — `GET /pabx/chamada` |
| 4 | C8 · Folha 1 item 5 | Encaminhar o cartão à mesa do despachador da região | Fecha a triagem; alimenta a `sala` | Item 1 | Fim da Sprint 1 ou início da 2 | Feito — `NA_MESA` + `GET /mesa/{mesa}/ocorrencias` |
| 5 | C8, C7 · Folha 2 | Mesa da região + sugerir Livre mais próximas (AVL **mock**) + despachador confirma empenho + T2 começa | Viatura sai empenhada; a sala vê o deslocamento | Itens 1 e 4 | Sprint 2 · `sala` | Feito — mock AVL + `POST /ocorrencias/{id}/empenhar` |
| 6 | R10, R11 · Folha 2 | Sem Livre perto: fila por gravidade **e** puxar outro bairro (ainda por proximidade) | A mesa não trava | Item 5 | Sprint 2 · `sala` | Feito — raio 3 km + `?ampliar=true` |
| 7 | C6 · Folha 2 | Registrar “No local” (manual do despachador se o tablet falhar) e fechar T2 | Segundo relógio oficial fecha | Item 5 | Sprint 2 · `sala` | Feito — `POST /ocorrencias/{id}/no-local` |
| 8 | C10 · Folha 1 item 6 | Isolar quem opera em CBA / VG / RDO (mínimo) | Cartão não vaza de unidade | Item 1 | Quando a 2ª unidade entrar | Hipótese |
| 9 | C2 | Adapter PABX **real** (uid, telefone, tronco, unidade) | Liga a mesa sem redigitar o número | Homologação + item 3 | Depois do contrato | Hipótese |
| 10 | C7, C9 | AVL real + ficha CIOSP Móvel (rádio já é fallback) | Campo com GPS e tablet | Inventário 3 cidades | Depois da Sprint 2 | Hipótese |

## Fora deste backlog (até haver Goal próprio)

| Item | Origem | Status |
|---|---|---|
| Alerta LPR (não abre ocorrência) | C17 · Folha 1/2 item 8 | Descartado desta fila — MVP do produto, **não** destas Sprints |
| Painel gerencial T1/T2 | C15 | Descartado desta fila |
| Cabine Lilás | R8 | Descartado desta fila |
| Facial / CPF federado | C14 | Descartado — fora do piloto |

## Sprint atual (compromisso)

**Goal Sprint 1 (fechado):** o atendente registra o 190/193 e a operação vê o T1. Itens 1–4.

**Goal Sprint 2:** o despachador empilha a Livre mais próxima e a sala vê o T2. Itens 5–7 feitos no mock. AVL **real** não entra.
