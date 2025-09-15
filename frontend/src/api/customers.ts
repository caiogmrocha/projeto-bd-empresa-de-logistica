// Create payload keyed dynamically by language ISO codes present in the system
export interface CreateCustomerRequest {
  name: string
  creditLimit: number
  addresses: Addresses[]
}

export interface Addresses {
  country: string
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
  const response = await fetch("http://localhost:8080/api/customers/create", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })

  if (!response.ok) {
    throw new Error(`Failed to create customer: ${response.statusText}`)
  }

  const customer = await response.json()
  return customer as Customer
}

export async function getCustomer(id: number | string): Promise<Customer> {
  const response = await fetch(`http://localhost:8080/api/customers/${id}`)

  if (!response.ok) {
    throw new Error(`Failed to fetch customer: ${response.statusText}`)
  }

  const customer = await response.json()
  return customer as Customer
}

export async function updateCustomer(
  id: number | string,
  data: UpdateCustomerRequest
): Promise<Customer> {
  const response = await fetch(`http://localhost:8080/api/customers/update/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  })

  if (!response.ok) {
    throw new Error(`Falha ao atualizar cliente: ${response.statusText}`)
  }

  const customer = await response.json()
  return customer as Customer
}

export async function deleteCustomer(id: number | string): Promise<{ ok: true; id: number }> {
  const response = await fetch(`http://localhost:8080/api/customers/delete/${id}`, {
    method: "DELETE",
  })

  if (response.status === 400) {
    throw new Error(`Falha ao deletar cliente: ${response.statusText}`)
  }

  return { ok: true, id: Number(id) }
}

export interface GetCustomersParams {
  page?: number
  limit?: number
  search?: string
}

export interface CustomersPage {
  content: Customer[]
  total: number
  page: number
  limit: number
}

export async function getCustomers(params: GetCustomersParams = {}): Promise<CustomersPage> {
  const page = params.page ?? 0
  const size = params.limit ?? 10
  const search = params.search ?? ""

  // Adiciona search somente se tiver algo
  const searchParam = search ? `&search=${encodeURIComponent(search)}` : ""

  const response = await fetch(
    `http://localhost:8080/api/customers?page=${page}&size=${size}${searchParam}`
  )

  if (!response.ok) {
    throw new Error(`Failed to fetch customers: ${response.statusText}`)
  }

  const data = await response.json()
  return {
    content: data.content as Customer[],
    total: data.totalElements as number,
    page: data.number as number,
    limit: data.size as number,
  }
}
