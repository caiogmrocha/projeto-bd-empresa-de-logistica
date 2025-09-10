
// Create payload keyed dynamically by language ISO codes present in the system
export interface CreateCustomerRequest {
  name: string
  country: string
  credit: number
  state: string
  city: string
  street: string
  number: string
  zipCode: string
}

export type UpdateCustomerRequest = CreateCustomerRequest

export type Customer = {
  id: number
} & CreateCustomerRequest

export async function createCustomer(data: CreateCustomerRequest) {
  const response = await fetch("/api/customers/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Failed to create customer: ${response.statusText}`);
  }

  const customer = await response.json();
  return customer as Customer;
}

export async function getCustomer(id: number | string): Promise<Customer> {
  // Mock: simulate network delay and return a generated customer
  await new Promise((r) => setTimeout(r, 300))
  const n = typeof id === 'string' ? parseInt(id, 10) : id
  const safeId = Number.isFinite(n) && (n as number) > 0 ? (n as number) : 1

  return {
    id: safeId,
    name: `Customer ${safeId}`,
    country: "Country",
    credit: 100,
    state: "State",
    city: "City",
    street: "Street",
    number: "123",
    zipCode: "00000-000",
    
  }
}

export async function updateCustomer(id: number | string, data: UpdateCustomerRequest): Promise<Customer> {
  const response = await fetch(`/api/customers/update/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    throw new Error(`Falha ao atualizar armazém: ${response.statusText}`);
  }

  const customer = await response.json();
  return customer as Customer;
}

export async function deleteCustomer(id: number | string): Promise<{ ok: true; id: number }> {
  const response = await fetch(`/api/customers/delete/${id}`, {
    method: "DELETE",
  });
  if (!response.ok) {
    throw new Error(`Falha ao deletar cliente: ${response.statusText}`);
  }
  return await response.json(); 
}

export interface GetCustomersParams {
  page?: number
  limit?: number
  search?: string
}

export interface CustomersPage {
  items: Customer[]
  total: number
  page: number
  limit: number
}

export async function getCustomers(params: GetCustomersParams = {}): Promise<CustomersPage> {
  const page = params.page ?? 1
  const limit = params.limit ?? 10
  const search = (params.search ?? "").toLowerCase()

  // Mocked dataset
  const MOCK: Customer[] = Array.from({ length: 57 }).map((_, i) => {
    const id = i + 1
    return {
      id,
      name: `Customer ${id}`,
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

