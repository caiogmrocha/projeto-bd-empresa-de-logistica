
// Create payload keyed dynamically by language ISO codes present in the system
export interface CreateCostumerRequest {
  name: string
  country: string
  credit: number
  state: string
  city: string
  street: string
  number: string
  zipCode: string
}

export type UpdateCostumerRequest = CreateCostumerRequest

export type Costumer = {
  id: number
} & CreateCostumerRequest

export async function createCostumer(data: CreateCostumerRequest) {
  const response = await fetch("/api/costumers/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Failed to create costumer: ${response.statusText}`);
  }

  const costumer = await response.json();
  return costumer as Costumer;
}

export async function getCostumer(id: number | string): Promise<Costumer> {
  // Mock: simulate network delay and return a generated costumer
  await new Promise((r) => setTimeout(r, 300))
  const n = typeof id === 'string' ? parseInt(id, 10) : id
  const safeId = Number.isFinite(n) && (n as number) > 0 ? (n as number) : 1

  return {
    id: safeId,
    name: `Costumer ${safeId}`,
    country: "Country",
    credit: 100,
    state: "State",
    city: "City",
    street: "Street",
    number: "123",
    zipCode: "00000-000",
    
  }
}

export async function updateCostumer(id: number | string, data: UpdateCostumerRequest): Promise<Costumer> {
  const response = await fetch(`/api/costumers/update/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Falha ao atualizar armazém: ${response.statusText}`);
  }

  const costumer = await response.json();
  return costumer as Costumer;
}

export async function deleteCostumer(id: number | string): Promise<{ ok: true; id: number }> {
  const response = await fetch(`/api/costumers/delete/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    throw new Error(`Falha ao deletar cliente: ${response.statusText}`);
  }
  return await response.json(); 
}

export interface GetCostumersParams {
  page?: number
  limit?: number
  search?: string
}

export interface CostumersPage {
  items: Costumer[]
  total: number
  page: number
  limit: number
}

export async function getCostumers(params: GetCostumersParams = {}): Promise<CostumersPage> {
  const page = params.page ?? 1
  const limit = params.limit ?? 10
  const search = (params.search ?? "").toLowerCase()

  // Mocked dataset
  const MOCK: Costumer[] = Array.from({ length: 57 }).map((_, i) => {
    const id = i + 1
    return {
      id,
      name: `Costumer ${id}`,
      country: `Country ${id}`,
      credit: 100,
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

