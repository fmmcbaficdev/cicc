# Contexto técnico — CAD CIOSP/CICC-MT

Fonte de verdade da **stack**. Estilo e dono da ocorrência: ADRs 0001–0003. Hexágono compilado: [ADR 0004](adr/0004-maven-spring-angular-hexagonal.md). Pastas e nomes: [estrutura-hexagonal.md](estrutura-hexagonal.md), [nomenclatura.md](nomenclatura.md).

## Backend

| Item | Valor |
|---|---|
| Linguagem | Java 26 (pin). POM de `cicc-cad/backend/` em **25** até o JDK 26 estar instalado |
| Build | **Maven** (não Gradle) |
| Framework na borda | Spring Boot **4.1.x** (não 5 — essa linha ainda não existe; 4.1 é a que declara suporte a Java 26) |
| Persistência | Oracle + Oracle Spatial (ADR 0003); JPA só em `infrastructure` |
| Artefato | um JAR Boot (`infrastructure`) |
| Pacote-base | `br.gov.mt.sesp.cicc` |
| Pasta na raiz | `cicc-cad/` (`backend/` + `frontend/`) |

## Frontend

| Item | Valor |
|---|---|
| Framework | Angular 21 |
| Pasta | `cicc-cad/frontend/` |
| Organização | por feature do [component-map](component-map.md) (`cad`, `sala`, …), não por `services/` global |
| Autoridade de invariante | backend |

## O que este contexto **não** é

- Não é o `kit-hexagonal` (Gradle / Full Cycle / GraphQL).
- Não é o `kit-projetos` (Nest / Next / Prisma).
- Não é o `siosp-cicc` (3 microsserviços + Kafka como estilo).
- Outbox/Kafka só com ADR novo e driver (LPR pode ser adapter inbound sem EDA de sistema).
- Não copiar stack dos PDFs em `CICC/` (Java 25 / Boot 3.x / Angular 19) nem a do `siosp-cicc` (Java 21 / Boot 3.4 / Angular 17).

## Histórico de versão

| Data | Stack |
|---|---|
| 2026-09-07 | Primeiro pin: Java 21, Boot 3.4, Angular 17 (espelho do `siosp-cicc`) |
| 2026-09-07 | Pin do time: Java 26, Angular 21; Boot **4.1.x** (pedido era “Boot 5”; inexistente nesta data) |
| 2026-09-07 | `cicc-cad/` criado; POM compila em Java **25** (JDK 26 ausente nesta máquina) |
| 2026-09-07 | Maven do hexágono em `cicc-cad/backend/`; Angular permanece em `frontend/` |
