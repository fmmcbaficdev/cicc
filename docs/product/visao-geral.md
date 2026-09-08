# Visão geral do produto

Comece pelo problema e pelo contexto. Não liste telas nem histórias aqui.

| Campo | Preenchimento |
|---|---|
| Nome do produto/projeto | CIOSP + CICC-MT. O produto **é um CAD novo** de atendimento. SIOSP / CIOSP / CICC ainda convivem no vocabulário. |
| Problema ou oportunidade | O atendente precisa de um CAD nosso para registrar o chamado. Hoje a ligação vive no PABX e a ocorrência em outro sistema. O ganho é **PABX → nosso CAD** (sem redigitar), depois despacho/AVL/rádio/tablet/LPR. |
| Objetivo do produto | Entregar o CAD do atendente (ocorrência + T1) com PABX integrado; despacho e T2 no mesmo monólito. Piloto nas unidades de **Cuiabá, Várzea Grande e Rondonópolis**. |
| Principais usuários | Atendente/triagem (primário); despachador de área (primário); guarnição com rádio/tablet (primário); Cabine Lilás (variação); operador CICC (alertas); supervisor (secundário); cidadão (secundário). |
| Stakeholders-chave | Coordenador/Chefe do CICC; PO Daniel Rios; Tech Lead Jardel/Marcos; SESP/CIOSP; forças que já usam esses CAD (PM, e as demais só se o CAD delas estiver nessas três cidades). |
| Processo atual | Cidadão liga 190/193 → atendente colhe o quê/onde e **digita no SIOSP-GEO** (cartão + gravidade + ponto no mapa) → despachador da área vê o chamado e o AVL (viatura livre mais próxima) → aciona rádio **e** dados no CIOSP Móvel → equipe marca “no local”. Em violência doméstica, deriva em paralelo para Cabine Lilás sem atrasar o despacho. |
| Escopo macro conhecido | **Criamos o CAD.** Recorte geográfico do piloto: implantar esse CAD em **Cuiabá, Várzea Grande e Rondonópolis**. Integração estruturante: **PABX → CAD**. |
| Fora do escopo inicial | Demais municípios; substituição do rádio digital; facial e CPF federado. LPR/OCR entra no MVP. O CAD legado **não** é SoT deste produto (ADR 0002). |
| Premissas | PABX acessível (uid, telefone, tronco, unidade); AVL em parte da frota; rádio e CIOSP Móvel existem; mapa utilizável. |
| Restrições | Três cidades, não o estado. Segurança pública / LGPD. Persistência: **Oracle + Oracle Spatial** (ADR 0003). Não é PostgreSQL/PostGIS. |
| Fontes de conhecimento | Acervo `CICC/`; narrativa operacional do PO (2026-09-07); SESP/SEFAZ (AVL/CIOSP histórico); Primeira Hora (rádio digital MT); manual CIOSP Móvel (PM-MT); Gazeta do Vale (Cabine Lilás, ago/2026). Citações de Sergipe/Aracaju **não** valem como fato de MT. |
| Critério de sucesso | **Dois relógios, os dois oficiais.** T1 (cidadão/sala): início do atendimento (190/193) → repasse atendente→despachador → despacho. T2 (campo): despacho → “no local”. Meta numérica ainda sem série oficial — o piloto mede os dois; não escolhe um só. |

## Fontes

| Afirmação | Fonte | Confiança |
|---|---|---|
| Piloto nas 3 cidades (CBA, VG, RDO) | PO | Alta — onde roda |
| Criar o CAD do atendente; integrar o PABX a ele | PO, 2026-09-07 (correção) | Alta — ADR 0002 |
| Fluxo 190/193 → SIOSP-GEO → despachador de área → rádio + tablet | Narrativa operacional do PO | Alta como As-Is pretendido; validar na sala |
| Atendente não escolhe viatura; AVL sugere a mais próxima | PO + SEFAZ (AVL histórico 2006) | Média — AVL existe há anos; cobertura atual a inventariar |
| Cabine Lilás em paralelo (violência doméstica) | A Gazeta do Vale / SESP, ago/2026 | Alta para o processo; impacto de produto a detalhar |
| CIOSP Móvel (consulta mandado/placa/identidade na viatura) | Manual PM-MT | Alta que o app existe; profundidade da API a ver |
| Rádio digital criptografado estadual | Primeira Hora / SESP | Alta como infra existente — não substituir |
| LPR/OCR no MVP (alerta de placa na tela do CICC) | PO, Pergunta 2-B | Alta como escopo; contrato da API de câmeras a abrir |
| Canais 190 e 193 no fluxo deste recorte | PO | Alta. 197/194 existem no CIOSP histórico (SEFAZ 2006) — fora do texto do piloto |
| Dois tempos: T1 (190→despacho) e T2 (despacho→local) | PO, Pergunta 1 | Alta — decisão. Meta em minutos ainda hipótese |
| 770.648 chamadas/ano; >20 vs >60 min | Acervo CICC | Hipótese — não usar como SLA |

## Histórico

| Data | Mudança | Motivo |
|---|---|---|
| 2026-09-07 | Spec preenchida a partir do acervo `CICC/` | Avaliação do produto real; números conflitantes ficam como hipótese |
| 2026-09-07 | Recorte geográfico: CBA + VG + RDO | Onde o piloto roda |
| 2026-09-07 | CAD **nosso**; integração = PABX | Correção do PO; ADR 0002 |
| 2026-09-07 | Q1 = dois relógios (T1+T2); Q2 = LPR no MVP (2-B) | Facial/CPF fora do MVP |
