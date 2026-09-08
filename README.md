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
mvn -pl :cicc-cad-domain,:cicc-cad-application,:cicc-cad-infrastructure test
mvn -pl :cicc-cad-infrastructure -am install -DskipTests
mvn -f backend/infrastructure/pom.xml spring-boot:run
```

```bash
curl.exe -s -D - http://localhost:8080/ocorrencias -H "Content-Type: application/json" --data-binary "@docs/exemplo-abrir-ocorrencia.json"
```

Esperado: HTTP 201, JSON com `id`, `protocolo` e `inicioAtendimento` (T1). Se o JSON tiver `pabxUid` do mock (`pabx-mock-190-cba-001`), a resposta também traz o telefone. `GET /pabx/chamada` devolve a ligação da mesa. PABX mudo: `cicc.pabx.mock-mudo=true`. Depois abra no browser `http://localhost:8080/ocorrencias/{id}` (o `Location` da resposta). `http://localhost:8080/` só descreve a API — abrir ocorrência é **POST**, não GET. Persistência desta fatia é **memória** (some ao desligar). Sem Oracle ainda.

Frontend (Goal visível na sala):

```bash
cd frontend
npm install --legacy-peer-deps
npm start
```

Abra `http://localhost:4200/`. Atendente abre o T1; **Mesa** confirma a Livre e começa o T2. Proxy: `/ocorrencias`, `/pabx`, `/mesa`.

Domínio `cad` + `sala` + portas `pabx`/`avl` (mock). JPA/Oracle Spatial e AVL real depois.

Como se constrói: [docs/](docs/) — [componentes](docs/fluxo-componentes.md) · [código](docs/fluxo-desenvolvimento-codigo.md).
