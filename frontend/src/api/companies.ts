// src/api/companies.ts
// Mocked API for creating companies. Replace with real HTTP calls when ready.

export type CreateCompanyInput = {
  name: string
  email?: string
  website?: string
}

export type Company = CreateCompanyInput & { id: string; createdAt: string }

// Simulate an API delay and return the created record
export async function createCompany(input: CreateCompanyInput): Promise<Company> {
  await wait(800)
  return {
    id: generateId(),
    createdAt: new Date().toISOString(),
    ...input,
  }
}

function wait(ms: number) {
  return new Promise((resolve) => setTimeout(resolve, ms))
}

function generateId() {
  // Prefer Web Crypto if available (in browsers), fallback to timestamp-based id
  try {
    // @ts-expect-error: crypto may not exist in some runtimes
    if (typeof crypto !== "undefined" && crypto.randomUUID) {
      // @ts-expect-error: crypto may not exist in some runtimes
      return crypto.randomUUID()
    }
  } catch {}
  return `cmp_${Date.now()}_${Math.random().toString(36).slice(2, 8)}`
}

