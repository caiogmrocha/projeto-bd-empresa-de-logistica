// Based on V12__create_warehouses_table.sql (id, name)
export interface Warehouse {
  id: number
  name: string
}

const API_BASE_URL = (typeof import.meta !== 'undefined' && (import.meta as any).env?.VITE_API_URL) || process.env.VITE_API_URL || process.env.API_URL || ''

// Fetch warehouses from backend and flatten the page
export async function getWarehouses(): Promise<Warehouse[]> {
  const base = API_BASE_URL?.replace(/\/$/, '') || ''
  const res = await fetch(`${base}/api/warehouses?page=0&size=100`)
  if (!res.ok) {
    throw new Error(`Failed to fetch warehouses: ${res.status} ${res.statusText}`)
  }
  const data = await res.json() as { content: Array<{ id: number; name: string }> }
  return (data.content ?? []).map((w) => ({ id: w.id, name: w.name }))
}

