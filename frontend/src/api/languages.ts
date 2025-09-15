// Based on V4__create_languages_table.sql (id, language_name, iso_code)
export interface Language {
  id: number
  name: string
  isoCode: string
}

const API_BASE_URL = (typeof import.meta !== 'undefined' && (import.meta as any).env?.VITE_API_URL) || process.env.VITE_API_URL || process.env.API_URL || ''

// Fetch languages from backend and map to the UI shape
export async function getLanguages(): Promise<Language[]> {
  const base = API_BASE_URL?.replace(/\/$/, '') || ''
  const res = await fetch(`${base}/api/languages`)
  if (!res.ok) {
    throw new Error(`Failed to fetch languages: ${res.status} ${res.statusText}`)
  }
  const data = await res.json() as Array<{ id: number; languageName: string; isoCode: string }>
  return data.map((l) => ({ id: l.id, name: l.languageName, isoCode: l.isoCode }))
}