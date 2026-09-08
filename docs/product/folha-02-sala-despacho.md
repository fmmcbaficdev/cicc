# Folha de trabalho da equipe

**Equipe:** Piloto CIOSP — mesa do despachador  
**Cenário:** [Mesa do despachador (BC `sala`)](cenario-02-sala-despacho.md)  
**Data:** 2026-09-07  
**Depende de:** [folha 1](folha-01-cad-atendente.md) (ocorrência + T1 entregues; PABX automático ainda não)

Proposta didática. Não é gabarito.

## 1. Papéis

| Papel | Responsabilidade | Nome |
|---|---|---|
| Product Owner | Prioriza e representa o **despachador** da região | PO da oficina |
| Scrum Master | Facilita e protege o Goal | SM da oficina |
| Developers | Constroem a solução no mesmo JAR | Time do monólito `cicc-cad` |

## 2. Sprint Goal

O despachador empilha a viatura Livre mais próxima e a sala vê quando o deslocamento começou.

## 3. Product Backlog

| Prioridade | Entrega | Por que é importante? |
|---|---|---|
| 1 | Mesa com os cartões da região (gravidade + espera) | Sem mesa o cartão do Cenário 1 morre na triagem |
| 2 | Sugerir Livre mais próximas do ponto | Atendente não escolhe; proximidade é a regra |
| 3 | Despachador confirma o empenho e começa o T2 | Humano empilha; relógio de campo nasce aqui |
| 4 | Sem Livre perto: fila por gravidade e puxar outro bairro | Exceção já fechada pelo PO; senão a mesa trava |
| 5 | “No local” (tablet ou carimbo manual) fecha o T2 | Completa o segundo relógio oficial |
| 6 | AVL mock / timeout se o GPS falhar | Porta, não produto; rádio continua |
| 7 | Ficha no CIOSP Móvel | Melhora o campo; não é indispensável se houver rádio |
| 8 | LPR, WMS `cicc-geo`, painel, Cabine Lilás | Fora deste Goal |

## 4. Sprint Backlog

- [ ] Mesa da região (cartões do `cad`)
- [ ] Sugerir Livre mais próximas (AVL **mock**)
- [ ] Confirmar empenho e carimbar T2
- [ ] Fila por gravidade + puxar outro bairro
- [ ] “No local” manual do despachador (fecha T2)

**Justificativa:** o Goal é o **despachador** e o **T2**. Móvel e LPR esperam. AVL real fica mock — o evento abaixo testa se o Goal vive sem GPS.

## 5. Evento inesperado

**Evento:** AVL das três cidades **mudo** nesta Sprint (inventário/GPS não homologado). Sem posição ao vivo.

**Após o evento**

| Pergunta | Resposta |
|---|---|
| O Sprint Goal mudou? | **Não.** Empilhar a mais próxima *conhecida* e ver o T2 ainda vale — com lista Livre da mesa / rádio. |
| O Sprint Backlog mudou? | **Sim.** “Sugerir por GPS ao vivo” recua. Fica: mesa + empenho + T2 + fila. Matching usa posição **informada** ou ordem da mesa; despachador confirma. |
| Como ainda entregamos valor? | A viatura sai empenhada no CAD; T2 começa. O rádio cobre o campo. Proximidade perfeita espera o AVL. |

## 6. Sprint Review (até 3 minutos)

1. **Problema:** o cartão chega e para; o despachador não tem fluxo no CAD nosso.
2. **Sprint Goal:** empilhar a Livre mais próxima e ver o início do deslocamento.
3. **Backlog:** mesa, empenho, T2, fila; AVL ao vivo e móvel de fora após o evento.
4. **Evento:** AVL mudo. Tiramos GPS ao vivo; o empenho humano ficou.
5. **Justificativa:** T2 é o valor; satélite AVL não pode ser o Goal.
6. **Se a Sprint terminasse hoje:** o despachador empilha no CAD e a sala vê o T2. O policial ainda pode fechar “No local” na mão. Relatório de campo e LPR ficam para depois.

## 7. Mini retrospectiva

| | Registro (uma frase) |
|---|---|
| O que funcionou | Não transformar `sala` em microsserviço nem em “escolher viatura na lista”. |
| O que poderia ser melhor | Combinar com a operação a lista Livre sem AVL antes da Planning. |
| Principal aprendizado | Porta AVL com timeout é o que deixa o T2 nascer sem GPS. |

## Progresso

- [x] Papéis definidos
- [x] Sprint Goal definido
- [x] Product Backlog organizado
- [x] Sprint Backlog escolhido
- [x] Evento inesperado analisado
- [x] Sprint Review preparada
- [x] Retrospectiva concluída
