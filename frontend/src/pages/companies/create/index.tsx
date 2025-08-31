import { useState } from "react"
import { useMutation } from "@tanstack/react-query"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { zodResolver } from "@hookform/resolvers/zod"

import { createCompany } from "@/api/companies"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from "@/components/ui/form"

const CompanyFormSchema = z.object({
  name: z.string().min(1, "Name is required"),
  email: z.string().email("Invalid email").optional().or(z.literal("")),
  website: z
    .string()
    .url("Invalid URL")
    .optional()
    .or(z.literal("")),
})

type CompanyFormValues = z.infer<typeof CompanyFormSchema>

export const CompanyCreatePage: React.FC = () => {
  const [successMsg, setSuccessMsg] = useState<string | null>(null)
  const [errorMsg, setErrorMsg] = useState<string | null>(null)

  const form = useForm<CompanyFormValues>({
    resolver: zodResolver(CompanyFormSchema),
    defaultValues: {
      name: "",
      email: "",
      website: "",
    },
    mode: "onSubmit",
  })

  const mutation = useMutation({
    mutationFn: createCompany,
    onSuccess: (data) => {
      setErrorMsg(null)
      setSuccessMsg(`Company “${data.name}” created (id: ${data.id}).`)
      form.reset()
    },
    onError: (err: unknown) => {
      setSuccessMsg(null)
      setErrorMsg(
        err instanceof Error ? err.message : "An error occurred while creating the company."
      )
    },
  })

  function onSubmit(values: CompanyFormValues) {
    // Normalize optional empty strings to undefined
    const payload = {
      ...values,
      email: values.email || undefined,
      website: values.website || undefined,
    }
    mutation.mutate(payload)
  }

  return (
    <div className="mx-auto w-full max-w-5xl p-6">
      <Card>
        <CardHeader>
          <CardTitle>Create company</CardTitle>
          <CardDescription>Fill in the details below and submit.</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-5">
              <FormField
                control={form.control}
                name="name"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Name</FormLabel>
                    <FormControl>
                      <Input placeholder="Acme Inc" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Email (optional)</FormLabel>
                    <FormControl>
                      <Input type="email" placeholder="admin@acme.com" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="website"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel>Website (optional)</FormLabel>
                    <FormControl>
                      <Input placeholder="https://acme.com" {...field} />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <div className="flex items-center gap-3">
                <Button type="submit" disabled={mutation.isPending}>
                  {mutation.isPending ? "Creating..." : "Create company"}
                </Button>
                {mutation.isPending && (
                  <span className="text-sm text-zinc-500">Submitting your company...</span>
                )}
              </div>
            </form>
          </Form>

          {successMsg && (
            <div className="mt-6 rounded-md border border-green-300 bg-green-50 p-3 text-sm text-green-800">
              {successMsg}
            </div>
          )}
          {errorMsg && (
            <div className="mt-6 rounded-md border border-red-300 bg-red-50 p-3 text-sm text-red-800">
              {errorMsg}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
