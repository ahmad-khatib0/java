package se.magnus.microservices.core.product.services;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import se.magnus.api.core.product.Product;
import se.magnus.microservices.core.product.persistence.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  // The entityToApi() method maps entity objects to the API model object. Since the entity class
  // does not have a field for serviceAddress, the entityToApi() method is annotated to ignore
  // the serviceAddress field in the API model object.
  @Mappings({ @Mapping(target = "serviceAddress", ignore = true) })
  Product entityToApi(ProductEntity entity);

  // The apiToEntity() method maps API model objects to entity objects. In the same way, the
  // apiToEntity() method is annotated to ignore the id and version fields that are missing in
  // the API model class.
  @Mappings({
    @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true)
  })
  ProductEntity apiToEntity(Product api);
}
