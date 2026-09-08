# ADR 0003 — Oracle e Oracle Spatial como persistência do CAD

- Status: aceita
- Data: 2026-09-07
- Decisores: PO
- Relacionado: [0001](0001-monolito-modular-camadas.md), [0002](0002-cad-proprio-pabx.md)

## Contexto

O CAD do atendente grava ocorrência, ponto no mapa, carimbos T1/T2 e trilha. Um banco, no monólito.

O banco que o time e o órgão **já operam** é **Oracle com Oracle Spatial**. PostgreSQL/PostGIS não é a plataforma de trabalho.

O PABX legado usa procedures Oracle. Ponto do chamado e proximidade de viatura são dado espacial no mesmo motor.

*Ilities:* T1/T2 síncronos; geo no despacho (endereço → ponto, AVL); trilha no mesmo lugar.

## Opções consideradas

1. **PostgreSQL + PostGIS**
   - Prós: Spatial aberto, licença menor.
   - Contras: **não é o banco em que trabalhamos.** Outra operação, outro DBA, outro Spatial. Rejeitada.

2. **Oracle (relacional) sem Spatial; mapa só no SIOSP-GEO**
   - Prós: menos feature de licença.
   - Contras: o ponto da ocorrência e a proximidade saem do SoT do CAD; T2/AVL ficam cegos no nosso banco. O PO opera Spatial.

3. **Oracle + Oracle Spatial** (schema do CAD; PABX noutro schema/instance)
   - Prós: um motor que o time já conhece; ocorrência e geometria juntas; ACID no despacho; PABX e CAD na mesma família.
   - Contras: licença Spatial; SDO_GEOMETRY no modelo (não PostGIS).

## Decisão

**Opção 3 — Oracle com Oracle Spatial.**

SoT do CAD: tabelas de ocorrência/tempos/auditoria **e** geometria (ponto do chamado; posição quando gravarmos AVL). Um schema do produto. PABX não mistura tabela de ocorrência (porta `pabx`).

Trade-off aceito: **licença e SDO_GEOMETRY** em troca de não introduzir PostGIS. SIOSP-GEO, se continuar na sala, é tela/camada — não substitui Spatial no nosso banco.

Não é “Oracle 26ai + VPD + sete schemas” dos PDFs em `CICC/`. Versão e naming (STI/SESP) no DDL.

## Consequências

- Positivas: geo e ocorrência no mesmo `@Transactional`; stack = a que o time opera; T1/T2 com ponto persistido.
- Negativas / dívida: licença Spatial; queries `SDO_*` na infra, não no domínio; isolamento multi-força (VPD vs app) ainda aberto.
- *Ilities:* 1, 2 e 4 no mesmo banco; 3 (PABX/LPR) continuam adapter.
- Specs: `component-map.md`, `quality-attributes.md`, `risk-register.md`.

## Conformidade (opcional)

Repositório e `SDO_GEOMETRY` só em `infrastructure`. Domínio fala “ponto” (lat/long ou value object), não tipo Oracle. DDL: `sesp-db-naming-standard` se o órgão exigir.
