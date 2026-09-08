# Checklist — CAD CIOSP/CICC-MT

Marque o que já está cravado. O que está aberto **bloqueia** ou só **atrasa** o primeiro código, conforme a coluna.

## 1. Recorte (PO)

- [x] Criar **CAD nosso** para o atendente (não reusar o CAD velho como SoT) — ADR 0002
- [x] Integrar o **PABX** a esse CAD (ligação → chamado)
- [x] Piloto em **Cuiabá, Várzea Grande e Rondonópolis**
- [x] Dois relógios: **T1** (190→despacho) e **T2** (despacho→local)
- [x] **LPR** no MVP; facial e CPF federado **fora**
- [x] Fallback: PABX cai → atendente abre o chamado à mão
- [ ] Ata/aceite formal do coordenador do CICC (hoje só conversa + docs)

## 2. Arquitetura

- [x] Estilo: monólito modular em camadas — ADR 0001
- [x] Módulos: `cad`, `pabx`, `sala`, `posicao-avl`, `acionamento`, `alerta-lpr`, `auditoria-tempos`, `acesso`
- [x] Persistência: **Oracle + Oracle Spatial** (não PostgreSQL/PostGIS) — ADR 0003
- [ ] Versão/edition do Oracle (licença Spatial confirmada no ambiente)
- [ ] Isolamento multi-força: VPD vs regra na aplicação (ADR se for duradouro)
- [x] Construção: Maven + Spring Boot + Angular no hexágono — ADR 0004
- [x] Pasta do código na raiz (`cicc-cad/`) — ossatura Maven + Angular (`hex-criar-projeto`)

## 3. Integrações — precisa existir para o fatia

| Item | Bloqueia o 1º código? | Feito |
|---|---|---|
| Contrato PABX (uid, telefone, tronco, unidade, instante) | Não — dá para mockar a porta | [x] mock (`PabxPort` + `MockPabxAdapter`) |
| Schema Oracle do CAD + `SDO_GEOMETRY` do ponto | Sim, para gravar de verdade | [ ] |
| Inventário AVL nas 3 cidades | Não — despacho manual no começo | [ ] |
| API LPR / câmeras do Estado | Não — sala sobe sem alerta | [ ] |
| CIOSP Móvel (ficha) | Não — rádio é fallback | [ ] |
| Mapa: Spatial nosso vs tela SIOSP-GEO | Não — ponto no Oracle basta no 1º corte | [ ] |

## 4. Primeira fatia de implementação (ordem)

Faça nesta ordem. Não comece por LPR nem por microsserviço.

- [x] 1. Pasta `cicc-cad/` + monólito (camadas) — sem domínio de ocorrência ainda
- [x] 2. Módulo `cad`: VOs + agregado + use case + `POST /ocorrencias` (T1). Unidade CBA/VG/RDO ainda não
- [x] 3. Porta `pabx` (interface) + adapter mock; depois adapter real
- [ ] 4. Persistir no Oracle; ponto em Oracle Spatial
- [x] 5. Carimbar **início do atendimento** (T1 no `newOcorrencia`) — visível na tela Angular `cad`
- [ ] 6. `auditoria-tempos`: os três instantes (atende / despacha / no local)
- [ ] 7. `sala`: despacho (atendente **não** escolhe viatura)
- [ ] 8. `posicao-avl` (timeout se GPS mudo)
- [ ] 9. `alerta-lpr` (timeout; não abre ocorrência sozinho)
- [ ] 10. `acionamento` / CIOSP Móvel

## 5. Ainda não unificado (não bloqueia o item 4.1–4.5)

- [x] Escala de gravidade do fluxo: **Baixa / Média / Alta / Crítica**
- [x] Sem Livre perto: **fila por gravidade e** pode puxar outro bairro; **sempre as mais próximas**
- [x] Tablet offline: **rádio + despachador atualiza o CAD na mão**
- [ ] Uma lista só de regras de negócio (hoje RN01–20 + RN-T/S/C/G/A)
- [ ] Faixa de esforço (sem os “8–9 meses” dos PDFs como prazo)
- [ ] Glossário: CAD vs SIOSP-GEO vs CIOSP vs CICC

## 6. Recusar no caminho

Não marcar como “feito” se isto entrar sem ADR novo:

- [ ] PostgreSQL/PostGIS no lugar de Oracle Spatial
- [ ] Microsserviço por módulo / Kafka “por padrão”
- [ ] CAD legado como dono da ocorrência
- [ ] Facial ou consulta federada de CPF no piloto
- [ ] 99,99% / Event Sourcing / K8s sem driver

Specs: [visão](visao-geral.md) · [jornada](jornada.md) · [levantamento](levantamento-funcional.md) · [ilities](../architecture/quality-attributes.md) · [mapa](../architecture/component-map.md) · [ADRs](../architecture/adr/README.md)
