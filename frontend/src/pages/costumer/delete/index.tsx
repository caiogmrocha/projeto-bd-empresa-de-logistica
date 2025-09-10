import * as React from "react"
import { useNavigate, useParams } from "react-router"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { deleteCustomer } from "@/api/customers"
import { toast } from "sonner"
import { Toaster } from "sonner"

export const CustomerDeletePage: React.FC = () => {
  const { customerId = "" } = useParams<{ customerId: string }>()
  const navigate = useNavigate()
  const [open, setOpen] = React.useState(true)
  const [pending, setPending] = React.useState(false)

  const onClose = () => {
    setOpen(false)
    // navigate back to list after close
    navigate("/customers")
  }

  const onConfirm = async () => {
    if (!customerId) return
    try {
      setPending(true)
      await deleteCustomer(customerId)
      toast.success("Cliente excluído com sucesso")
      onClose()
    } catch (error) {
      toast.error((error as Error)?.message || "Falha ao excluir cliente")
      setPending(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={(v) => { if (!v) onClose() }}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Confirmar exclusão</DialogTitle>
          <DialogDescription>
            Tem certeza que deseja excluir o cliente {customerId}? Esta ação não pode ser desfeita.
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <Button variant="outline" onClick={onClose} disabled={pending}>Cancelar</Button>
          <Button variant="destructive" onClick={onConfirm} disabled={pending}>
            {pending ? "Excluindo..." : "Excluir"}
          </Button>
        </DialogFooter>
      </DialogContent>
      <Toaster />
    </Dialog>
  )
}