# Acesso — isolamento mínimo (item 8)

**Não implementar agora.** Hipótese até a 2ª unidade operar de verdade.

## Objetivo

Quando VG ou RDO entrar na operação, o cartão da mesa **CBA** não aparece na mesa **VG** (e o inverso). Isolamento é recorte de **unidade**, não *ility* de segurança e não VPD.

## Contexto

- [component-map](component-map.md) — módulo `acesso`
- [quality-attributes](quality-attributes.md) — isolamento é implícita levantada; não entra nas 3–5 prioridades
- Backlog item 8 · Folha 1 item 6
- Encerrar (7b) já fecha o mock numa mesa só (`CBA` hardcoded)

Hoje o atendente **escolhe** CBA / VG / RDO ao encaminhar. A mesa Angular fixa `CBA`. Isso não é isolamento: quem souber a URL lista a outra mesa.

## Recorte mínimo (quando a 2ª unidade entrar)

1. A mesa não é digitada na URL como dona da fila — operador traz a unidade da sessão (ainda pode ser mock de login).
2. `ocorrenciasNaMesa(MesaRegiao)` continua sendo a regra: cartão só na mesa para a qual foi encaminhado.
3. Empenhar / No local / Encerrar recusam se a ocorrência não for da mesa do operador.
4. Ampliar (“puxar outro bairro”) **não** muda de mesa: ainda é proximidade, não vazamento de fila.

Fora deste mínimo: VPD Oracle, login real, perfil multi-força, auditoria de “quem viu”.

## Não é

| Tentação | Por quê recusar |
|---|---|
| Tratar como segurança / LGPD nova | Já está nas implícitas; ADR só se a fronteira mudar |
| VPD no Oracle | Persistência ainda é memória; schema nem existe |
| Isolar matching por cerca de bairro | Bairro é mesa, não cerca — [módulo fila](modulo-fila-sem-livre.md) |

## Encerramento

Nada a marcar feito. Status no backlog: **Hipótese**. Sem Goal, sem fatia hexagonal.
