# ADR 0001 — Monólito modular em camadas (não microsserviços)

- Status: aceita (premissa de “não cria CAD” **corrigida** pelo [ADR 0002](0002-cad-proprio-pabx.md))
- Data: 2026-09-07
- Decisores: PO (recorte e *ilities*); kit deste repo (estilo)

## Contexto

O piloto do CIOSP/CICC-MT **cria o CAD do atendente** e integra o **PABX** a esse CAD (ADR 0002). Roda em Cuiabá, Várzea Grande e Rondonópolis. AVL, rádio, CIOSP Móvel e LPR continuam satélites.

*Ilities* em `quality-attributes.md`: T1 (190→despacho), T2 (despacho→local), integração com legado, trilha/LGPD, disponibilidade da sala se um satélite cair.

Restrições: time pequeno; um fluxo síncrono na sala (atendente não escolhe viatura); satélites (LPR, móvel, PABX automático) podem falhar sem parar o rádio+CAD; sem driver de carga para shard, Kafka ou 99,99%.

O acervo em `CICC/` oscila entre 6–8 BCs, Java 25, Oracle 26ai, Kafka e K8s. Nada disso é decisão deste ADR. Persistência do **nosso** artefato (Oracle vs PostgreSQL) fica para ADR próprio, se o CAD não for o único banco.

## Opções consideradas

1. **Microsserviço por bounded context** (atendimento, ocorrência, despacho, geo, LPR, IAM, cada um com deploy e banco)
   - Prós: isolamento de falha e escala teórica por pedaço; times independentes se existissem.
   - Contras: T1/T2 somam latência de rede no caminho crítico; despacho+ocorrência+AVL pedem consistência imediata; squad pequeno vira operação distribuída; BD compartilhado (CAD) já nega “um banco por serviço”. Vira monólito distribuído.

2. **Service-based** (poucos JARs gordos, um BD nosso, serviços não se chamam)
   - Prós: degrau se LPR ou AVL precisarem de ciclo de deploy diferente; ainda dá ACID no núcleo.
   - Contras: hoje o valor é **um** CAD de sala + satélites. Três deploys sem times separados só aumenta pipeline.

3. **Event-driven (Kafka/broker) como estilo do sistema**
   - Prós: alerta LPR é naturalmente “algo aconteceu”; desacopla câmera da sala.
   - Contras: T1 e o despacho são síncronos e ordenados; consistência eventual no cartão da ocorrência viola o relógio da sala. Evento no *adapter* LPR não exige o sistema inteiro em broker. Kafka-por-hype está fora das *ilities*.

4. **Monólito modular em camadas** (um artefato; módulos por fluxo; adaptadores para legado)
   - Prós: T1/T2 sem hop de rede; uma transação no “despachar”; ACL única para os 3 CAD; LPR/AVL/móvel atrás de porta com timeout — a sala despacha sem eles; um pipeline, um time.
   - Contras: deploy acoplado; um processo é SPOF (mitiga-se com duas instâncias atrás de load balancer, não com 8 serviços); disciplina de módulo é regra, não runtime.

## Decisão

**Opção 4 — monólito modular em camadas.**

Partição por **fluxo operacional** (cad/atendimento, sala/despacho, AVL, LPR, PABX), não por tabela e não por “BC” dos PDFs. Camadas fechadas: interface → aplicação → domínio; infraestrutura só nos adaptadores (PABX, AVL, LPR, móvel).

Trade-off aceito: **liberar um módulo redesenha o artefato inteiro**; em troca T1/T2 e a ocorrência ficam no mesmo processo. Extração service-based só se um adaptador (LPR, AVL ou PABX) ganhar ciclo de vida e falha que o processo único não aguente.

Não é SOA/ESB. O PABX não entra num barramento corporativo; entra numa porta.

## Consequências

- Positivas: caminho crítico da sala é chamada local; satélites com circuit breaker não derrubam T1; um lugar para carimbar T1/T2; mapa de componentes cabe num time.
- Negativas / dívida: módulo mal isolado vira bola de lama; o CAD *nosso* é SPOF de dado (ADR 0002); escala horizontal é réplica do mesmo JAR.
- *Ilities* afetadas: favorece 1, 2, 3 e 5; 4 (auditoria) fica módulo interno, não event store.
- Specs a atualizar: `component-map.md`, `quality-attributes.md` (histórico), `risk-register.md`. Sem `event-contracts.md` — LPR é adapter inbound, não estilo EDA.

## Conformidade (opcional)

ArchUnit (quando houver código): domínio sem Spring/JPA; `sala` não importa cliente HTTP do LPR — só porta; nenhum pacote `*Manager` espelhando tabela do CAD.
