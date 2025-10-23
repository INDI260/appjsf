package com.example.appjsf.web;

import com.example.appjsf.model.CartItem;
import com.example.appjsf.model.Product;
import com.example.appjsf.service.StoreService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Named
@SessionScoped
public class StoreBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private final StoreService storeService;
    private ProductForm form;
    private final Map<Long, CartItem> cart;
    private Long editingProductId;
    private String editingProductName;

    public StoreBean(StoreService storeService) {
        this.storeService = storeService;
        this.form = ProductForm.empty();
        this.cart = new LinkedHashMap<>();
    }

    public ProductForm getForm() {
        return form;
    }

    public List<CartItem> getCartItems() {
        return new ArrayList<>(cart.values());
    }

    public boolean getHasCartItems() {
        return !cart.isEmpty();
    }

    public Long getEditingProductId() {
        return editingProductId;
    }

    public String getEditingProductName() {
        return editingProductName;
    }

    public void setEditingProductName(String editingProductName) {
        this.editingProductName = editingProductName;
    }

    public boolean isEditingProduct(Long productId) {
        return editingProductId != null && editingProductId.equals(productId);
    }

    public void addProductToCart() {
        try {
            validateForm();
            Product stored = storeService.save(form.toProduct());
            CartItem item = cart.computeIfAbsent(stored.getId(), key -> new CartItem(new Product(stored.getId(), stored.getName()), 0));
            item.getProduct().setName(stored.getName());
            item.setQuantity(item.getQuantity() + 1);
            addMessage("Producto añadido al carrito", FacesMessage.SEVERITY_INFO);
            resetForm();
        } catch (IllegalArgumentException ex) {
            addMessage(ex.getMessage(), FacesMessage.SEVERITY_ERROR);
        }
    }

    public void increaseQuantity(Long productId) {
        CartItem item = cart.get(productId);
        if (item == null) {
            return;
        }
        item.setQuantity(item.getQuantity() + 1);
        addMessage("Cantidad actualizada", FacesMessage.SEVERITY_INFO);
    }

    public void decreaseQuantity(Long productId) {
        CartItem item = cart.get(productId);
        if (item == null) {
            return;
        }
        int updated = item.getQuantity() - 1;
        if (updated <= 0) {
            cart.remove(productId);
            addMessage("Producto eliminado del carrito", FacesMessage.SEVERITY_INFO);
        } else {
            item.setQuantity(updated);
            addMessage("Cantidad actualizada", FacesMessage.SEVERITY_INFO);
        }
    }

    public void startEditing(Long productId) {
        CartItem item = cart.get(productId);
        if (item == null) {
            return;
        }
        this.editingProductId = productId;
        this.editingProductName = item.getProduct().getName();
    }

    public void applyEdit() {
        if (editingProductId == null) {
            return;
        }
        if (!StringUtils.hasText(editingProductName)) {
            addMessage("El nombre es obligatorio", FacesMessage.SEVERITY_ERROR);
            return;
        }
        CartItem item = cart.get(editingProductId);
        if (item == null) {
            cancelEdit();
            return;
        }
        Product updated = new Product(editingProductId, editingProductName.trim());
        Product stored = storeService.save(updated);
        item.getProduct().setName(stored.getName());
        addMessage("Nombre actualizado", FacesMessage.SEVERITY_INFO);
        cancelEdit();
    }

    public void cancelEdit() {
        this.editingProductId = null;
        this.editingProductName = null;
    }

    public void clearCart() {
        cart.clear();
        cancelEdit();
        addMessage("Carrito vaciado", FacesMessage.SEVERITY_INFO);
    }

    private void resetForm() {
        this.form = ProductForm.empty();
    }

    private void validateForm() {
        if (form.getId() == null) {
            throw new IllegalArgumentException("El identificador es obligatorio");
        }
        if (!StringUtils.hasText(form.getName())) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        form.setName(form.getName().trim());
    }

    private void addMessage(String detail, FacesMessage.Severity severity) {
        FacesMessage message = new FacesMessage(severity, detail, null);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext != null) {
            facesContext.addMessage(null, message);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class ProductForm implements Serializable {

        private static final long serialVersionUID = 1L;
        private Long id;
        private String name;

        static ProductForm empty() {
            return new ProductForm();
        }

        Product toProduct() {
            return new Product(id, name);
        }
    }
}
