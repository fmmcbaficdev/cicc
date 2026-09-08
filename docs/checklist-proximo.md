# Checklist para continuar

**Goal da Sprint 2:** o despachador empilha a Livre mais próxima e a sala vê o T2.

Fonte: [product-backlog](../../docs/product/product-backlog.md) · [folha 2](../../docs/product/folha-02-sala-despacho.md) · [módulo](../../docs/architecture/modulo-sugerir-empenhar.md)

## Já feito (Sprint 1 + item 5)

- [x] Abrir 190/193 + T1 + mapa + encaminhar à mesa CBA/VG/RDO
- [x] Tela `/sala` lista cartões
- [x] AVL **mock** + sugerir Livre mais próximas (haversine, até 3)
- [x] Despachador confirma empenho — T2 no clique; viatura sai de Livre

## Agora (se sobrar / Sprint 2 restante)

- [ ] Sem Livre perto: fila por gravidade **e** puxar outro bairro (item 6)
- [ ] “No local” manual fecha o T2 (item 7)

## Não fazer agora

| Item | Por quê |
|---|---|
| JPA / Oracle Spatial | Schema + licença; memória basta |
| Adapter PABX **real** | Sem homologação |
| AVL ao vivo / Hungarian / OSRM | Evento da folha 2: posição informada |
| Isolamento CBA / VG / RDO | Hipótese — 2ª unidade |
| LPR, painel, Cabine Lilás, facial/CPF | Fora desta fila |

## Como validar

```powershell
mvn -pl :cicc-cad-domain,:cicc-cad-application,:cicc-cad-infrastructure test
mvn -pl :cicc-cad-infrastructure -am install -DskipTests
mvn -f backend/infrastructure/pom.xml spring-boot:run
```

1. Abrir atendimento e encaminhar à mesa CBA
2. Em `http://localhost:4200/sala` — sugeridas `PM-CBA-01` primeiro
3. Empenhar — T2 aparece; T1 não muda

Porta 8080 ocupada: encerre o Java antigo. Depois de mudar `domain`/`application`, rode o `install` antes do `spring-boot:run`. O `ng serve` precisa ter sido iniciado **depois** de `proxy.conf.json` com `/mesa`.
