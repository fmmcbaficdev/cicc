# Fontes SIOSP (zip 2026-09-09) — o que entra

Avaliação do pacote `CICC - SIOSP` (Downloads). **Não** é Product Backlog. Não substitui ADRs 0001–0004.

O zip mistura três produtos: **painel unificado CICC**, **CAD Intelbras refatorado** e **admin/natureza Oracle**. O nosso recorte é só o **CAD nosso + PABX** (ADR 0002).

## Trazer (como fonte, não como código)

Copiar para `cicc-cad/docs/product/fontes/` só se for citar no levantamento. Não versionar o Java/SQL do BC5.

| Arquivo no zip | Por quê | Como usar |
|---|---|---|
| `FRD_CICC_MT_v2.1_Painel_Unificado.md` — F2, F3, RN01–RN03, RN08, RN12, RN15 | Jornada e PABX (`uid`, telefone, tronco) batem com o que já mockamos | Extrair RN; **não** copiar gravidade emergência/urgência/rotina (nós temos 4 níveis) |
| `DER_CICC_MT_Oracle_v2.2.md` — `ADM.TB_NATUREZA` / grupo / complemento | Catálogo de natureza para quando o texto livre virar lista do órgão | Consulta na fatia JPA; sem Flyway agora |
| `01_RESUMO_EXECUTIVO_PO.md` — volume (770 mil chamadas/ano) e PABX Intelbras | Contexto de carga; não vira prazo de 8–9 meses | Hipótese no levantamento, com fonte |

## Não trazer

| Arquivo | Por quê |
|---|---|
| `PRD_CICC_MT_v2_Validado.md` como dono do produto | Manda **refatorar CAD Intelbras** e painel ArcGIS/câmera/drone. ADR 0002 recusou. |
| `75_ARQUITETURA_MODULOS_BCS.md` | 8 BCs + Kafka + K8s + PostGIS + `*Service` anêmico. Choca ADR 0001/0003/0004. |
| `UserStories_CICC_MT_v2.1.md` | Spike PostGIS/Keycloak/PostgreSQL. Não é a nossa Sprint. |
| `DDD_Java25_Angular19_*.md` | Cliente Intelbras, Angular 19, pasta técnica. |
| `CICC-MOD-ADM/*` (Entities, DTOs, `pom_bc5`, SQL) | Admin e catálogo. Fora do mock. |
| Cópias em `DOCUMENTOS GESIS/CICC-DOC-INICIAIS/` | Duplicata dos de cima. |
| Kanban (`71`–`74`) e sumários ADM vs STBS | Operação de outro recorte. |
| `ddl_CODEX.txt` / `DER_CICC_MT_v2.1.md` | DER v2.2 (Oracle Spatial) já substitui o v2.1 UUID/PostGIS. |

## Nosso desenvolvimento × o zip

| Tema | Zip (v2.1/v2.2) | `cicc-cad` hoje | Veredito |
|---|---|---|---|
| Dono da ocorrência | Refatorar Intelbras / SoT legado | CAD nosso (ADR 0002) | **Manter o nosso** |
| Persistência | PRD/US: PostgreSQL+PostGIS; DER v2.2: Oracle Spatial | Memória; alvo Oracle Spatial (ADR 0003) | Alvo certo; JPA depois |
| Estilo | 6–8 BCs, Kafka, K8s | Monólito hexagonal, 1 JAR | **Manter o nosso** |
| Abertura + T1 | F2 manual + PABX cria ocorrência sozinha | T1 no clique; PABX mock ou à mão | Nosso fallback é mais honesto (evento PABX mudo) |
| Natureza | Dropdown / `TB_NATUREZA` | Texto obrigatório | Feito o mínimo; catálogo é melhoria |
| Gravidade | Emergência / urgência / rotina | Baixa / Média / Alta / Crítica | **Não trocar** sem PO |
| Despacho | Livre &lt; 5 km; operador clica na viatura | 3 km; despachador confirma sugestão | Mesma intenção; raio nosso |
| T2 / No local / Encerrar | RN12: no local + “atendimento concluído” | No local fecha T2; Encerrar devolve Livre | Feito no mock; relatório de campo não |
| PABX real | Poll 5 s, procedure Oracle (RN15) | Mock `uid/telefone/tronco/unidade` | Contrato ok; adapter real = item 9 |
| Painel / LPR / drone / Keycloak | Núcleo do PRD | Fora desta fila | Continua fora |

## Conflitos que o zip não pode sobrescrever

1. CAD Intelbras como SoT.  
2. PostGIS no lugar de Oracle Spatial.  
3. Microsserviço / Kafka / K8s sem driver.  
4. Gravidade de 3 níveis.  
5. PABX que **abre** a ocorrência sozinho (no nosso recorte ele só correlaciona).
