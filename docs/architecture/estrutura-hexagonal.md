# Estrutura hexagonal + Clean + DDD

Como o CAD nasce em disco. *Ilities* e recorte: [quality-attributes](quality-attributes.md), [component-map](component-map.md). Nomes: [nomenclatura.md](nomenclatura.md). Stack: [contexto-tecnico.md](contexto-tecnico.md).

Hexagonal e Clean descrevem o **mesmo isolamento**. A pasta grita o **domínio** (`cad`, `sala`); Maven impede o domínio de importar Spring.

## Inside / outside

```text
         REST  job  teste Angular          OUTSIDE
                    │  provided (UseCase.execute)
         domain + application               INSIDE
                    │  required (*Repository, portas PABX/AVL/LPR)
         JPA  Oracle Spatial  HTTP legado   OUTSIDE
```

- **Inside:** módulos Maven `domain` e `application`.
- **Provided:** `UseCase.execute` — controller, job e teste falam essa língua.
- **Required:** `OcorrenciaRepository`, `PabxPort`, … — intenção substituível.
- `*JpaRepository` **não** é port. É API do Spring Data.
- Fluxo de controle pode apontar para fora; **dependência** sempre aponta para dentro.

## Módulos Maven (um JAR)

```text
cicc-cad/
├── pom.xml                 # reactor (módulo backend)
├── backend/                # Maven do hexágono
│   ├── pom.xml             # parent: domain / application / infrastructure
│   ├── domain/             # Java + JUnit. Sem Spring. Sem src/main/resources
│   ├── application/        # depende de domain. Use cases. Sem @Service
│   └── infrastructure/     # Spring Boot + JPA + adapters. O JAR publicável
└── frontend/               # Angular (não é módulo Maven)
```

```text
infrastructure  →  application  →  domain
```

`domain` **não** declara `application`. Se um agregado importar use case ou Spring, o **build Maven deve falhar**.

`InMemory*`: `application/src/test/java`. Expor test-jar (`classifier` `tests`) para ITs em `infrastructure`.

## Pacotes (screaming architecture)

```text
br.gov.mt.sesp.cicc.domain.{cad,sala,pabx,avl,lpr,...}
br.gov.mt.sesp.cicc.application.{cad,sala,...}
br.gov.mt.sesp.cicc.infrastructure.{persistence,rest,pabx,avl,lpr,config}
```

Não criar `usecases/`, `controllers/`, `services/` globais no root do código. Módulo de negócio primeiro (component-map), camada depois.

Angular:

```text
frontend/src/app/<feature>/
  pages/  components/  application/  domain/  infrastructure/
frontend/src/app/shared/     # só transversal estável
```

## Ordem de construção (quando houver código)

1. `hex-criar-projeto` — ossatura Maven + Angular vazia em `cicc-cad/`.
2. `hex-value-object` → `hex-agregado` → `hex-repositorio-porta`.
3. `hex-caso-de-uso` (teste com `InMemory*`, sem Spring).
4. `hex-adapter-jpa` → `hex-adapter-rest`.
5. `hex-feature-angular` se a fatia tiver tela.

Não comece por LPR, Kafka ou GraphQL. PABX é porta + adapter (mock primeiro).

## Fontes (avaliadas, não copiadas)

| Kit | O que entrou | O que ficou de fora |
|---|---|---|
| `hexagonal-na-pratica` / `kit-hexagonal` | inside/outside, UseCase, VO/agregado, porta no domain, adapter fino | Gradle, GraphQL, outbox obrigatório, pacote `br.com.fullcycle` |
| `kit-projetos` | spec de nomenclatura, módulo = contexto, domínio leve em CRUD | Nest, Next, Prisma, sufixos `*.tsx` |
| `ddd-architecture-guide` (pessoal) | localização de regra; Angular por feature | Nest/Next scaffolding |

## Anti-padrões

- Um módulo Maven por microsserviço SIOSP (`atendimento` / `despacho` / `geo` deploys).
- PostGIS no CAD (ADR 0003). `cicc-geo` é WMS, não este repo de código.
- `@Entity` no domain; `SDO_GEOMETRY` no domain (só infra).
- Use case chamando outro use case.
- Despachador escolhendo viatura no controller — regra mora no domínio/`sala`.
