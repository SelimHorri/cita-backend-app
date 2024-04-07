package tn.cita.app.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import tn.cita.app.model.domain.entity.Category;
import tn.cita.app.model.dto.CategoryDto;
import tn.cita.app.model.dto.SaloonDto;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {
	
	public static CategoryDto toDto(@NonNull final Category category) {
		
		final var parentCategory = Objects
				.requireNonNullElseGet(category.getParentCategory(), Category::new);
		
		return CategoryDto.builder()
				.id(category.getId())
				.identifier(category.getIdentifier())
				.name(category.getName())
				.parentCategoryDto(
					CategoryDto.builder()
						.id(parentCategory.getId())
						.identifier(parentCategory.getIdentifier())
						.name(parentCategory.getName())
						.build())
				.saloonDto(
					SaloonDto.builder()
						.id(category.getSaloon().getId())
						.identifier(category.getSaloon().getIdentifier())
						.code(category.getSaloon().getCode())
						.taxRef(category.getSaloon().getTaxRef())
						.name(category.getSaloon().getName())
						.isPrimary(category.getSaloon().getIsPrimary())
						.openingDate(category.getSaloon().getOpeningDate())
						.fullAdr(category.getSaloon().getFullAdr())
						.email(category.getSaloon().getEmail())
						.build())
				.build();
	}
	
}




