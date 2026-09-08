# Cenário 2 — Mesa do despachador (BC `sala`)

**Órgão / área:** SESP-MT · CIOSP / CICC-MT  
**Produto:** CAD nosso — recorte **despacho**  
**Depende de:** [Cenário 1 — CAD do atendente](cenario.md) (ocorrência + T1 já existem)

Didática. Não substitui [jornada](jornada.md) nem o [component-map](../architecture/component-map.md). Não é Product Backlog.

## Contexto

A Secretaria de Estado de Segurança Pública de Mato Grosso opera o CIOSP / CICC nas unidades de Cuiabá, Várzea Grande e Rondonópolis. Depois da primeira versão do CAD, o atendente já registra o 190/193 e a sala vê quando o atendimento começou. O cartão precisa chegar ao despachador da região, que empilha a viatura e acompanha o deslocamento até o local.

Atualmente, o cartão chega à mesa e para. O despachador volta ao mapa antigo ou ao rádio solto, sem um fluxo no CAD nosso. Em dias de maior movimento, a fila de ocorrências cresce, não está claro qual Livre está mais perto, e o cidadão continua sem saber quanto tempo levará até a polícia sair.

A Secretaria decidiu, neste recorte, dar ao despachador uma mesa no mesmo CAD: ver o cartão da sua região, confirmar a viatura Livre mais próxima e marcar o início do deslocamento — sem o atendente escolher viatura e sem trocar o rádio digital.

## Objetivo do projeto

Disponibilizar uma primeira versão da mesa de despacho que permita ao despachador empenhar a viatura Livre mais próxima de forma simples e à operação acompanhar o início do deslocamento antes de a polícia comparecer ao local da ocorrência.

## Público envolvido

- Despachador de área (usuário primário deste cenário)
- Atendente (já entregou o cartão no Cenário 1; não despacha)
- Guarnição (só “foi empenhada” / rádio; “No local” pode ser fatia desta Sprint ou da próxima)
- Supervisor (fila parada)
- Coordenação / PO

## Necessidades identificadas

Possibilidades. O grupo corta.

- Mesa do despachador: cartões da sua região (CBA / VG / RDO), ordenados por gravidade e espera
- Sugerir viaturas **Livre** mais próximas do ponto (Oracle Spatial / AVL); atendente fora dessa tela
- Despachador **confirma** empenho (não o sistema empilha sozinho sem humano)
- Carimbar início do T2 no empenho
- Sem Livre perto: fila Crítica → Alta → Média → Baixa **e** puxar de outro bairro por proximidade
- Status da viatura: Livre → Empenhada (rascunho; Fora de serviço fora do matching)
- AVL mock se o inventário das 3 cidades não estiver fechado
- Registrar “No local” (fecha T2) — tablet ou carimbo **manual** do despachador
- Marcar T2 como manual quando não veio do móvel
- Ficha no CIOSP Móvel (alerta + rota)
- Fallback rádio + atualização manual do CAD
- Cabine Lilás em paralelo (não atrasar esta mesa)
- Mapa operacional com overlay WMS do `cicc-geo` (camada, não SoT do ponto)
- Painel gerencial T1/T2 para a coordenação
- Alerta LPR na mesma tela da mesa

## Restrições

- Sprint fictícia: **duas semanas**
- A equipe **não** entrega tudo
- Recorte: contexto **`sala`**; AVL e móvel podem ser **mock** / rádio
- Ocorrência continua dona no `cad` — a sala **referencia** o cartão, não cria outro SoT
- Matching por proximidade; **proibido** “Despachar = escolher viatura na lista” como regra (isso é o `siosp-cicc`)
- Sem microsserviço `ms-despacho` + Kafka: mesmo JAR (ADR 0001)
- Valor esperado: a viatura **sai empenhada** e o T2 **é visível**, mesmo sem tablet e sem AVL real

## Missão do grupo

1. Definir Product Owner, Scrum Master e Developers
2. Montar Product Backlog com 5 a 8 entregas
3. Ordenar por prioridade
4. Definir um Sprint Goal (uma frase de valor)
5. Escolher o Sprint Backlog
6. Preparar Sprint Review (até 3 minutos)
7. Fazer mini retrospectiva

## Perguntas para decidir

- Qual problema precisa ser resolvido primeiro?
- O que é indispensável para o **despachador** usar o serviço?
- O que pode ficar para a próxima Sprint (`acionamento`, `alerta-lpr`, encerramento)?
- Como saberemos se a primeira entrega gerou valor? (T2 começou? viatura empenhada?)
- Quem precisa validar? (despachador da sala, não só o atendente)

## Atenção

Não existe resposta única. Justifique por valor, necessidade, viabilidade e objetivo da Sprint.

Sugestão de corte (facilitador, **não** gabarito): LPR, WMS `cicc-geo`, painel e Cabine Lilás perdem para “mesa + empenho + T2”. CIOSP Móvel perde para rádio se o Goal for só a sala. “No local” cabe se o Goal incluir fechar o T2; senão vai ao Cenário 3.

Carta de evento que combina: **restrição técnica** (AVL mudo nas 3 cidades) ou **feedback de teste** (despachador recusa matching automático sem confirmar).

Folha preenchida: [folha-02-sala-despacho.md](folha-02-sala-despacho.md). Evento desta rodada: AVL mudo (restrição técnica).
