# ETL: Importar idiomas ISO para MySQL

Este diretório contém um ETL simples que importa dados de idiomas ISO a partir de um arquivo CSV para uma tabela MySQL utilizando Pandas e SQLAlchemy.

Mapeamento de colunas do CSV:
- "Endonym(s)" → language_name (preferencial)
- "ISO Language Names" → language_name (fallback)
- "Set 1" → iso_code

O script irá:
- Criar a tabela `languages` caso não exista
- Limpar e normalizar nomes e códigos
- Remover duplicidades por `iso_code`
- Fazer upsert por `iso_code` (atualiza quando já existe)

## Pré-requisitos
- Python 3.10+ (recomendado)
- Banco MySQL acessível a partir desta máquina
  - Usuário com permissões de CREATE TABLE e INSERT/UPDATE
- Arquivo CSV contendo os cabeçalhos: `Endonym(s)`, `ISO Language Names`, `Set 1`

## Passo a passo (rápido)

1) Crie e ative um ambiente virtual e instale as dependências:

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```

2) Configure a URL do banco em uma variável de ambiente (recomendado):

```bash
# Substitua os placeholders, evite expor segredos no histórico do shell.
export DB_URL='mysql+pymysql://{{MYSQL_USER}}:{{MYSQL_PASSWORD}}@{{MYSQL_HOST}}:3306/{{MYSQL_DATABASE}}?charset=utf8mb4'
```

3) Execute o ETL:

```bash
# Ajuste o caminho/nome do CSV conforme sua realidade
python3 script.py --csv ./List_of_ISO_639_language_codes_1.csv --db "$DB_URL"
```

Opcionalmente, torne o script executável e rode diretamente:

```bash
chmod +x script.py
./script.py --csv ./List_of_ISO_639_language_codes_1.csv --db "$DB_URL"
```

## Parâmetros do script
- `--csv` (obrigatório): Caminho do arquivo CSV, ex.: `./List_of_ISO_639_language_codes_1.csv`
- `--db` (obrigatório): URL do banco no formato SQLAlchemy, ex.: `mysql+pymysql://user:password@localhost:3306/projeto_bd?charset=utf8mb4`

## Tabela criada/atualizada
O script garante a existência da tabela abaixo e realiza upsert por `iso_code`:

```sql
CREATE TABLE IF NOT EXISTS languages (
  id SERIAL PRIMARY KEY,
  language_name VARCHAR(50) NOT NULL,
  iso_code CHAR(4) NOT NULL UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
  deleted_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
```

## Saída esperada
Em caso de sucesso, a saída será semelhante a:

```
Upsert tentado para N linhas (rowcount retornado: M).
```

- `N` é a quantidade de linhas após limpeza/deduplicação
- `M` é o rowcount reportado pelo driver nas operações de upsert

## Referências

- [List of ISO 639 language codes - Wikipedia](https://en.wikipedia.org/wiki/List_of_ISO_639_language_codes)
