/*
 * Copyright (c) 2021. Liem Doan
 */

package com.fromlabs.inventory.inventoryservice.ingredient.beans.request;

import com.fromlabs.inventory.inventoryservice.common.helper.BaseCustomizePageRequest;
import lombok.*;

import java.util.Objects;

/**
 * Ingredient page request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngredientPageRequest extends BaseCustomizePageRequest {
    private Long clientId;
    private Long parentId = -1L;
    private String name = "";
    private String code = "";
    private String unit = "";
    private String description = "";
    private String unitType = "";
    private String createAt = "";
    private String updateAt = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IngredientPageRequest that = (IngredientPageRequest) o;
        if (!clientId.equals(that.clientId)) return false;
        if (!Objects.equals(parentId, that.parentId)) return false;
        if (!Objects.equals(name.trim(), that.name.trim())) return false;
        if (!Objects.equals(code.trim(), that.code.trim())) return false;
        if (!Objects.equals(unit.replaceAll("\\s", ""), that.unit.replaceAll("\\s", ""))) return false;
        if (!Objects.equals(description.trim(), that.description.trim())) return false;
        if (!Objects.equals(unitType.replaceAll("\\s", ""), that.unitType.replaceAll("\\s", ""))) return false;
        if (!Objects.equals(createAt.replaceAll("\\s", ""), that.createAt.replaceAll("\\s", ""))) return false;
        if (!Objects.equals(page, that.page)) return false;
        if (!Objects.equals(size, that.size)) return false;
        if (!Objects.equals(sort.replaceAll("\\s", ""), that.sort.replaceAll("\\s", ""))) return false;
        return Objects.equals(updateAt.replaceAll("\\s", ""), that.updateAt.replaceAll("\\s", ""));
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + clientId.hashCode();
        result = 31 * result + (parentId != null ? parentId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (unit != null ? unit.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (unitType != null ? unitType.hashCode() : 0);
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        result = 31 * result + (updateAt != null ? updateAt.hashCode() : 0);
        return result;
    }
}
