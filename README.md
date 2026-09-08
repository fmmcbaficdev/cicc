# cicc-cad

CAD do atendente CIOSP/CICC-MT. Monólito hexagonal (ADR 0001–0004).

```text
backend/        Maven: domain → application → infrastructure (JAR Boot)
frontend/       Angular 21 (não é módulo Maven)
```

- Java **26** no pin do produto (`docs/architecture/contexto-tecnico.md`). O POM de `backend/` compila com **25** até o JDK 26 estar no PATH (`java.version`).
- Spring Boot **4.1.x**
- Pacote: `br.gov.mt.sesp.cicc`

```bash
mvn -pl :cicc-cad-domain,:cicc-cad-application test
cd frontend && npm install && npx ng serve
```

`npm install` no frontend falhou neste ambiente (erro `edgesOut` do npm). A árvore Angular 21 está criada; rode o install na sua máquina.

Domínio `cad`: `Gravidade`, `Protocolo`, `Ponto`, agregado `Ocorrencia` com T1 no `newOcorrencia`. Próximo: porta + `AbrirOcorrenciaUseCase`.
