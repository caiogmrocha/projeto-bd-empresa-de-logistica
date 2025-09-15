import { useEffect } from "react"
import { useQuery, useMutation } from "@tanstack/react-query"
import { useParams } from "react-router"
import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"

import { getCustomer, updateCustomer, type Customer } from "@/api/customers"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"
import { toast } from "sonner"

const CustomerFormSchema = z.object({
  name: z.string().min(1, "Nome é obrigatório").max(100, "Nome deve ter no máximo 100 caracteres"),
  country: z.string().min(1, "País é obrigatório").max(50, "País deve ter no máximo 50 caracteres"),
  credit: z.number().min(0, "Crédito deve ser um número não negativo"),
  state: z.string().min(1, "Estado é obrigatório").max(50, "Estado deve ter no máximo 50 caracteres"),
  city: z.string().min(1, "Cidade é obrigatória").max(50, "Cidade deve ter no máximo 50 caracteres"),
  street: z.string().min(1, "Rua é obrigatória").max(100, "Rua deve ter no máximo 100 caracteres"),
  number: z.string().min(1, "Número é obrigatório").max(10, "Número deve ter no máximo 10 dígitos"),
  zipCode: z.string().min(1, "CEP é obrigatório").max(8, "CEP deve ter no máximo dígitos"),
})

type CustomerFormValues = z.infer<typeof CustomerFormSchema>

export const EditCustomerPage: React.FC = () => {
  const { customerId = "" } = useParams<{ customerId: string }>()
  const customerQuery = useQuery<Customer>({
    queryKey: ["customer", customerId],
    queryFn: () => getCustomer(customerId),
    enabled: !!customerId,
  })

  const form = useForm<CustomerFormValues>({
    resolver: zodResolver(CustomerFormSchema),
    defaultValues: {
      name: "",
      country: "",
      credit: 0,
      state: "",
      city: "",
      street: "",
      number: "",
      zipCode: "",
    },
    mode: "onSubmit",
  })

  // Populate form when customer data arrives
  useEffect(() => {
    const c = customerQuery.data
    if (!c) return
    form.reset({
      name: c.name,
      credit: c.credit,
      country: c.address.country,
      state: c.address.state,
      city: c.address.city,
      street: c.address.street,
      number: c.address.number,
      zipCode: c.address.zipCode,
    })
  }, [form, customerQuery.data])

  const mutation = useMutation({
    mutationFn: (values: CustomerFormValues) =>
      updateCustomer(customerId, {
        name: values.name,
        credit: values.credit || 0,
        address: {
          country: values.country,
          state: values.state,
          city: values.city,
          street: values.street,
          number: values.number,
          zipCode: values.zipCode,
        },
      }),
    onSuccess: () => {
      toast.success("Warehouse updated successfully")
    },
    onError: (err: Error) => {
      toast.error(err.message)
    }
  })

  function onSubmit(values: CustomerFormValues) {
    mutation.mutate(values)
  }

  if (customerQuery.isLoading) {
    return <div className="p-6 text-sm text-zinc-500">Carregando cliente...</div>
  }

  if (customerQuery.isError) {
    return <div className="p-6 text-sm text-red-500">Erro ao carregar cliente.</div>
  }

  return (
    <div className="mx-auto w-full max-w-5xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Editar cliente</CardTitle>
          <CardDescription>Atualize os detalhes do cliente e salve.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid gap-4 md:grid-cols-2">
                <FormField
                  control={form.control}
                  name="name"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Nome</FormLabel>
                      <FormControl>
                        <Input placeholder="Nome do armazém" {...field} />
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
                        <Input placeholder="País" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="credit"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Crédito</FormLabel>
                      <FormControl>
                        <Input
                          placeholder="1000"
                          type="number"
                          min={0}
                          {...field}
                          value={field.value === 0 ? "" : field.value}
                          onChange={e => {
                            const val = e.target.value;
                            field.onChange(val === "" ? "" : Number(val));
                          }}
                        />
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
                        <Input placeholder="Estado" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="city"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Cidade</FormLabel>
                      <FormControl>
                        <Input placeholder="Cidade" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="street"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Rua</FormLabel>
                      <FormControl>
                        <Input placeholder="Rua" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="number"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Número</FormLabel>
                      <FormControl>
                        <Input placeholder="Número" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <FormField
                  control={form.control}
                  name="zipCode"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>CEP</FormLabel>
                      <FormControl>
                        <Input placeholder="CEP" {...field} />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
              </div>
              <div className="flex items-center gap-3">
                <Button type="submit" disabled={mutation.isPending}>
                  {mutation.isPending ? "Salvando..." : "Salvar alterações"}
                </Button>
              </div>
            </form>
          </Form>
        </CardContent>
      </Card>
    </div>
  )
}