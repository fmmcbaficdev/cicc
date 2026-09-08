# Levantamento funcional

Inventário macro do universo conhecido. Isto **não** é Product Backlog.

Decisão (PO, 2026-09-07, ADR 0002): **criamos o CAD do atendente**; integramos o **PABX** a ele. Piloto em Cuiabá, Várzea Grande e Rondonópolis. **Q1 = T1+T2. Q2-B = LPR.** Facial/CPF federado fora.

## Domínios


| ID  | Domínio / macrocapacidade  | Objetivo                                                                    | Principais atores             | Processos relacionados                    | Estruturante?         | Observações                                                           |
| --- | -------------------------- | --------------------------------------------------------------------------- | ----------------------------- | ----------------------------------------- | --------------------- | --------------------------------------------------------------------- |
| D1  | Atendimento / telefonia    | Receber ligação, fila, PA, correlacionar chamada                            | PA, supervisor, PABX          | Entrada 190/193/…                         | Sim                   | BC0+BC6 nos docs v2.2; às vezes fundidos                              |
| D2  | Ocorrência / CAD nosso     | Registrar chamado no CAD que vamos construir; ciclo de vida                 | Atendente, despachador        | Abertura, alteração, fechamento           | Sim                   | SoT da ocorrência (ADR 0002). PABX alimenta, não dono                 |
| D3  | Despacho                   | Despachador de área escolhe com apoio do AVL; atendente não escolhe viatura | Despachador, guarnição        | Recomendação por proximidade, acionamento | Sim                   | “Científico” = AVL + mapa, não Hungarian                              |
| D4  | Geo / SIOSP-GEO / AVL      | Mapa da RM, ponto do chamado, posição/velocidade/status da viatura          | Atendente, despachador        | Geocode ao digitar, AVL                   | Sim                   | SIOSP-GEO é a plataforma nomeada pelo PO                              |
| D5  | Recurso operacional        | Viaturas, efetivo, status, rádio                                            | Admin, despachante, guarnição | Disponibilidade, check-in                 | Sim                   | Sem isso D3 não despacha                                              |
| D6  | Administração / IAM        | Quem opera o quê, por força/unidade                                         | Admin, Keycloak               | Login, papel, auditoria                   | Sim                   | Isolamento multi-força                                                |
| D7  | Monitoramento              | Câmeras, drones, rádio na mesma sala                                        | Operador de vídeo, gestor     | Feed ao vivo, acionamento                 | Não (fase 1 estreita) | PRD v2.1 coloca no MVP; este kit tira do recorte inicial              |
| D8  | Inteligência / consulta    | LPR/OCR (alerta de placa); dossiê CPF/RG e facial fora deste recorte        | Operador CICC                 | Alerta câmera → atenção do despacho       | Não                   | 2-B: API de câmeras no escopo imediato; não bloqueia abrir ocorrência |
| D9  | Painéis / governança       | KPI, mapa de calor, auditoria gerencial                                     | Comandante, gestão            | SLA, relatórios                           | Não                   | Depois de haver evento operacional confiável                          |




## Capacidades


| ID  | Domínio | Macrofuncionalidade | Capacidade                                  | Descrição / objetivo                          | Ator(es)               | Prioridade | Complexidade | Estruturante? | MVP / release                    | Fonte                      | Status           | Observações                                       |
| --- | ------- | ------------------- | ------------------------------------------- | --------------------------------------------- | ---------------------- | ---------- | ------------ | ------------- | -------------------------------- | -------------------------- | ---------------- | ------------------------------------------------- |
| C1  | D1      | Entrada             | Receber chamada no PA                       | Ligação chega e vincula a um operador         | PA                     | Essencial  | Média        | Sim           | MVP                              | Fluxo telefonia            | Em levantamento  | Hoje PABX isolado                                 |
| C2  | D1      | Correlação          | Ligar chamada PABX ↔ ocorrência do CAD nosso | Evitar retrabalho de digitação                | PA, sistema            | Essencial  | Alta         | Sim           | MVP                              | PO ADR 0002                | Em levantamento  | Se PABX cair, C3 à mão                            |
| C3  | D2      | Registro            | Abrir ocorrência **no CAD nosso**           | Cartão + gravidade + ponto                    | Atendente              | Essencial  | Média        | Sim           | MVP                              | PO 2026-09-07              | Validado         | Núcleo do produto. Unificar Baixo/Médio/Alto vs P1–P4 |
| C4  | D2      | Ciclo               | Atualizar / fechar ocorrência               | Estados do incidente                          | PA, despachante        | Essencial  | Média        | Sim           | MVP                              | PRD v2.1 F9                | Em levantamento  |                                                   |
| C5  | D4      | Local               | Geocodificar ao digitar                     | Mapa aponta o ponto enquanto o atendente fala | Atendente              | Essencial  | Alta         | Sim           | MVP                              | PO / SIOSP-GEO             | Em levantamento  | Já descrito como As-Is                            |
| C6  | D5      | Frota               | Manter status da viatura                    | Livre / deslocamento / no local               | Despachador, guarnição | Essencial  | Média        | Sim           | MVP                              | PO passo 5–7               | Em levantamento  |                                                   |
| C7  | D4      | AVL                 | Ver viatura livre mais próxima              | Posição, velocidade, rota, status             | Despachador            | Essencial  | Alta         | Sim           | MVP (viaturas com AVL nos 3 CAD) | PO; SEFAZ AVL              | Em levantamento  | Inventariar cobertura CBA/VG/RDO                  |
| C8  | D3      | Alocação            | Despachar (despachador de área)             | Atendente **não** escolhe viatura             | Despachador            | Essencial  | Média        | Sim           | MVP                              | PO passo 4                 | Validado (regra) | Sugestão AVL; override humano                     |
| C9  | D3      | Contexto            | Acionamento duplo                           | Rádio (crítico) + ficha no CIOSP Móvel        | Guarnição              | Essencial  | Média        | Não           | MVP                              | PO passo 5; manual PM-MT   | Em levantamento  | Sem tablet = só rádio                             |
| C16 | D8      | Consulta campo      | Consultar mandado/placa/identidade no móvel | Sem sobrecarregar o rádio                     | Guarnição              | Importante | Alta         | Não           | A avaliar                        | Manual CIOSP Móvel         | Em levantamento  | Já existe app; não é CAD novo                     |
| C17 | D8      | Alerta LPR          | Alerta de placa com queixa de roubo         | Tela do operador CICC                         | Operador CICC          | Essencial  | Alta         | Não           | MVP                              | PO 2-B                     | Em levantamento  | API do cercamento; sem facial                     |
| C10 | D6      | Acesso              | Autenticar e isolar por força               | PM não vê sigilo da PC                        | Todos                  | Essencial  | Alta         | Sim           | MVP mínimo (papel + órgão)       | PRD v2.1 F11               | Em levantamento  | Keycloak vs “login simples” a decidir             |
| C11 | D6      | Auditoria           | Registrar quem fez o quê                    | Trilha operacional                            | Sistema                | Importante | Média        | Não           | MVP (log); cadeia hash depois    | RN10 / CIOSP RF-10         | Em levantamento  | Event Sourcing ≠ requisito ainda                  |
| C12 | D3      | SLA                 | Medir HAT / TDCL / TAt                      | Tempo atendimento e deslocamento              | Gestão                 | Importante | Alta         | Não           | Próxima release                  | FRD-005; PRD Daniel        | Em levantamento  | Geofence A/B sem TRD                              |
| C13 | D7      | Vídeo               | Abrir câmera no contexto                    | Consciência situacional                       | Operador vídeo         | Evolutivo  | Alta         | Não           | Futuro                           | PRD v2.1 F5                | Revisar          | Fora do recorte deste kit                         |
| C14 | D8      | Consulta            | Pesquisar CPF/RG (federada)                 | Dossiê na sala                                | Despachante            | Evolutivo  | Alta         | Não           | Futuro                           | PRD Daniel; 2-B não inclui | Não iniciado     | Fora do MVP. Placa no campo = C16/CIOSP Móvel     |
| C15 | D9      | KPI                 | Painel de fila e tempos                     | Ver gargalo da sala                           | Supervisor             | Importante | Baixa        | Não           | Próxima release                  | F8                         | Em levantamento  | Só depois do fluxo C1–C8                          |




## Regras e exceções


| ID  | Domínio | Capacidade | Tipo      | Descrição                                                 | Fonte                             | Impacto | Dúvida pendente? | Observações                                        |
| --- | ------- | ---------- | --------- | --------------------------------------------------------- | --------------------------------- | ------- | ---------------- | -------------------------------------------------- |
| R1  | D2      | C3         | Regra     | Gravidade Baixa / Média / Alta / Crítica; Crítica primeiro na fila | Fluxo PO | Alto    | Não              | |
| R2  | D3      | C8         | Regra     | Viatura não “recusa” como Uber — hierarquia               | CIOSP RF-03                       | Alto    | Sim              | Confirmar com operação                             |
| R3  | D1      | C1         | Exceção   | Trote / abandono / transferência de PA                    | FRD-002 RN-T; fluxo               | Alto    | Sim              | Prefixos de RN fragmentados                        |
| R4  | D6      | C10        | Restrição | Isolamento por órgão / unidade (sigilo)                   | PRD v2.1; CIOSP RF-08             | Crítico | Sim              | VPD Oracle vs app — decisão técnica                |
| R5  | D3      | C8         | Variação  | 190 e 193 no mesmo CIOSP; despachador por área            | PO                                | Alto    | Sim              | Não é “só PM”. É o que cada um dos 3 CAD já atende |
| R8  | D1      | C3         | Variação  | Violência doméstica: Cabine Lilás em paralelo ao despacho | SESP / Gazeta do Vale 2026        | Alto    | Não              | Software não pode serializar os dois fluxos        |
| R9  | D3      | C8         | Regra     | Atendente não escolhe viatura                             | PO passo 4                        | Alto    | Não              |                                                    |
| R10 | D3      | C8         | Regra     | Matching sempre pela viatura Livre **mais próxima**; bairro não é cerca | PO 2026-09-07 | Alto | Não | Spatial/AVL |
| R11 | D3      | C8         | Exceção   | Sem Livre perto: fila por gravidade **e** despachador pode puxar outro bairro | PO 2026-09-07 | Alto | Não | As duas; proximidade manda |
| R6  | D4      | C7         | Exceção   | Tablet/GPS sem rede: rádio + despachador atualiza o CAD na mão | PO 2026-09-07 | Alto    | Não              | T2 manual se não veio do móvel |
| R7  | D1      | C2         | Regra     | Correlação chamada↔ocorrência em poucos segundos          | PRD v2.1 RN15                     | Alto    | Sim              | 5 s sem contrato PABX no pacote                    |


Vocabulário de RN no acervo está **fatiado** (RN01–20, RN-T, RN-S, RN-C, RN-G, RN-A) sem matriz reversa. Não recopiar as 40+ regras aqui até existir uma lista única validada pelo PO.

## Dependências


| ID  | Item / capacidade   | Depende de                                  | Tipo       | Motivo / impacto                         | Criticidade | Responsável    | Situação   | Observações                                                                  |
| --- | ------------------- | ------------------------------------------- | ---------- | ---------------------------------------- | ----------- | -------------- | ---------- | ---------------------------------------------------------------------------- |
| P1  | C8 Despachar        | C3 ocorrência + C6 status viatura           | Funcional  | Sem incidente e recurso, não há despacho | Crítica     | Negócio        | Mapeada    | Núcleo estruturante                                                          |
| P2  | C2 Correlação       | Contrato/procedures PABX                    | Integração | Sem contrato, C2 não existe              | Crítica     | TI + telefonia | Em análise | Bloqueio potencial do “automático”                                           |
| P3  | C5 Geocode          | Base / ArcGIS                               | Integração | Endereço errado = despacho errado        | Crítica     | Geo            | Em análise |                                                                              |
| P4  | C7 AVL              | Rastreador funcionando na viatura do piloto | Externa    | Sem AVL a “sugestão” cai para voz        | Alta        | Operação       | Em análise | Inventário CBA + VG + RDO                                                    |
| P5  | C10 Isolamento      | Modelo de força/unidade/usuário             | Dados      | Multi-força sem isso vaza sigilo         | Crítica     | Admin/IAM      | Mapeada    |                                                                              |
| P6  | T2 (despacho→local) | C7 AVL + C6 status “no local”               | Dados      | Sem carimbo de chegada T2 não existe     | Crítica     | Despacho       | Mapeada    |                                                                              |
| P7  | C17 LPR             | API do cercamento/câmeras do Estado         | Integração | 2-B sem contrato não entrega alerta      | Crítica     | CICC / vídeo   | Em análise | Não atrasar M1 se a API atrasar: LPR é MVP, mas o fluxo CAD+AVL sobe sem ela |




## Integrações


| ID  | Sistema / órgão                      | Tipo         | Envia                  | Recebe                         | Quando                          | Criticidade | Responsável externo | Situação           | Observações                                                            |
| --- | ------------------------------------ | ------------ | ---------------------- | ------------------------------ | ------------------------------- | ----------- | ------------------- | ------------------ | ---------------------------------------------------------------------- |
| I1  | PABX Intelbras Simples IP            | Entrada      | —                      | uid, telefone, tronco, unidade | Chegada/encerramento da ligação | Crítica     | Telefonia CICC      | Identificada       | Procedures Oracle citadas (SIOPM)                                      |
| I2  | CAD legado (se ainda no prédio)      | —            | —                      | —                              | Convívio operacional            | Baixa       | Operação            | Identificada       | **Não é SoT.** Plano de corte/convívio à parte                         |
| I3  | SIOSP-GEO                            | Bidirecional | Chamado, status        | Mapa, ponto, AVL na tela       | Digitação e despacho            | Crítica     | Geo / CIOSP         | Identificada       | Plataforma centralizadora nomeada pelo PO                              |
| I4  | SIOSP cadastros                      | Entrada      | —                      | Municípios, serviços, unidades | Referência                      | Alta        | TI                  | Identificada       | Não confundir com “criar SIOSP”                                        |
| I5  | Rádio DMR                            | Bidirecional | Despacho/voz           | Check-in                       | Acionamento                     | Alta        | Operação            | Identificada       | Substituição do rádio está fora                                        |
| I6  | Keycloak                             | Bidirecional | Auth                   | Token / papéis                 | Login                           | Alta        | IAM                 | Identificada       | Versão e realm divergem nos docs                                       |
| I7  | INFOSEG / Detran / SINESP            | Entrada      | Consulta               | Dossiê / placa                 | Inteligência                    | Baixa       | Órgãos externos     | Identificada       | Fora do MVP (2-B não puxou CPF federado). Placa do LPR ≠ esta consulta |
| I8  | Cercamento / LPR (câmeras do Estado) | Entrada      | —                      | Alerta de placa (OCR)          | Passagem do veículo             | Crítica     | CICC / vídeo        | Em análise         | MVP (2-B). Contrato da API é dependência de prazo                      |
| I9  | CIOSP Móvel                          | Saída        | Ficha da ocorrência    | Status “no local”, consultas   | Acionamento e campo             | Alta        | PM / CIOSP          | Identificada       | App já existe; contrato de API a ver                                   |




## MVP e priorização


| ID  | Domínio / capacidade | Valor esperado                                              | Prioridade | Estruturante? | Complexidade | Dependências | Recorte de MVP                     | Horizonte       | Justificativa                              | Decisão         |
| --- | -------------------- | ----------------------------------------------------------- | ---------- | ------------- | ------------ | ------------ | ---------------------------------- | --------------- | ------------------------------------------ | --------------- |
| M1  | C1+C2+C3+C4          | PABX chega no CAD nosso; atendente fecha o cartão           | Essencial  | Sim           | Alta         | P2, I1       | 3 cidades                          | Agora           | ADR 0002                                   | MVP             |
| M2  | C5+C6+C7             | SIOSP-GEO + AVL no despacho                                 | Essencial  | Sim           | Alta         | P3, P4       | Viaturas com AVL nessas 3 unidades | Agora           | Sem isso não há “despacho científico”      | MVP             |
| M3  | C8+C9                | Despacho + acionamento a partir do CAD nosso                | Essencial  | Sim           | Alta         | P1           | Depois de M1                       | Agora           | Sem isso não há T2                         | MVP             |
| M4  | C10 mínimo           | Quem opera em qual CAD                                      | Essencial  | Sim           | Média        | P5           | 3 unidades                         | Agora           | Isolar o que cada CAD já isola             | MVP             |
| M5  | C11 + T1 + T2        | Carimbar atende, despacha, no local e exibir os dois tempos | Essencial  | Não           | Média        | M1, P6       | 3 CAD                              | Agora           | PO: precisa dos dois relógios              | MVP             |
| M6  | C12+C15              | Painel dos dois SLAs (sem meta inventada)                   | Importante | Não           | Média        | M5           | Mesmo piloto                       | Agora           | Meta em minutos vem da medição             | MVP             |
| M7  | C13                  | Câmera/drone na mesma tela                                  | Evolutivo  | Não           | Alta         | —            | Fora                               | Futuro          | Não é o CAD                                | Futuro          |
| M8  | C17                  | Alerta LPR na tela do CICC                                  | Essencial  | Não           | Alta         | P7, I8       | API câmeras Estado                 | Agora           | PO 2-B                                     | MVP             |
| M10 | C14                  | CPF/RG federado + facial                                    | Evolutivo  | Não           | Alta         | I7           | Fora                               | Futuro          | 2-B = “ao menos LPR”, não o pacote inteiro | Futuro          |
| M9  | C16                  | CIOSP Móvel no acionamento                                  | Importante | Não           | Média        | I9           | Se a API do app existir            | Agora           | Canal de dados já citado no fluxo          | MVP             |




## Riscos e dúvidas


| ID  | Tipo     | Descrição                                                                    | Impacto | Probabilidade | Próximo passo                                                  | Responsável      | Prazo | Status     | Resposta                                        |
| --- | -------- | ---------------------------------------------------------------------------- | ------- | ------------- | -------------------------------------------------------------- | ---------------- | ----- | ---------- | ----------------------------------------------- |
| Q1  | Decisão  | Dois relógios: T1 (190→despacho) e T2 (despacho→local)                       | Alto    | Baixa         | Instrumentar os 3 carimbos; não publicar meta em min sem série | PO               |       | Respondido | A+B. Meta numérica ainda se descobre no piloto  |
| Q2  | Dúvida   | 40% não despachadas vs 70% não atendidas vs ~30% atendidas                   | Alto    | Alta          | Série oficial PABX/CAD do último ano                           | Coordenador CICC |       | Aberto     |                                                 |
| Q3  | Decisão  | LPR no MVP (2-B); facial e CPF federado fora                                 | Crítico | Baixa         | Abrir contrato da API de câmeras; LGPD de placa                | PO + jurídico    |       | Respondido | Sem facial no piloto                            |
| Q4  | Dúvida   | Glossário: SIOSP-GEO vs CAD vs CIOSP vs CICC                                 | Médio   | Alta          | Um nome por sistema no contrato de integração                  | PO               |       | Aberto     |                                                 |
| Q5  | Risco    | Acervo com 200+ arquivos e 4 “fontes da verdade” (main03–07)                 | Alto    | Alta          | Este `docs/product/` é o mapa; CICC/ é arquivo                 | Time             |       | Em análise | “GO FOR DEVELOPMENT” dos executivos é prematuro |
| Q6  | Hipótese | Coesão 88% / índice 0,828 / 50% ganho de build                               | Baixo   | Alta          | Ignorar como evidência                                         | —                |       | Respondido | Métrica sem método de medição                   |
| Q7  | Decisão  | Persistência = Oracle + Oracle Spatial (não PostgreSQL/PostGIS)              | Alto    | Baixa         | Version/edition no provisionamento                             | PO               |       | Respondido | ADR 0003. Stack que o time já opera             |
| Q8  | Premissa | Cobertura AVL real nos 3 CAD (CBA, VG, RDO)                                  | Alto    | Média         | Inventário: quais viaturas entram no piloto                    | Operação         |       | Aberto     | Sem AVL o recorte vira só rádio                 |
| Q11 | Decisão  | CAD nosso + PABX; piloto em CBA/VG/RDO                                       | Alto    | Baixa         | Implementar módulo `cad` + porta `pabx`                        | PO               |       | Respondido | ADR 0002                                        |
| Q9  | Risco    | Documentos vendem Event Sourcing, 99,99%, Hungarian, K8s sem carga medida    | Alto    | Alta          | Recusar até haver driver                                       | Arquitetura      |       | Aberto     | Viola foco 3–5 *ilities*                        |
| Q10 | Dúvida   | DER vs TRD vs US (BC6 sem tabela; UUID vs TSID; 17 US vs “42+”)              | Alto    | Alta          | Uma SoT de dados depois do recorte MVP                         | Time             |       | Aberto     | Análise rígida v2.2                             |




## Checklist de suficiência

O levantamento nunca fica 100% pronto. Está **suficiente** quando dá para responder com segurança razoável:

- [x] Problema de negócio claro (fragmentação + tempo até o recurso)
- [x] Usuários e stakeholders identificados
- [ ] Stakeholders relevantes **participaram** (só nomes nos PDFs; sem ata de validação no pacote)
- [x] Processo atual e to-be: PABX → **CAD nosso** → despachador → rádio/tablet
- [x] Jornada de ponta a ponta mapeada
- [x] Grandes domínios identificados
- [x] Capacidades principais registradas
- [ ] Regras relevantes conhecidas **unificadas** (hoje fragmentadas)
- [x] Principais exceções consideradas (trote, GPS mudo, endereço ruim)
- [x] Dependências mapeadas
- [x] Integrações identificadas
- [x] Componentes estruturantes claros (D1–D6)
- [x] Prioridades iniciais discutidas (recorte + T1/T2 + LPR aceitos pelo PO)
- [x] Existe um possível recorte de MVP
- [x] Riscos e incertezas visíveis
- [ ] Visão inicial de esforço e evolução (faixa, não data fechada) — 8–9 meses nos PDFs está como compromisso; **não adotado**
- [x] Premissas das estimativas explícitas
- [x] O que ainda precisa ser descoberto está escrito (Q1–Q10)



## Histórico


| Data       | Mudança                                    | Motivo                                                            |
| ---------- | ------------------------------------------ | ----------------------------------------------------------------- |
| 2026-09-07 | Inventário a partir de `CICC/` (main03–07) | Avaliação do produto real; recorte MVP menor que o “MVP” dos PRDs |
| 2026-09-07 | Piloto nas 3 cidades; CAD **nosso** + PABX | ADR 0002; correção do “não cria CAD” |
| 2026-09-07 | Q1 = T1+T2; Q2-B = LPR no MVP              | Facial/CPF fora; *ilities* preenchidas                            |


