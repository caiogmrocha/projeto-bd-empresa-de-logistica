// Based on V12__create_warehouses_table.sql (id, name)
export interface Warehouse {
  id: number
  name: string
}

// Mocked API to list warehouses. Replace with real HTTP call later.
export async function getWarehouses(): Promise<Warehouse[]> {
  return [
    { id: 1, name: 'Matriz' },
    { id: 2, name: 'Filial Norte' },
    { id: 3, name: 'Centro Logístico' },
  ]
}

