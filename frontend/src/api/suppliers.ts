// Based on V8__create_suppliers_table.sql (id, name, type)
export type SupplierType = 'natural_person' | 'legal_entity'

export interface Supplier {
  id: number
  name: string
  type: SupplierType
}

const API_BASE_URL = (typeof import.meta !== 'undefined' && (import.meta as any).env?.VITE_API_URL) || process.env.VITE_API_URL || process.env.API_URL || ''

// Fetch suppliers from backend and map enum to UI type
export async function getSuppliers(): Promise<Supplier[]> {
  const base = API_BASE_URL?.replace(/\/$/, '') || ''
  const res = await fetch(`${base}/api/suppliers`)
  if (!res.ok) {
    throw new Error(`Failed to fetch suppliers: ${res.status} ${res.statusText}`)
  }
  const data = await res.json() as Array<{ id: number; name: string; supplierType: 'NATURAL_PERSON' | 'LEGAL_ENTITY' }>
  return data.map((s) => ({
    id: s.id,
    name: s.name,
    type: s.supplierType === 'NATURAL_PERSON' ? 'natural_person' : 'legal_entity',
  }))
}

