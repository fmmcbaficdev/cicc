# Docs do cicc-cad

Tudo o que descreve **esta** implementação mora aqui. O código está em `backend/` e `frontend/`.

## Como se constrói

| Doc | O que responde |
|---|---|
| [checklist-proximo.md](checklist-proximo.md) | Feito vs próximo recorte (Sprint 2 fechada no mock) |
| [fluxo-componentes.md](fluxo-componentes.md) | Ordem dos módulos (`cad`, `pabx`, `sala`, AVL) |
| [fluxo-desenvolvimento-codigo.md](fluxo-desenvolvimento-codigo.md) | Esteira hexagonal (VO → use case → REST → Angular) |
| [exemplo-abrir-ocorrencia.json](exemplo-abrir-ocorrencia.json) | Corpo do `POST /ocorrencias` |

## Produto

| Doc | O que responde |
|---|---|
| [product/visao-geral.md](product/visao-geral.md) | Problema, usuários, escopo |
| [product/jornada.md](product/jornada.md) | Ligação → triagem → mesa → T2 → no local |
| [product/levantamento-funcional.md](product/levantamento-funcional.md) | Capacidades, regras, MVP |
| [product/product-backlog.md](product/product-backlog.md) | Fila eleita e status |
| [product/checklist.md](product/checklist.md) | O que está cravado |
| [product/cenario.md](product/cenario.md) · [folha-01](product/folha-01-cad-atendente.md) | Sprint 1 — atendente e T1 |
| [product/evento-01-pabx-nao-homologado.md](product/evento-01-pabx-nao-homologado.md) | PABX mudo |
| [product/cenario-02-sala-despacho.md](product/cenario-02-sala-despacho.md) · [folha-02](product/folha-02-sala-despacho.md) | Sprint 2 — mesa e T2 |

Índice: [product/README.md](product/README.md).

## Arquitetura

| Doc | O que responde |
|---|---|
| [architecture/quality-attributes.md](architecture/quality-attributes.md) | T1, T2, integração, LGPD, disponibilidade |
| [architecture/component-map.md](architecture/component-map.md) | Módulos no mesmo JAR |
| [architecture/estrutura-hexagonal.md](architecture/estrutura-hexagonal.md) | Inside / outside, Maven |
| [architecture/nomenclatura.md](architecture/nomenclatura.md) | Nomes de classe, porta, tela |
| [architecture/contexto-tecnico.md](architecture/contexto-tecnico.md) | Java, Boot, Angular |
| [architecture/modulo-sugerir-empenhar.md](architecture/modulo-sugerir-empenhar.md) | Fatia Livre + T2 |
| [architecture/modulo-fila-sem-livre.md](architecture/modulo-fila-sem-livre.md) | Fila + puxar outro bairro |
| [architecture/modulo-no-local.md](architecture/modulo-no-local.md) | No local fecha o T2 |
| [architecture/risk-register.md](architecture/risk-register.md) | Riscos |
| [architecture/adr/](architecture/adr/README.md) | ADR 0001–0004 |

Índice: [architecture/README.md](architecture/README.md).
