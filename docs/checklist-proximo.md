# Checklist para continuar

**Goal da Sprint 1:** o atendente registra o 190/193 no CAD e a operação vê quando o atendimento começou.

Fonte: [product-backlog](../../docs/product/product-backlog.md) · [folha 1](../../docs/product/folha-01-cad-atendente.md) · [fluxo-componentes](fluxo-componentes.md)

## Já feito

- [x] Ossatura `cicc-cad/` (Maven `backend/` + Angular vazio)
- [x] Abrir ocorrência (o quê, onde, gravidade) + T1 no clique — `POST /ocorrencias`
- [x] Abrir à mão se o PABX estiver mudo
- [x] Porta `pabx` + mock — `GET /pabx/chamada` (uid, telefone, tronco, unidade)
- [x] Consultar ocorrência — `GET /ocorrencias/{id}`

## Agora (fecha o Goal na sala)

A API já carimba o T1. A operação ainda **não vê** isso numa tela.

- [x] Tela Angular `cad`: formulário de abertura + T1 visível depois do clique
- [x] Mesma tela mostra protocolo + `inicioAtendimento` (T1) sem o atendente ir no JSON
- [x] PABX mudo na UI: `GET /pabx/chamada` 404 e o formulário continua abrindo
- [x] Ponto no mapa a partir do endereço/lat-long do atendente (PABX não envia coordenada)

## Se sobrar capacidade nesta Sprint (item 4)

- [x] Encaminhar fecha a triagem (`EM_TRIAGEM` → `NA_MESA`) sem matching
- [x] Mesa do piloto: CBA / VG / RDO — `POST /ocorrencias/{id}/encaminhar`
- [x] Cartão visível na mesa: `GET /mesa/{mesa}/ocorrencias` + tela `/sala`

## Não fazer agora

| Item | Por quê |
|---|---|
| JPA / Oracle Spatial | Precisa de schema + licença Spatial; memória basta para o Goal |
| Adapter PABX **real** | Evento da oficina: sem homologação |
| Mesa + Livre mais próxima + T2 | Sprint 2 · `sala` |
| Isolamento CBA / VG / RDO | Hipótese — 2ª unidade |
| LPR, painel gerencial, Cabine Lilás, facial/CPF | Fora desta fila |

## Como validar cada passo

```powershell
mvn -pl :cicc-cad-domain,:cicc-cad-application,:cicc-cad-infrastructure test
mvn -pl :cicc-cad-infrastructure -am install -DskipTests
mvn -f backend/infrastructure/pom.xml spring-boot:run
```

1. `GET http://localhost:8080/pabx/chamada` → uid + telefone
2. `POST /ocorrencias` com `docs/exemplo-abrir-ocorrencia.json` → 201 + T1
3. `GET` do `Location` → mesmo T1
4. Depois da tela: o atendente vê o T1 no browser, sem PowerShell

Porta 8080 ocupada: encerre o Java antigo antes de subir de novo. Depois de mudar `domain`/`application`, rode o `install` antes do `spring-boot:run`.
