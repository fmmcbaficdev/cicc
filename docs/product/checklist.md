# Checklist — CAD CIOSP/CICC-MT

O que está **cravado** (decisão) e o que já **roda no mock**. Fila e status: [product-backlog](product-backlog.md). Próximo recorte: [checklist-proximo](../checklist-proximo.md). Zip SIOSP: [fontes-siosp-2026-09](fontes-siosp-2026-09.md) — citar FRD/DER; **não** importar PRD nem 8 BCs.

## 1. Recorte (PO)

- [x] Criar **CAD nosso** (não reusar o CAD velho como SoT) — ADR 0002
- [x] Integrar o **PABX** a esse CAD (ligação → chamado)
- [x] Piloto em **Cuiabá, Várzea Grande e Rondonópolis**
- [x] Dois relógios: **T1** (190 → despacho) e **T2** (despacho → no local)
- [x] Gravidade **Baixa / Média / Alta / Crítica** — não trocar pela tríade emergência/urgência/rotina do zip
- [x] **Natureza** obrigatória na abertura (texto). Catálogo `TB_NATUREZA` depois
- [x] Atendente **não** escolhe viatura; bairro é mesa, não cerca
- [x] Sem Livre perto: **fila por gravidade** e pode puxar outro bairro (ainda por proximidade)
- [x] Fallback: PABX cai → abre à mão; tablet sem rede → rádio + CAD na mão
- [x] **LPR** no MVP do produto; **facial / CPF federado** fora — LPR **não** entra nestas sprints
- [ ] Ata/aceite formal do coordenador do CICC (hoje só conversa + docs)

## 2. Arquitetura

- [x] Estilo: monólito modular em camadas — ADR 0001
- [x] Construção: Maven + Spring Boot + Angular no hexágono — ADR 0004
- [x] Pasta `cicc-cad/` (domain → application → infrastructure + `frontend/`)
- [x] Persistência **alvo**: Oracle + Oracle Spatial — ADR 0003
- [ ] Schema Oracle + edition/licença Spatial no ambiente
- [ ] Isolamento CBA / VG / RDO (VPD vs regra na aplicação — ADR se for duradouro)

Mapa (`cad`, `pabx`, `sala`, `posicao-avl`, …): [component-map](../architecture/component-map.md). Nem todo módulo do mapa está no código.

## 3. No mock hoje (Sprint 1 + 2 + Fase 4)

Jornada no código, persistência em **memória**.

| Fase | Feito | Como ver |
|---|---|---|
| 1 Entrada | Abrir 190/193 + T1 no clique; natureza; PABX mock (telefone + ponto do celular → Onde); mapa ajustável + ponto de referência | `POST /ocorrencias` · `GET /pabx/chamada` · `GET /geocodificacao/*` · `/` |
| 2 Triagem | Gravidade + encaminhar à mesa CBA / VG / RDO | `POST /ocorrencias/{id}/encaminhar` |
| 3 Despacho | Sugerir Livre (haversine, até 3, raio 3 km) + empenho + T2 | `/sala` · `POST .../empenhar` |
| 3 Sem Livre | Fila por gravidade + puxar outro bairro | `?ampliar=true` |
| 3 No local | Manual fecha o T2 | `POST .../no-local` |
| 4 Encerrar | Caso `ENCERRADA`; viatura volta a Livre | `POST .../encerrar` |

Carimbos T1 / T2 / no local / encerrada moram no agregado `Ocorrencia`. Não há módulo `auditoria-tempos` separado.

## 4. Ainda aberto (não bloqueia o mock)

| Item | Bloqueia o mock? | Status |
|---|---|---|
| Adapter PABX **real** | Não | Hipótese — backlog 9 |
| AVL ao vivo + CIOSP Móvel | Não | Hipótese — backlog 10 |
| Isolar quem opera em CBA / VG / RDO | Não | Hipótese — backlog 8 |
| Persistir no Oracle Spatial | Sim, para gravar de verdade | Fora deste recorte |
| API LPR / câmeras | Não | Fora desta fila |
| Catálogo de natureza (`ADM.TB_NATUREZA`) | Não | Melhoria — DER v2.2 |
| Relatório de campo / estatística | Não | Fora desta fila |
| Uma lista só de RNs + glossário CAD / SIOSP-GEO / CIOSP | Não | Documento |

## 5. Recusar no caminho

Não marcar como “feito” se isto entrar sem ADR novo:

- [ ] PostgreSQL/PostGIS no lugar de Oracle Spatial
- [ ] Microsserviço por módulo / Kafka “por padrão”
- [ ] CAD legado como dono da ocorrência
- [ ] Facial ou consulta federada de CPF no piloto
- [ ] 99,99% / Event Sourcing / K8s sem driver
- [ ] Isolamento tratado como segurança (é recorte de unidade, não *ility* nova)

Specs: [visão](visao-geral.md) · [jornada](jornada.md) · [levantamento](levantamento-funcional.md) · [fontes SIOSP](fontes-siosp-2026-09.md) · [ilities](../architecture/quality-attributes.md) · [mapa](../architecture/component-map.md) · [ADRs](../architecture/adr/README.md)
