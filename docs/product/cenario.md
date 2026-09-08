# Cenário — CAD do atendente no CIOSP (piloto CBA / VG / RDO)

**Órgão / área:** SESP-MT · CIOSP / CICC-MT  
**Produto:** CAD nosso de atendimento (190/193), com PABX integrado  

Didática da oficina Scrum Simplificado. Não substitui [visão geral](visao-geral.md) nem [jornada](jornada.md). Não é Product Backlog nem ADR.

## Contexto

A Secretaria de Estado de Segurança Pública de Mato Grosso opera o CIOSP / CICC, com salas de atendimento nas unidades de Cuiabá, Várzea Grande e Rondonópolis. Nesses centros, o cidadão aciona a emergência pelos números 190 e 193. A ligação chega ao PABX; o atendente registra o fato, o local e a gravidade; o despachador aciona a viatura.

Atualmente, os atendimentos dependem de dois sistemas: a ligação vive no PABX e a ocorrência é digitada em outro cadastro. Em dias de maior movimento, forma-se fila de chamadas, a sala não enxerga com clareza quanto tempo leva até o despacho, e o cidadão não sabe quanto tempo irá esperar até a polícia chegar ao local.

A Secretaria decidiu criar um CAD próprio para o atendente e integrar o PABX a esse CAD, para organizar o registro da ocorrência emergencial e melhorar o tempo de resposta na sala.

## Objetivo do projeto

Disponibilizar uma primeira versão do CAD de atendimento emergencial que permita ao atendente registrar o fato (o quê, onde e a gravidade) de forma simples e à operação acompanhar o início do atendimento antes de a polícia comparecer ao local da ocorrência.

## Público envolvido

- Atendente / triagem (quem registra o chamado)
- Despachador de área (quem empenha a viatura)
- Guarnição (rádio e, quando houver rede, tablet)
- Supervisor da sala (vê se a fila anda)
- Coordenação do CICC / PO (valida o recorte do piloto)
- Cidadão (não usa o sistema; sente o tempo até a viatura sair)

## Necessidades identificadas

Lista de **possibilidades** (não compromisso). A equipe escolhe o que entra no Product Backlog e o que cabe nesta Sprint.

- Abrir ocorrência no CAD nosso: endereço, tipo e gravidade (Baixa / Média / Alta / Crítica)
- Ligação do PABX abrir a tela e preencher o telefone de origem
- Abrir o chamado à mão se o PABX estiver mudo (T1 no clique)
- Carimbar o início do atendimento (T1) de forma visível na sala
- Encaminhar o cartão à mesa do despachador da região (bairro é mesa, não cerca)
- Mostrar viaturas **Livre** e sugerir as **mais próximas** (atendente não escolhe)
- Despachador confirmar o empenho e começar o T2
- Registrar “No local” (tablet ou carimbo manual do despachador)
- Sem Livre perto: fila por gravidade **e** poder puxar de outro bairro, ainda por proximidade
- Enviar ficha/alerta ao CIOSP Móvel
- Fallback: rádio + despachador atualiza o CAD na mão se o tablet estiver offline
- Isolar quem opera em CBA, VG ou RDO
- Alerta de placa (LPR) na tela do CICC — **não** abre ocorrência sozinho
- Cabine Lilás em paralelo nos casos de violência doméstica (não atrasar o despacho)
- Painel de tempos T1/T2 para a coordenação
- Consulta federada de CPF/RG ou reconhecimento facial na sala

## Restrições

- Primeira Sprint fictícia: **duas semanas**
- A equipe **não** entrega tudo
- Recorte inicial: **CAD do atendente** no piloto das três cidades; PABX pode ser **mock** se o contrato (uid, telefone, tronco, unidade) não estiver fechado
- Persistência do ponto: **Oracle + Oracle Spatial** (não PostGIS; `cicc-geo` é mapa/camada)
- Não substituir o rádio digital
- Não copiar o protótipo `siosp-cicc` (despacho por clique na viatura, 3 microsserviços, Kafka) como regra desta Sprint
- Valor esperado nesta primeira versão: a sala **registra o 190/193 no CAD nosso e enxerga o T1**, mesmo se o PABX automático atrasar

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
- O que é indispensável para o usuário usar o serviço?
- O que pode ficar para a próxima Sprint?
- Como saberemos se a primeira entrega gerou valor?
- Quem precisa validar?

## Atenção

Não existe resposta única. Justifique por valor, necessidade, viabilidade e objetivo da Sprint.

Sugestão de corte (só para o facilitador, **não** é gabarito): LPR, facial/CPF, painel gerencial e Cabine Lilás costumam perder para “abrir chamado + T1”. Matching e T2 podem caber se o Goal for a sala completa; se o Goal for só o atendente, ficam para a Sprint seguinte.

Folha preenchida: [folha-01-cad-atendente.md](folha-01-cad-atendente.md). Evento: [PABX não homologado](evento-01-pabx-nao-homologado.md).

**Próximo recorte:** [Cenário 2 — mesa do despachador](cenario-02-sala-despacho.md) · [folha 2](folha-02-sala-despacho.md).
