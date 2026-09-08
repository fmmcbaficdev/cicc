# Registro de riscos

Escala: 1 baixa, 2 média, 3 alta. Nível = impacto × probabilidade.

| ID | Risco | Onde (componente) | Impacto | Prob. | Nível | Tendência | Mitigação | Status |
|---|---|---|---|---|---|---|---|---|
| R01 | Nosso CAD fora = T1/T2 param | cad | 3 | 2 | 6 | → | Duas instâncias; backup; plano de convívio com o processo antigo nas 3 cidades | aberto |
| R09 | Contrato PABX incompleto / procedures fechadas | pabx | 3 | 3 | 9 | → | Porta com timeout; abertura manual do chamado; T1 no clique se a ligação não correlacionar | aberto |
| R02 | AVL incompleto nos 3 municípios | posicao-avl | 3 | 2 | 6 | → | Inventário de viaturas; sugestão só com heartbeat; T2 manual marcado | aberto |
| R03 | API LPR sem contrato / atraso | alerta-lpr | 2 | 3 | 6 | → | Circuit breaker; sala sobe sem LPR (ADR 0001) | aberto |
| R04 | Processo único derruba sala inteira | deploy do JAR | 3 | 1 | 3 | → | Duas instâncias; satélites isolados por porta; sem 8 serviços | aberto |
| R05 | Módulo vaza regra para o controller / Entity Trap | sala / cad | 2 | 2 | 4 | → | ArchUnit quando houver código; mapa por fluxo | aberto |
| R06 | Dois relógios sem carimbo confiável (T1 mistura com T2) | auditoria-tempos | 3 | 2 | 6 | → | Três instantes obrigatórios; painel mostra T1 e T2 separados | aberto |
| R07 | LGPD: alerta de placa sem trilha de quem viu | alerta-lpr / auditoria-tempos | 3 | 2 | 6 | → | Log append-only no ato da visualização | aberto |
| R08 | Acervo CICC (Kafka/K8s/8 BCs) puxar o desenho | — | 2 | 3 | 6 | → | Mapa + ADR 0001–0003 são a SoT | aberto |
| R10 | Licença e operação Oracle Spatial | cad | 2 | 1 | 2 | → | É o stack do time. Sem PostGIS. Domínio sem `SDO_GEOMETRY` | aberto |

## Legenda de tendência

- `↑` piorando · `→` estável · `↓` melhorando

## Sem sessão de risk storming ainda

Matriz inicial após ADR 0001. Revisão formal: skill `architecture-risk-review`.
