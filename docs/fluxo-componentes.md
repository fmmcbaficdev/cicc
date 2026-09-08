# Fluxo de desenvolvimento dos componentes

Como os módulos do mapa entram no código. Não é uma lista “um componente por vez até acabar o mapa”: é **fatia vertical**. Primeiro o `cad` (ocorrência + T1), de dentro para fora do hexágono. Os outros só entram quando o backlog pedir.

Fonte do mapa: `docs/architecture/component-map.md` no kit do curso. Estilo: monólito modular, um JAR (ADR 0001–0004).

## Onde estamos (2026-09-07)

```mermaid
flowchart LR
  subgraph feito [Feito]
    A[Ossatura<br/>backend + frontend]
    B[VOs<br/>Gravidade Protocolo Ponto]
    C[Agregado<br/>Ocorrencia + T1]
  end
  subgraph agora [Feito nesta fatia]
    D[Porta<br/>OcorrenciaRepository]
    E[Use case<br/>AbrirOcorrencia]
  end
  subgraph depois [Borda — se a fatia precisar]
    F[REST / JPA]
    G[Tela Angular cad]
  end
  subgraph pabx [Item 3 — feito no mock]
    H[PabxPort + MockPabxAdapter]
  end
  A --> B --> C --> D --> E --> F --> H
  F --> G
```

Itens 1–3 + tela: `POST /ocorrencias` (T1) + `GET /pabx/chamada` + Angular `cad`. Persistência em memória até o JPA. PABX **real** não entra.

## Como nasce um componente

Cada módulo (`cad`, `pabx`, `sala`…) atravessa as mesmas camadas. A pasta grita o domínio; o Maven impede o núcleo de importar Spring.

```mermaid
flowchart TB
  subgraph inside [Inside — regra]
    VO[1. Value object]
    AG[2. Agregado]
    PT[3. Porta]
    UC[4. Use case]
    VO --> AG --> PT --> UC
  end
  subgraph outside [Outside — ambiente]
    JPA[5. Adapter JPA / mock]
    REST[6. REST fino]
    UI[7. Feature Angular]
    UC --> JPA
    UC --> REST --> UI
  end
```

Dependência Maven (sempre para dentro):

`infrastructure` → `application` → `domain`

Quem fala com o núcleo é só `UseCase.execute`. Persistência e PABX são portas (`OcorrenciaRepository`, `PabxPort`). O teste usa `InMemory*`; o JAR usa JPA ou mock.

Detalhe da esteira de arquivos: [fluxo-desenvolvimento-codigo.md](fluxo-desenvolvimento-codigo.md).

## Ordem dos componentes no produto

O mapa tem 8 módulos no **mesmo JAR**. Só se constrói o que a Sprint pede.

```mermaid
flowchart TB
  subgraph s1 [Sprint 1 — Goal: registrar 190/193 e ver o T1]
    CAD["cad — dono da ocorrência"]
    PABX["pabx — porta + mock<br/>não bloqueia o T1"]
    CAD --> PABX
  end
  subgraph s2 [Sprint 2 — se o Goal for a mesa]
    SALA["sala — matching Livre + T2"]
    AVL["posicao-avl — mock primeiro"]
    SALA --> AVL
    CAD -.->|cartão da triagem| SALA
  end
  subgraph hypotese [Depois / hipótese — sem Goal próprio ainda]
    ACESSO[acesso CBA/VG/RDO]
    MOVEL[acionamento / CIOSP Móvel]
    LPR["alerta-lpr — fora desta fila"]
  end
  s1 --> s2
  s2 -.-> hypotese
```

`auditoria-tempos` não é um serviço à parte: o T1 já mora no agregado `Ocorrencia`; T2 entra com a `sala`.

| Componente | Recorte | Estado |
|---|---|---|
| **cad** | o quê, onde, gravidade, T1 | Use case + `POST /ocorrencias` + tela Angular `cad` |
| **pabx** | mock; real só com homologação | `PabxPort` + `MockPabxAdapter`; `GET /pabx/chamada` |
| **sala** | mesa, Livre mais próxima, T2 | Sprint 2 |
| **posicao-avl** / **acionamento** | GPS e tablet; rádio é fallback | depois da Sprint 2 |
| **acesso** | isolamento de unidade | hipótese |
| **alerta-lpr** | não abre ocorrência | fora desta fila |

## Por que essa ordem

O hexágono força **invariante antes de framework**. Abrir ocorrência sem Spring prova o T1 no clique; PABX mudo (evento da oficina) não derruba o Goal. Matching e T2 não entram no `cad` — o atendente não escolhe viatura.

Sprint 1 (compromisso): itens 1–3 do backlog. Item 4 (encaminhar à mesa) só se sobrar capacidade. PABX real não entra.
