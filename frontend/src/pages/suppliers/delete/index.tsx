import * as React from "react"
import { useNavigate, useParams } from "react-router"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { deleteSupplier } from "@/api/suppliers"
import { toast } from "sonner"
import { Toaster } from "@/components/ui/sonner"
import { useMutation, useQueryClient } from "@tanstack/react-query" 

export const SupplierDeletePage: React.FC = () => {
  const { supplierId = "" } = useParams<{ supplierId: string }>()
  const navigate = useNavigate()
  const [open, setOpen] = React.useState(true)
  const queryClient = useQueryClient() 

  const mutation = useMutation({
    mutationFn: () => deleteSupplier(supplierId),
    onSuccess: () => {
        toast.success("Fornecedor excluído com sucesso")
        queryClient.invalidateQueries({ queryKey: ["suppliers"] }) 
        onClose()
    },
    onError: (error) => {
        toast.error((error as Error)?.message || "Falha ao excluir fornecedor")
    }
  })


  const onClose = () => {
    setOpen(false)
    navigate("/suppliers")
  }

  const onConfirm = () => {
    if (!supplierId) return
    mutation.mutate()
  }

  return (
    <Dialog open={open} onOpenChange={(v) => { if (!v) onClose() }}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Confirmar exclusão</DialogTitle>
          <DialogDescription>
            Tem certeza que deseja excluir o fornecedor {supplierId}? Esta ação não pode ser desfeita.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} disabled={mutation.isPending}>Cancelar</Button>
          <Button variant="destructive" onClick={onConfirm} disabled={mutation.isPending}>
            {mutation.isPending ? "Excluindo..." : "Excluir"}
          </Button>
        </DialogFooter>
      </DialogContent>
      <Toaster />
    </Dialog>
  )
}