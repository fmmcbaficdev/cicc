# Checklist para continuar

**Mock fechado.** Sprint 1 (T1) + Sprint 2 (T2) + Encerrar (Fase 4). Não há próximo recorte de código sem refinar uma hipótese.

Fonte: [checklist](product/checklist.md) · [product-backlog](product/product-backlog.md) · [folha 2](product/folha-02-sala-despacho.md) · [módulo T2](architecture/modulo-sugerir-empenhar.md) · [módulo fila](architecture/modulo-fila-sem-livre.md) · [módulo No local](architecture/modulo-no-local.md) · [módulo encerrar](architecture/modulo-encerrar.md)

## Já feito

- [x] Sprint 1 — abrir 190/193 + T1 + mapa + encaminhar à mesa CBA/VG/RDO (itens 1–4)
- [x] Sprint 2 — `/sala`, AVL mock, empenho + T2, fila + outro bairro, No local (itens 5–7)
- [x] Fase 4 — Encerrar devolve a viatura a Livre (item 7b)

## Agora

Nenhum item está **Pronto para Sprint**. O próximo código só entra depois de escolher **uma** hipótese e refinar:

| Item | Quando refinar |
|---|---|
| 8 Isolar CBA / VG / RDO | 2ª unidade — [nota](architecture/modulo-isolamento.md); não é *ility* de segurança |
| 9 Adapter PABX **real** | Homologação do contrato (`uid`, telefone, tronco, unidade) |
| 10 AVL real + CIOSP Móvel | Inventário nas 3 cidades; rádio já é fallback |

Até lá, não implementar.

## Não fazer agora

| Item | Por quê |
|---|---|
| JPA / Oracle Spatial | Schema + licença; memória basta no mock |
| Adapter PABX **real** | Sem homologação (item 9) |
| AVL ao vivo / Hungarian / OSRM | Evento da folha 2: posição informada |
| Isolamento CBA / VG / RDO | Hipótese — 2ª unidade (item 8) |
| LPR, painel, Cabine Lilás, facial/CPF | Fora desta fila |
| Relatório de campo / estatística | Fora do Encerrar |

## Como validar

Rode a partir de `cicc-cad/`, não da pasta do curso.

```powershell
cd C:\estudos\JAVA\arquitetura-de-sistemas\cicc-cad
mvn -pl :cicc-cad-domain,:cicc-cad-application,:cicc-cad-infrastructure test
mvn -pl :cicc-cad-infrastructure -am install -DskipTests
mvn -f backend/infrastructure/pom.xml spring-boot:run
```

1. Abrir atendimento e encaminhar à mesa CBA
2. Em `http://localhost:4200/sala` — Crítica acima de Baixa; sugeridas `PM-CBA-01` primeiro (até 3 km)
3. Empenhar as perto — aparece “Puxar outro bairro”; ampliar traz `PM-CBA-03` / `PM-VG-01`
4. Empenhar — T2 aparece; T1 não muda
5. **No local** — T2 para; rótulo manual
6. **Encerrar** — `PM-CBA-01` volta a Livre no próximo cartão

Porta 8080 ocupada: encerre o Java antigo. Depois de mudar `domain`/`application`, rode o `install` antes do `spring-boot:run`. O `ng serve` precisa ter sido iniciado **depois** de `proxy.conf.json` com `/mesa`.
