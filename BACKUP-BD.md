# Backup de Volume PostgreSQL no Docker

Este documento fornece instruções passo-a-passo para realizar o backup de um volume Docker contendo dados do PostgreSQL.

## Pré-requisitos

- Docker instalado e em execução
- Um container PostgreSQL em execução (neste exemplo, chamado `postgres_padarias`)

## Passos para Backup

1. **Verifique se o container está em execução**

   ```bash
   docker ps
   ```

   Certifique-se de que o container `postgres_padarias` está listado e em estado "Up".

2. **Escolha um diretório para salvar o backup**

   Navegue até o diretório onde você deseja salvar o arquivo de backup:

   ```bash
   cd /caminho/para/diretorio/de/backup
   ```

3. **Execute o comando de backup**

   ```bash
   docker run --rm --volumes-from postgres_padarias -v $(pwd):/backup ubuntu tar cvf /backup/postgres_backup.tar /var/lib/postgresql/data
   ```

   Este comando cria um arquivo `postgres_backup.tar` no diretório atual.

4. **Verifique se o backup foi criado**

   ```bash
   ls -l postgres_backup.tar
   ```

   Você deve ver o arquivo de backup listado.

## Notas Importantes

- O backup é uma cópia direta dos arquivos do volume. Para garantir a consistência dos dados, considere parar o container antes do backup:

  ```bash
  docker stop postgres_padarias
  # Execute o comando de backup
  docker start postgres_padarias
  ```

- Para backups mais seguros em ambientes de produção, considere usar `pg_dump`:

  ```bash
  docker exec postgres_padarias pg_dump -U padaria padarias_db > backup.sql
  ```

## Restauração do Backup

Para restaurar o backup (use com cautela, pois isso substituirá os dados existentes):

```bash
docker stop postgres_padarias
docker run --rm --volumes-from postgres_padarias -v $(pwd):/backup ubuntu bash -c "cd /var/lib/postgresql/data && tar xvf /backup/postgres_backup.tar --strip 1"
docker start postgres_padarias
```

## Solução de Problemas

Se você encontrar o erro "docker: invalid reference format", tente uma destas soluções:

1. Use um caminho absoluto:
   ```bash
   docker run --rm --volumes-from postgres_padarias -v /caminho/completo/para/seu/diretorio:/backup ubuntu tar cvf /backup/postgres_backup.tar /var/lib/postgresql/data
   ```

2. No Windows, use o formato de caminho do Windows:
   ```bash
   docker run --rm --volumes-from postgres_padarias -v C:\caminho\para\seu\diretorio:/backup ubuntu tar cvf /backup/postgres_backup.tar /var/lib/postgresql/data
   ```

3. No PowerShell do Windows:
   ```powershell
   docker run --rm --volumes-from postgres_padarias -v "${PWD}:/backup" ubuntu tar cvf /backup/postgres_backup.tar /var/lib/postgresql/data
   ```

4. Em sistemas Unix-like, use aspas simples:
   ```bash
   docker run --rm --volumes-from postgres_padarias -v '$(pwd)':/backup ubuntu tar cvf /backup/postgres_backup.tar /var/lib/postgresql/data
   ```

Lembre-se sempre de ajustar os caminhos e nomes de container conforme necessário para o seu ambiente específico.