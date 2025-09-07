// Based on V4__create_languages_table.sql (id, language_name, iso_code)
export interface Language {
  id: number
  name: string
  isoCode: string
}

// Mocked API returning languages configured in the system.
// Replace with a real HTTP call when the backend is ready.
export async function getLanguages(): Promise<Language[]> {
  return [
    { id: 1, name: 'Português (Brasil)', isoCode: 'pt-BR' },
    { id: 2, name: 'English', isoCode: 'en' },
    { id: 3, name: 'Español', isoCode: 'es' },
    { id: 4, name: 'Français', isoCode: 'fr' },
    { id: 5, name: 'Deutsch', isoCode: 'de' },
    { id: 6, name: 'Italiano', isoCode: 'it' },
    { id: 7, name: 'Nederlands', isoCode: 'nl' }
  ]
}
