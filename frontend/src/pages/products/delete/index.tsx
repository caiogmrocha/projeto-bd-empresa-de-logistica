import * as React from "react"
import { useNavigate, useParams } from "react-router"
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import { Button } from "@/components/ui/button"
import { deleteProduct } from "@/api/products"
import { toast } from "sonner"
import { Toaster } from "@/components/ui/sonner"

export const ProductDeletePage: React.FC = () => {
  const { productId = "" } = useParams<{ productId: string }>()
  const navigate = useNavigate()
  const [open, setOpen] = React.useState(true)
  const [pending, setPending] = React.useState(false)

  const onClose = () => {
    setOpen(false)
    // navigate back to list after close
    navigate("/products")
  }

  const onConfirm = async () => {
    if (!productId) return
    try {
      setPending(true)
      await deleteProduct(productId)
      toast.success("Produto excluído com sucesso")
      onClose()
    } catch (error) {
      toast.error((error as Error)?.message || "Falha ao excluir produto")
      setPending(false)
    }
  }

  return (
    <Dialog open={open} onOpenChange={(v) => { if (!v) onClose() }}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Confirmar exclusão</DialogTitle>
          <DialogDescription>
            Tem certeza que deseja excluir o produto {productId}? Esta ação não pode ser desfeita.
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
