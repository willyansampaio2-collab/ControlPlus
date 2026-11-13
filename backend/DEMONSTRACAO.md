# Demonstração do progresso diário

## Preparar e executar

Requisitos: JDK 17 e Maven no PATH, PostgreSQL em execução. Na pasta `backend`:

```powershell
# No psql ou pgAdmin, execute uma vez: CREATE DATABASE controlplus;
$env:DB_URL = 'jdbc:postgresql://localhost:5432/controlplus'
$env:DB_USER = 'postgres'
$env:DB_PASSWORD = 'sua-senha-local'
mvn test
mvn spring-boot:run
```

O Hibernate cria/atualiza as tabelas no banco local (`ddl-auto=update`). Isso facilita a apresentação; mudanças de esquema de produção exigiriam migrações versionadas.

## Roteiro de 5 minutos

1. Explique o fluxo: HTTP → Controller → DTO validado → Service → Repository → PostgreSQL.
2. Mostre `ProgressoDiario`: relação muitos-para-um com `Usuario`, chave estrangeira e restrição única `(usuario_id, data)`.
3. Execute os comandos abaixo em outro PowerShell. Eles usam IDs retornados pela API, sem assumir que o primeiro será 1.
4. Mostre o conflito de duplicidade e a validação de data futura.
5. Explique que os testes usam H2 descartável em modo PostgreSQL. Eles verificam HTTP e persistência, mas não substituem uma execução com PostgreSQL real.

```powershell
$api = 'http://localhost:8080'
$usuario = Invoke-RestMethod "$api/usuarios" -Method Post -ContentType 'application/json' -Body (@{
    nome = 'Aluno demonstracao'
    email = "demo-$([guid]::NewGuid().ToString('N'))@example.com"
    metaDias = 30
} | ConvertTo-Json)
$rota = "$api/usuarios/$($usuario.id)/progressos"
$dados = @{
    data = (Get-Date -Format 'yyyy-MM-dd')
    metaCumprida = $true
    observacoes = 'Estudei Java e Spring Boot'
}
$registro = Invoke-RestMethod $rota -Method Post -ContentType 'application/json' -Body ($dados | ConvertTo-Json)
$registro
Invoke-RestMethod $rota
Invoke-RestMethod "$rota/$($registro.id)"
Invoke-RestMethod "$rota/resumo" # Dias cumpridos, sequências e percentual da meta

$dados.metaCumprida = $false
Invoke-RestMethod "$rota/$($registro.id)" -Method Put -ContentType 'application/json' -Body ($dados | ConvertTo-Json)

# Esperado: HTTP 409, pois já existe um registro nesta data.
try {
    Invoke-RestMethod $rota -Method Post -ContentType 'application/json' -Body ($dados | ConvertTo-Json)
} catch { $_.ErrorDetails.Message }

# Esperado: HTTP 400, com erro no campo data.
$dados.data = (Get-Date).AddDays(1).ToString('yyyy-MM-dd')
try {
    Invoke-RestMethod $rota -Method Post -ContentType 'application/json' -Body ($dados | ConvertTo-Json)
} catch { $_.ErrorDetails.Message }

Invoke-RestMethod "$rota/$($registro.id)" -Method Delete
Invoke-RestMethod $rota # Lista vazia
Invoke-RestMethod "$api/usuarios/$($usuario.id)" -Method Delete
```

## Contrato REST

Todas as rotas começam com `/usuarios/{usuarioId}/progressos`.

| Método | Sufixo | Resultado |
|---|---|---|
| POST | vazio | Cria, retorna 201 e header Location |
| GET | vazio | Lista do usuário por data decrescente; vazia retorna 200 |
| GET | `/resumo` | Calcula dias cumpridos, sequências e avanço da meta; retorna 200 |
| GET | `/{id}` | Busca um registro; retorna 200 |
| PUT | `/{id}` | Substitui os dados; retorna 200 |
| DELETE | `/{id}` | Exclui; retorna 204 sem corpo |

Corpo de POST/PUT: `{"data":"2026-11-13","metaCumprida":true,"observacoes":"Estudei Java"}`. Use a data atual na demonstração. `data` e `metaCumprida` são obrigatórios. `false` é válido. Observações são opcionais, até 1000 caracteres.

Regras: um registro por usuário/dia; data entre o início do usuário e hoje. PUT pode mudar a data, respeitando as mesmas regras. A listagem de usuário inexistente retorna 404. Buscar, atualizar ou excluir um ID que pertence a outro usuário retorna 404. Excluir usuário com histórico retorna 409; exclua os progressos primeiro.

Erros retornam `ProblemDetail` com `status` e `detail`; erros de validação incluem `campos`. JSON/data/ID malformados retornam 400, recurso ausente 404 e duplicidade ou vínculo que impede exclusão 409. A restrição no banco protege inclusive contra criações simultâneas.

Os IDs na URL organizam os recursos, mas não autenticam o solicitante. A API acadêmica continua sem autenticação.

## Resumo para apresentar as regras de negócio

`GET /usuarios/{usuarioId}/progressos/resumo` retorna:

```json
{
  "usuarioId": 1,
  "totalRegistros": 3,
  "diasCumpridos": 2,
  "sequenciaAtual": 0,
  "melhorSequencia": 2,
  "metaDias": 30,
  "percentualMeta": 6.67,
  "metaAtingida": false
}
```

Esse exemplo corresponde a dois dias consecutivos cumpridos antes de uma falha registrada hoje. `totalRegistros` conta também os dias de meta não cumprida. `diasCumpridos` conta apenas `metaCumprida=true`.

Uma sequência exige dias consecutivos: lacuna ou falha a interrompe. Sem registro hoje, a sequência pode continuar a partir de ontem, pois ainda há tempo de registrar o dia atual. Uma falha registrada hoje zera a sequência atual. A melhor sequência considera todo o histórico.

O percentual corresponde a `diasCumpridos / metaDias × 100`, arredondado para duas casas e limitado a 100%. A meta é acumulada, sem exigir dias consecutivos. Usuário sem histórico recebe contadores e percentual zerados; usuário inexistente recebe 404. O resumo é calculado a cada consulta e reflete alterações e exclusões, sem salvar contadores redundantes. As datas seguem o relógio e o fuso do servidor.

## Testes

`mvn test` compila o backend e executa os testes de integração sem precisar do PostgreSQL. O perfil `test` usa H2 somente no escopo de testes, com tabelas descartáveis. O perfil normal continua usando PostgreSQL.

Cobertura: CRUD, ordenação, duplicidade em POST/PUT, datas inválidas, campos obrigatórios, limite de texto, JSON/ID inválidos, usuário ausente, isolamento entre usuários, unicidade no banco e bloqueio de exclusão de usuário com histórico.

O resumo possui testes para histórico vazio, lacunas, sequência de ontem, falha hoje, sequência antiga, percentual arredondado/limitado, separação por usuário e recálculo após exclusão.

Para conferir a persistência real, execute o roteiro com PostgreSQL, reinicie o backend antes da exclusão e consulte novamente o registro. Não confunda aprovação em H2 com validação específica do PostgreSQL.
