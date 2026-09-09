# Nomenclatura — hexagonal / Clean / DDD

Vale para `cicc-cad/`. Pastas de arquivo em **kebab-case**. Classes Java em PascalCase. Specs desta implementação em `docs/`.

## Java

| Tipo | Nome | Exemplo |
|---|---|---|
| Agregado | substantivo do domínio | `Ocorrencia`, `Chamada` |
| Id | `{Agregado}Id` — `unique()`, `with(String)` | `OcorrenciaId` |
| VO | conceito, sem `Vo` no nome | `Gravidade`, `Natureza`, `Protocolo`, `Ponto` |
| Porta de persistência | `{Agregado}Repository` | `OcorrenciaRepository` |
| Porta de legado | `{Intenção}Port` | `PabxPort`, `AvlPort`, `GeocodificacaoPort` |
| Fake | `InMemory{Agregado}Repository` | `InMemoryOcorrenciaRepository` |
| Use case | `{Verbo}{Coisa}UseCase` | `AbrirOcorrenciaUseCase` |
| I/O | records aninhados `Input` / `Output` | primitivos/String; **nunca** o agregado |
| JPA entity | `{Agregado}Entity` | `OcorrenciaEntity` |
| Spring Data | `{Agregado}JpaRepository` | **não** é port |
| Adapter JPA | `{Agregado}DatabaseRepository` | implementa a port |
| REST in | `{Intenção}Request` | `AbrirOcorrenciaRequest` |
| REST out | `{Recurso}Response` | `OcorrenciaResponse` |
| Exceção de regra | `ValidationException` | 422 |
| Não encontrado | `NotFoundException` | 404 |
| Config | `UseCaseConfig` | `@Bean` manual, sem `@Service` no use case |

Língua da port: `ocorrenciaDeId(OcorrenciaId)`, não `findById`. Unicidade (protocolo já existe) no use case; validade do VO no construtor.

Mensagens de `ValidationException` em **português**.

## Maven (artefatos)

| Módulo | `artifactId` | Depende de |
|---|---|---|
| reactor | `cicc-cad` | — |
| backend (pasta) | `cicc-cad-backend` | `cicc-cad` |
| domain | `cicc-cad-domain` | nada |
| application | `cicc-cad-application` | `cicc-cad-domain` |
| infrastructure | `cicc-cad-infrastructure` | `cicc-cad-application` + Spring Boot |

`groupId`: `br.gov.mt.sesp.cicc`.

## Angular

Pastas e arquivos em kebab-case. Sufixo = papel.

| Sufixo | Uso |
|---|---|
| `*.page.ts` | rota / composição de tela |
| `*.component.ts` | componente da feature |
| `*.facade.ts` | coordenação de UI (application) |
| `*.model.ts` | tipo da feature (não é o agregado Java) |
| `*.api.ts` | adapter HttpClient |
| `*.request.ts` / `*.response.ts` | DTO de borda |
| `*.spec.ts` | teste |

Feature = nome do componente do mapa (`cad`, `sala`). Sem `services/` global para regra de negócio.

## Proibido

- `*Manager`, `*Helper`, `OcorrenciaService` anêmico no domain.
- `New*DTO` (estudo Full Cycle) — aqui é `*Request`.
- Pacote `usecases` genérico.
- Nome de tabela (`TbOcorrencia`) como classe de domínio.
