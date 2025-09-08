
// Create payload keyed dynamically by language ISO codes present in the system
export interface CreateWarehouseRequest {
  name: string
  country: string
  state: string
  city: string
  street: string
  number: string
  zipCode: string
}

export type UpdateWarehouseRequest = CreateWarehouseRequest

export type Warehouse = {
  id: number
} & CreateWarehouseRequest

export async function createWarehouse(data: CreateWarehouseRequest) {
  const response = await fetch("/api/warehouses/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Failed to create warehouse: ${response.statusText}`);
  }

  const warehouse = await response.json();
  return warehouse as Warehouse;
}

export async function getWarehouse(id: number | string): Promise<Warehouse> {
  // Mock: simulate network delay and return a generated warehouse
  await new Promise((r) => setTimeout(r, 300))
  const n = typeof id === 'string' ? parseInt(id, 10) : id
  const safeId = Number.isFinite(n) && (n as number) > 0 ? (n as number) : 1

  return {
    id: safeId,
    name: `Warehouse ${safeId}`,
    country: "Country",
    state: "State",
    city: "City",
    street: "Street",
    number: "123",
    zipCode: "00000-000",
    
  }
}

export async function updateWarehouse(id: number | string, data: UpdateWarehouseRequest): Promise<Warehouse> {
  const response = await fetch(`/api/warehouses/update/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Falha ao atualizar armazém: ${response.statusText}`);
  }

  const warehouse = await response.json();
  return warehouse as Warehouse;
}

export async function deleteWarehouse(id: number | string): Promise<{ ok: true; id: number }> {
  const response = await fetch(`/api/warehouses/delete/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    throw new Error(`Falha ao deletar armazém: ${response.statusText}`);
  }
  return await response.json(); 
}

export interface GetWarehousesParams {
  page?: number
  limit?: number
  search?: string
}

export interface WarehousesPage {
  items: Warehouse[]
  total: number
  page: number
  limit: number
}

export async function getWarehouses(params: GetWarehousesParams = {}): Promise<WarehousesPage> {
  const page = params.page ?? 1
  const limit = params.limit ?? 10
  const search = (params.search ?? "").toLowerCase()

  // Mocked dataset
  const MOCK: Warehouse[] = Array.from({ length: 57 }).map((_, i) => {
    const id = i + 1
    return {
      id,
      name: `Warehouse ${id}`,
      country: `Country ${id}`,
      state: `State ${id}`,
      city: `City ${id}`,
      street: `Street ${id}`,
      number: `123 ${id}`,
      zipCode: `00000-000 ${id}`,
    }
  })

  // Filter by search (in any name value)
  const filtered = search
    ? MOCK.filter((p) => Object.values(p.name).some((n) => n.toLowerCase().includes(search)))
    : MOCK

  const total = filtered.length
  const start = (page - 1) * limit
  const end = start + limit
  const slice = filtered.slice(start, end)

  // Simulate network latency
  await new Promise((r) => setTimeout(r, 300))

  return {
    items: slice,
    total,
    page,
    limit,
  }
}

