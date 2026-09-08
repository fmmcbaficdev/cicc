# ADR 0004 — Maven, Spring Boot e Angular no hexágono

- Status: aceita
- Data: 2026-09-07
- Decisores: time (stack de construção); PO (produto já cravado nos ADRs 0001–0003)
- Relacionado: [0001](0001-monolito-modular-camadas.md) (um artefato), [0003](0003-oracle-persistencia-cad.md)

## Contexto

A opção 1 do piloto é criar `cicc-cad/` neste repo. Três kits de estudo existem no workspace:

- `hexagonal-na-pratica` / `kit-hexagonal` — Gradle, Java 21 (estudo), Spring só na borda, GraphQL, outbox.
- `kit-projetos` — NestJS + Next + Prisma, specs `.specs/`.
- `siosp-cicc` — Maven/Spring/Angular, mas **3 microsserviços + Kafka**.

O time pede **Maven**, **Spring Boot** e **Angular**, com rules/skills/specs de hexagonal + Clean + DDD. As *ilities* (T1/T2, um CAD, Oracle Spatial) não pedem Gradle nem Nest.

## Opções consideradas

1. **Gradle multi-módulo como o estudo Full Cycle**
   - Prós: rules do `kit-hexagonal` colam sem tradução; `testFixtures` nativo.
   - Contras: não é o build que o time pediu; outra operação; diverge do `siosp-cicc` que já é Maven.

2. **Um único módulo Maven + só pacotes + ArchUnit**
   - Prós: um `pom`, onboarding curto.
   - Contras: a fronteira volta a ser convenção (aula 22 do estudo: qualquer classe importa qualquer outra). ArchUnit é rede de segurança, não o compilador.

3. **Três JARs deploys (atendimento / despacho / geo) como `siosp-cicc`**
   - Prós: código Java já existe noutro repo.
   - Contras: contradiz ADR 0001; T1 soma hop; Kafka vira estilo.

4. **Maven multi-módulo `domain` → `application` → `infrastructure` (Boot) + Angular em `frontend/`**
   - Prós: mesma garantia do estudo (domínio sem Spring no classpath); um JAR publicável; stack = Maven + Boot + Angular; pasta Angular fora do hexágono Java.
   - Contras: três `pom`s; test-jar no lugar de `testFixtures`; disciplina de `frontend/` à parte.

## Decisão

**Opção 4.**

Trade-off aceito: **três módulos Maven** (e test-jar) em troca de o `mvn compile` quebrar se o domínio importar Spring ou use case. Angular não entra no parent Maven. GraphQL e outbox do estudo **não** vêm no MVP. Nest/Next/Prisma ficam no `kit-projetos`.

Specs: [estrutura-hexagonal.md](../estrutura-hexagonal.md), [nomenclatura.md](../nomenclatura.md), [contexto-tecnico.md](../contexto-tecnico.md) (Java 26, Spring Boot 4.1.x, Angular 21).

## Consequências

- Positivas: harness alinhado ao que o time opera; Dependency Rule executável; um artefato (ADR 0001); tela na stack que o órgão já prototipou.
- Negativas / dívida: copiar skills do `kit-hexagonal` exige traduzir Gradle→Maven; ITs pedem classifier `tests`.
- *Ilities:* 1 e 2 (T1/T2) sem hop entre JARs; 3 (PABX/LPR) = adapter em `infrastructure`.
- Specs a atualizar: `component-map.md` (quando nascer `cicc-cad/`), `quality-attributes.md` (histórico).

## Conformidade (opcional)

`mvn -pl :cicc-cad-domain compile` sem dependência `spring-boot`. ArchUnit no `infrastructure` como cinto extra, não como substituto dos módulos.
