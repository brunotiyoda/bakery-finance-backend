# Projeto Padaria

Este é um sistema de gerenciamento para padarias, desenvolvido utilizando Kotlin e o framework Ktor. O projeto segue os princípios do Domain-Driven Design (DDD) e oferece funcionalidades como autenticação, gerenciamento de receitas e despesas, e geração de relatórios.

## Estrutura do Projeto

O projeto está organizado seguindo a arquitetura DDD:

- `domain`: Contém as entidades de domínio, interfaces de repositório e serviços.
- `application`: Implementa os casos de uso da aplicação.
- `infrastructure`: Lida com detalhes técnicos como persistência de dados e configurações.
- `presentation`: Contém os endpoints da API e lida com a serialização/deserialização de dados.

## Principais Funcionalidades

- Autenticação e autorização de usuários
- Registro de receitas e despesas
- Geração de relatórios diários, mensais e por período
- Limitação de taxa de requisições
- Configuração de CORS

## Tecnologias Utilizadas

- Kotlin
- Ktor
- Exposed (ORM)
- PostgreSQL
- Koin (Injeção de Dependência)
- JWT para autenticação

## Variáveis de Ambiente

O projeto utiliza as seguintes variáveis de ambiente:

- `DB_URL`: URL de conexão com o banco de dados PostgreSQL
- `DB_USER`: Usuário do banco de dados
- `DB_PASSWORD`: Senha do banco de dados
- `JWT_SECRET`: Chave secreta para geração e validação de tokens JWT
- `JWT_ISSUER`: Emissor do token JWT
- `JWT_AUDIENCE`: Audiência do token JWT
- `JWT_VALIDITY_MS`: Tempo de validade do token JWT em milissegundos
- `ALLOWED_HOSTS`: Lista de hosts permitidos para CORS, separados por vírgula

## Como Executar

1. Clone o repositório
2. Configure as variáveis de ambiente
3. Execute `./gradlew run` para iniciar o servidor

## Endpoints da API

- `/api/login`: Autenticação de usuários
- `/api/revenues`: Gerenciamento de receitas
- `/api/expenses`: Gerenciamento de despesas
- `/api/reports`: Geração de relatórios

Para mais detalhes sobre os endpoints e seus parâmetros, consulte a documentação da API.
