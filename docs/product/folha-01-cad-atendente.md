# Folha de trabalho da equipe

**Equipe:** Piloto CIOSP — CAD do atendente  
**Cenário:** [CAD do atendente no CIOSP (CBA / VG / RDO)](cenario.md)  
**Data:** 2026-09-07  

Proposta **didática** de uma equipe. Não é gabarito nem Product Backlog oficial.

## 1. Papéis

| Papel | Responsabilidade | Nome |
|---|---|---|
| Product Owner | Prioriza e representa o atendente da sala (e o tempo que o cidadão sente) | PO da oficina |
| Scrum Master | Facilita e mantém o foco no Goal | SM da oficina |
| Developers | Constroem a solução e decidem em conjunto | Time do monólito `cicc-cad` |

## 2. Sprint Goal

O atendente registra o 190/193 no CAD da sala e a operação vê quando o atendimento começou.

## 3. Product Backlog

5 a 8 entregas, ordenadas. Possibilidades, não promessas.

| Prioridade | Entrega | Por que é importante? |
|---|---|---|
| 1 | Abrir ocorrência no CAD (o quê, onde, gravidade) | Sem cartão não há atendimento nosso nem T1 |
| 2 | Abrir o chamado à mão se o PABX estiver mudo | A sala não pode parar se a correlação falhar |
| 3 | Carimbar o início do atendimento (T1) na tela | Valor da primeira versão: a operação enxerga o relógio |
| 4 | Ligação do PABX abrir a tela e preencher o telefone | Evita redigitar; **depende de homologação** |
| 5 | Encaminhar o cartão à mesa do despachador da região | Fecha o T1 no sentido “saiu da triagem”; alimenta o Cenário 2 |
| 6 | Isolar quem opera em CBA, VG ou RDO (mínimo) | Piloto em três unidades; sem isso o cartão vaza de mesa |
| 7 | Mesa: sugerir Livre mais próxima e empenhar (T2) | Valor do despachador — [Cenário 2](cenario-02-sala-despacho.md) |
| 8 | LPR, painel gerencial, Cabine Lilás, facial/CPF | Fora do Goal desta Sprint |

## 4. Sprint Backlog

Só o que entra **nesta** Sprint (**antes** do evento).

- [ ] Abrir ocorrência no CAD (o quê, onde, gravidade)
- [ ] Abrir o chamado à mão
- [ ] Carimbar T1 de forma visível
- [ ] PABX preenche telefone e abre a tela
- [ ] Encaminhar o cartão à mesa da região (entrega fina: “disponível para o despachador”)

**Justificativa:** o Goal é o **atendente** e o **T1**. Matching, T2, móvel e LPR não cabem em duas semanas sem perder o registro. Isolamento (item 6) e PABX automático (item 4) são desejáveis; o automático entra no compromisso porque estava no objetivo do produto — o evento testa se o Goal sobrevive sem ele.

## 5. Evento inesperado

Cartão: [evento-01-pabx-nao-homologado.md](evento-01-pabx-nao-homologado.md)

**Evento:** PABX não homologado. Sem uid, telefone, tronco nem abertura automática da tela.

**Após o evento**

| Pergunta | Resposta |
|---|---|
| O Sprint Goal mudou? | **Não.** Continua: registrar no CAD e ver quando o atendimento começou. |
| O Sprint Backlog mudou? | **Sim.** Saiu: “PABX preenche telefone e abre a tela” (volta ao Product Backlog, prioridade 4). Reforçado: abrir à mão e T1 no clique. Encaminhar à mesa **permanece** se couber; senão fica o primeiro item do Cenário 2. |
| Como ainda entregamos valor? | O atendente digita o fato no CAD nosso. O T1 começa no clique. O cidadão não ganha o telefone automático, mas a sala deixa de redigitar em outro sistema. |

Sprint Backlog **depois** do evento:

- [ ] Abrir ocorrência no CAD (o quê, onde, gravidade)
- [ ] Abrir o chamado à mão
- [ ] Carimbar T1 no clique (visível na sala)
- [ ] Encaminhar o cartão à mesa da região (se a capacidade sobrar)

## 6. Sprint Review (até 3 minutos)

1. **Problema:** ligação e ocorrência em sistemas diferentes; a sala não vê o tempo até o despacho; o cidadão não sabe quanto espera.
2. **Sprint Goal:** o atendente registra o 190/193 no CAD da sala e a operação vê quando o atendimento começou.
3. **Sprint Backlog (após evento):** abrir ocorrência; abrir à mão; carimbar T1; encaminhar à mesa se couber.
4. **Evento:** PABX sem homologação. Tiramos a correlação automática; o Goal ficou de pé.
5. **Justificativa:** valor = registro nosso + relógio da sala. Integração sem contrato não é incremento.
6. **Se a Sprint terminasse hoje:** o atendente registra o fato no CAD e a sala vê o início do atendimento. O despachador ainda não empilha viatura (Cenário 2).

## 7. Mini retrospectiva

| | Registro (uma frase) |
|---|---|
| O que funcionou | Tratar PABX como porta evitou o Goal “só existe se o PABX ligar”. |
| O que poderia ser melhor | Não comprometer correlação automática antes do contrato (uid, telefone, tronco, unidade). |
| Principal aprendizado | Fallback à mão não é plano B de marketing — é o que mantém o T1 nesta Sprint. |

## Progresso

- [x] Papéis definidos
- [x] Sprint Goal definido
- [x] Product Backlog organizado
- [x] Sprint Backlog escolhido
- [x] Evento inesperado analisado
- [x] Sprint Review preparada
- [x] Retrospectiva concluída
