import { useState } from "react"
import { useMutation } from "@tanstack/react-query"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"

import { createWarehouse } from "@/api/warehouses"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"

const WarehouseFormSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório").max(100, "Nome deve ter no máximo 100 caracteres"),
  country: z.string().min(1, "País é obrigatório").max(50, "País deve ter no máximo 50 caracteres"),
  state: z.string().min(1, "Estado é obrigatório").max(50, "Estado deve ter no máximo 50 caracteres"),
  city: z.string().min(1, "Cidade é obrigatória").max(50, "Cidade deve ter no máximo 50 caracteres"),
  street: z.string().min(1, "Rua é obrigatória").max(100, "Rua deve ter no máximo 100 caracteres"),
  number: z.string().min(1, "Número é obrigatório").max(10, "Número deve ter no máximo 10 dígitos"),
  zipCode: z.string().min(1, "CEP é obrigatório").max(8, "CEP deve ter no máximo 8 dígitos"),
})

type WarehouseFormValues = z.infer<typeof WarehouseFormSchema>

export const WarehouseCreatePage: React.FC = () => {
  const [successMsg, setSuccessMsg] = useState<string | null>(null)
  const [errorMsg, setErrorMsg] = useState<string | null>(null)

  const form = useForm<WarehouseFormValues>({
    resolver: zodResolver(WarehouseFormSchema),
    defaultValues: {
      name: "",
      country: "",
      state: "",
      city: "",
      street: "",
      number: "",
      zipCode: "",
    },
    mode: "onSubmit",
  })

  const mutation = useMutation({
    mutationFn: createWarehouse,
    onSuccess: (data) => {
      setErrorMsg(null)
      setSuccessMsg(`Armazém “${data.name}” criado (id: ${data.id}).`)
      form.reset()
    },
    onError: (err: unknown) => {
      setSuccessMsg(null)
      setErrorMsg(
        err instanceof Error ? err.message : "Um erro ocorreu ao criar o armazém."
      )
    },
  })

  function onSubmit(values: WarehouseFormValues) {
    // Normalize optional empty strings to undefined
    const payload = {
      ...values,
      address: {
        country: values.country,
        state: values.state,
        city: values.city,
        street: values.street,
        number: values.number,
        zipCode: values.zipCode,
      }
    }
    mutation.mutate(payload)
  }

  return (
    <div className="mx-auto w-full max-w-5xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Criar armazém</CardTitle>
          <CardDescription>Preencha os detalhes abaixo e envie.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Nome</FormLabel>
                    <FormControl>
                      <Input placeholder="Acme Logistics" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="country"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>País</FormLabel>
                    <FormControl>
                      <Input type="text" placeholder="USA" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="state"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Estado</FormLabel>
                    <FormControl>
                      <Input type="text" placeholder="California" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="flex gap-4">
                <div className="w-1/2">
                  <FormField
                    control={form.control}
                    name="city"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Cidade</FormLabel>
                        <FormControl>
                          <Input type="text" placeholder="Los Angeles" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>
                
                <div className="w-1/2">
                  <FormField
                    control={form.control}
                    name="street"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Rua</FormLabel>
                        <FormControl>
                          <Input type="text" placeholder="123 Main St" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>
              </div>

              <div className="flex gap-4">
                <div className="w-1/2">
                  <FormField
                    control={form.control}
                    name="number"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Número</FormLabel>
                        <FormControl>
                          <Input type="text" placeholder="123" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>

                <div className="w-1/2">
                  <FormField
                    control={form.control}
                    name="zipCode"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>CEP</FormLabel>
                        <FormControl>
                          <Input type="text" placeholder="90001" {...field} />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>
              </div>

              <div className="flex items-center gap-3">
                <Button type="submit" disabled={mutation.isPending}>
                  {mutation.isPending ? "Creating..." : "Create warehouse"}
                </Button>
                {mutation.isPending && (
                  <span className="text-sm text-zinc-500">Enviando seu armazém...</span>
                )}
              </div>
            </form>
          </Form>

          {successMsg && (
            <div className="mt-6 rounded-md border border-green-300 bg-green-50 p-3 text-sm text-green-800">
              {successMsg}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
