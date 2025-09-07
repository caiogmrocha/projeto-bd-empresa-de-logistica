// Based on V8__create_suppliers_table.sql (id, name, type)
export type SupplierType = 'natural_person' | 'legal_entity'

export interface Supplier {
  id: number
  name: string
  type: SupplierType
}

// Mocked API to list suppliers. Replace with real HTTP call later.
export async function getSuppliers(): Promise<Supplier[]> {
  return [
    { id: 1, name: 'ACME Ltda', type: 'legal_entity' },
    { id: 2, name: 'João da Silva', type: 'natural_person' },
    { id: 3, name: 'Globex Corp', type: 'legal_entity' },
  ]
}

