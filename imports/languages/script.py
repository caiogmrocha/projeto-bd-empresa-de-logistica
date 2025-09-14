#!/usr/bin/env python3
"""
Import ISO language codes from a CSV into a MySQL table using Pandas + SQLAlchemy,
saving the ENDONYM ("Endonym(s)") into `language_name` and the ISO-639-1 code ("Set 1")
into `iso_code`.

- Creates table `languages` if it does not exist (DDL below).
- Reads the CSV (tested with "List_of_ISO_639_language_codes_1.csv").
- Uses "Endonym(s)" as `language_name` (fallback to English name).
- Uses "Set 1" as `iso_code`.
- Normalizes/deduplicates and upserts by `iso_code` (UNIQUE).
"""

import argparse
import re
import sys
import pandas as pd
from sqlalchemy import create_engine, text
from sqlalchemy.dialects.mysql import insert as mysql_insert

DDL = """
CREATE TABLE IF NOT EXISTS languages (
  id SERIAL PRIMARY KEY,
  language_name VARCHAR(50) NOT NULL,
  iso_code CHAR(4) NOT NULL UNIQUE,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
  deleted_at TIMESTAMP NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
"""

ENDONYM_COL = "Endonym(s)"
ENGLISH_NAME_COL = "ISO Language Names"
ISO1_COL = "Set 1"

def clean_code(s: str | None) -> str | None:
    if s is None:
        return None
    s = str(s).strip().lower()
    if not s:
        return None
    return s[:4]  # CHAR(4) na tabela

def _first_nonempty(*values):
    for v in values:
        if isinstance(v, str):
            v = v.strip()
            if v:
                return v
        elif pd.notna(v):
            v = str(v).strip()
            if v:
                return v
    return None

def clean_name(s: str | None) -> str | None:
    if not isinstance(s, str):
        return None
    s = s.strip()
    # remove anotações longas entre colchetes ou parênteses
    s = re.sub(r"\s*\[[^\]]*\]\s*", " ", s).strip()
    s = re.sub(r"\s*\([^)]{15,}\)\s*", " ", s).strip()
    # pegar só a primeira variante antes de ';' ou '/'
    s = re.split(r"[;/]", s)[0].strip()
    return s or None

def load_and_transform(csv_path: str) -> pd.DataFrame:
    df = pd.read_csv(csv_path)

    # remover linhas de cabeçalho duplicadas
    if ENGLISH_NAME_COL in df.columns:
        df = df[df[ENGLISH_NAME_COL] != ENGLISH_NAME_COL].copy()

    # iso_code vem diretamente de "Set 1"
    if ISO1_COL not in df.columns:
        print(f'Erro: coluna "{ISO1_COL}" não encontrada no CSV.', file=sys.stderr)
        return pd.DataFrame()

    df["iso_code"] = df[ISO1_COL].apply(clean_code)

    # language_name: preferir Endonym(s), fallback para English name
    def pick_language_name(row):
        val = _first_nonempty(row.get(ENDONYM_COL), row.get(ENGLISH_NAME_COL))
        return clean_name(val)

    df["language_name"] = df.apply(pick_language_name, axis=1)

    out = (
        df[["language_name", "iso_code"]]
        .dropna(subset=["language_name", "iso_code"])
        .copy()
    )

    out["language_name"] = out["language_name"].astype(str).str.slice(0, 50)
    out = out.drop_duplicates(subset=["iso_code"], keep="first").reset_index(drop=True)
    return out

def ensure_table(engine):
    with engine.begin() as conn:
        conn.execute(text(DDL))

def upsert_languages(engine, df: pd.DataFrame):
    """Upsert por iso_code usando MySQL ON DUPLICATE KEY UPDATE."""
    if df.empty:
        return 0

    from sqlalchemy import Table, Column, MetaData, String, CHAR, Integer, TIMESTAMP
    metadata = MetaData()
    languages = Table(
        "languages",
        metadata,
        Column("id", Integer, primary_key=True, autoincrement=True),
        Column("language_name", String(50), nullable=False),
        Column("iso_code", CHAR(4), nullable=False),
        Column("created_at", TIMESTAMP),
        Column("updated_at", TIMESTAMP),
        Column("deleted_at", TIMESTAMP, nullable=True),
        extend_existing=True,
    )

    rows = df.to_dict(orient="records")
    inserted = 0
    with engine.begin() as conn:
        CHUNK = 1000
        for i in range(0, len(rows), CHUNK):
            chunk = rows[i : i + CHUNK]
            stmt = mysql_insert(languages).values(chunk)
            upsert_stmt = stmt.on_duplicate_key_update(
                language_name=stmt.inserted.language_name
            )
            result = conn.execute(upsert_stmt)
            inserted += result.rowcount or 0
    return inserted

def main():
    parser = argparse.ArgumentParser(description='Import languages into MySQL (Endonym(s) + Set 1).')
    parser.add_argument("--csv", required=True, help="Caminho do CSV.")
    parser.add_argument("--db", required=True, help='URL do banco, ex.: mysql+pymysql://user:pass@host:3306/db?charset=utf8mb4')
    args = parser.parse_args()

    df = load_and_transform(args.csv)
    if df.empty:
        print("Nada para importar após limpeza/transform.", file=sys.stderr)
        return 0

    engine = create_engine(args.db, pool_pre_ping=True, pool_recycle=3600)
    ensure_table(engine)

    n = upsert_languages(engine, df)
    print(f'Upsert tentado para {len(df)} linhas (rowcount retornado: {n}).')
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
