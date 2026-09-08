# Mapa de componentes

Partição por fluxo da jornada (`docs/product/jornada.md`). Estilo: ADR 0001. Dono da ocorrência: ADR 0002.

## Estilo vigente

- Pasta deste repositório: `cicc-cad/` (`backend/` Maven `domain` / `application` / `infrastructure` + `frontend/`)
- Estilo: monólito modular em camadas (o artefato **é** o CAD de atendimento)
- Construção: Maven `domain` → `application` → `infrastructure` (JAR Boot) + Angular em `cicc-cad/frontend/` — [ADR 0004](adr/0004-maven-spring-angular-hexagonal.md)
- Pacotes e nomes: [estrutura-hexagonal.md](estrutura-hexagonal.md) · [nomenclatura.md](nomenclatura.md)
- ADR: [0001](adr/0001-monolito-modular-camadas.md) (estilo) · [0002](adr/0002-cad-proprio-pabx.md) (CAD + PABX) · [0003](adr/0003-oracle-persistencia-cad.md) (Oracle Spatial) · [0004](adr/0004-maven-spring-angular-hexagonal.md) (Maven/Spring/Angular)

## Componentes

| Componente | Tipo | Responsabilidade | *Ilities* distintas? | Deploy | Dono |
|---|---|---|---|---|---|
| cad | módulo | Ocorrência do atendente: abrir, gravidade, endereço, ciclo, SoT do chamado | T1; dado nosso | mesmo JAR | — |
| pabx | módulo (adaptador) | Ligação 190/193 → vincular ao chamado; timeout se PABX cair | Integração; pode falhar | mesmo JAR | — |
| sala | módulo | Despacho: atendente não escolhe viatura; início do T2 | T1/T2 | mesmo JAR | — |
| posicao-avl | módulo (adaptador) | Viaturas livres, mais próxima, heartbeat | T2; pode falhar | mesmo JAR | — |
| acionamento | módulo | Ficha no CIOSP Móvel; rádio continua externo | T2; fallback voz | mesmo JAR | — |
| alerta-lpr | módulo (adaptador) | Alerta de placa; **não** abre ocorrência sozinho | LPR; pode falhar | mesmo JAR | — |
| auditoria-tempos | módulo | Carimbos atende / despacha / no local; quem viu LPR | T1, T2, LGPD | mesmo JAR | — |
| acesso | módulo | Quem opera em qual unidade (CBA / VG / RDO) | Isolamento | mesmo JAR | — |

`cad` é dono da ocorrência. `pabx` só entrega a ligação. AVL, LPR e móvel são portas: queda não impede abrir chamado à mão nem despachar por rádio.

Não há `OcorrenciaManager` genérico nem adaptador “para o CAD velho” como SoT.

## Integrações

| De | Para | Forma | Contrato |
|---|---|---|---|
| pabx | PABX (Intelbras Simples IP / procedures) | inbound + timeout | uid, telefone, tronco, unidade, instante |
| cad | Oracle + Oracle Spatial | local / JDBC | ocorrência, ponto (`SDO_GEOMETRY`), T1/T2, auditoria — [ADR 0003](adr/0003-oracle-persistencia-cad.md) |
| sala / cad | Oracle Spatial (SoT do ponto); SIOSP-GEO só se for camada/tela | local / sync | endereço → geometria |
| posicao-avl | AVL | sync + timeout | posição, status |
| acionamento | CIOSP Móvel | push; falha → rádio | ficha |
| alerta-lpr | câmeras / LPR do Estado | inbound + timeout | placa, câmera, instante |

## Anti-padrões a evitar neste mapa

- Um componente por entidade JPA.
- Tratar o CAD legado como dono da ocorrência deste produto (isso foi o erro do ADR 0001, corrigido no 0002).
- Microsserviço “porque PABX é outro sistema” — PABX é adaptador.
- Camada técnica como único recorte.
